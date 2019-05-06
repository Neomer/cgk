package my.neomer.sixtyseconds;

import android.app.Application;

import com.yandex.metrica.YandexMetrica;
import com.yandex.metrica.YandexMetricaConfig;

import my.neomer.sixtyseconds.helpers.AppMetricaHelper;

public class MyApp extends Application {

    public static final String Version = BuildConfig.VERSION_NAME;

    @Override
    public void onCreate() {
        super.onCreate();

        initializeAppMetrica();
    }

    private void initializeAppMetrica() {
        YandexMetricaConfig config = YandexMetricaConfig
                .newConfigBuilder(AppMetricaHelper.AppKey)
                .withAppVersion(MyApp.Version)
                .withCrashReporting(true)
                .build();
        // Инициализация AppMetrica SDK.
        YandexMetrica.activate(getApplicationContext(), config);
        // Отслеживание активности пользователей.
        YandexMetrica.enableActivityAutoTracking(this);
    }


}
