package my.neomer.sixtyseconds.core;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ForwardPipeline<T extends Parcelable> implements Pipeline<T> {

    private static final String TAG = "ForwardPipeline";

    //region Parcelable

    ForwardPipeline(Parcel in) {
        int listCount = in.readInt();
        for (int i = 0; i < listCount; ++i) {
            Class<?> elementClass = (Class<?>) in.readSerializable();
            if (elementClass != null) {
                ClassLoader classLoader = elementClass.getClassLoader();
                if (classLoader != null) {
                    T item = in.readParcelable(classLoader);
                    if (item != null) {
                        list.add(item);
                    }
                } else {
                    Log.e(TAG, "Element class has no ClassLoader!");
                }
            } else {
                Log.e(TAG, "elementClass is null!");
            }
        }

        idx = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(list.size());
        for (T item : list) {
            Class<?> itemClass = item.getClass();
            dest.writeSerializable(itemClass);
            dest.writeParcelable((Parcelable) item, 0);
        }
        dest.writeInt(idx);
    }

    public static final Parcelable.Creator<ForwardPipeline> CREATOR = new Parcelable.Creator<ForwardPipeline>() {
        public ForwardPipeline createFromParcel(Parcel in) {
            return new ForwardPipeline(in);
        }

        public ForwardPipeline[] newArray(int size) {
            return new ForwardPipeline[size];
        }
    };

    //endregion

    List<T> list = new ArrayList<>();
    int idx = 0;

    public ForwardPipeline() {

    }

    @SafeVarargs
    public ForwardPipeline(T ...values)
    {
        Collections.addAll(list, values);
    }

    @Nullable
    @Override
    public T current() {
        return list != null ? list.get(idx) : null;
    }

    @Nullable
    @Override
    public T next() {
        return list != null ? list.get(++idx) : null;
    }

    @Override
    public boolean isEnd() {
        return idx >= list.size();
    }
}
