package my.neomer.sixtyseconds;

import android.app.Application;

import com.yandex.metrica.YandexMetrica;
import com.yandex.metrica.YandexMetricaConfig;

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        initializeAppMetrica();
    }

    private void initializeAppMetrica() {
        YandexMetricaConfig config = YandexMetricaConfig
                .newConfigBuilder(AppMetricaHelper.AppKey)
                .withAppVersion("1.0.3")
                .withCrashReporting(true)
                .build();
        // Инициализация AppMetrica SDK.
        YandexMetrica.activate(getApplicationContext(), config);
        // Отслеживание активности пользователей.
        YandexMetrica.enableActivityAutoTracking(this);
    }


}
