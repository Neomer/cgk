package my.neomer.sixtyseconds.core;

import android.os.Parcelable;
import android.support.annotation.Nullable;

public interface Pipeline<T extends Parcelable> extends Parcelable {

    @Nullable T current();

    @Nullable T next();

    boolean isEnd();

}
