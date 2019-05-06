package my.neomer.sixtyseconds.helpers;

import android.app.Activity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class ActivityHelper {

    /**
     * Скрывает клавиатуру
     * @param activity
     */
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view != null) {
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

}
