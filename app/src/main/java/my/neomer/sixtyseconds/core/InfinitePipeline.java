package my.neomer.sixtyseconds.core;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

public class InfinitePipeline<T extends Parcelable> extends ForwardPipeline<T> {

    //region Parcelable

    public static final Parcelable.Creator<InfinitePipeline> CREATOR = new Parcelable.Creator<InfinitePipeline>() {
        public InfinitePipeline createFromParcel(Parcel in) {
            return new InfinitePipeline(in);
        }

        public InfinitePipeline[] newArray(int size) {
            return new InfinitePipeline[size];
        }
    };

    private InfinitePipeline(Parcel in) {
        super(in);
        returnIndex = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(returnIndex);
    }

    //endregion

    private int returnIndex;

    public InfinitePipeline() {
    }

    @SafeVarargs
    public InfinitePipeline(T... values) {
        super(values);
        this.returnIndex = -1;
    }


    @SafeVarargs
    public InfinitePipeline(int returnIndex, T... values) {
        super(values);
        this.returnIndex = returnIndex - 1;
    }

    @Nullable
    @Override
    public T next() {
        if (idx == list.size() - 1) {
            idx = this.returnIndex;
        }
        return super.next();
    }

    @Override
    public boolean isEnd() {
        return false;
    }
}
