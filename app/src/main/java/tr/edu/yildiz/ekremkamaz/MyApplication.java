package tr.edu.yildiz.ekremkamaz;

import android.app.Application;
import android.content.Intent;
import android.os.Build;
import android.os.StrictMode;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Method m = null;
        try {
            m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
            m.invoke(null);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        boolean firstRun = getSharedPreferences("preferences", MODE_PRIVATE).getBoolean("firstrun", true);
        if (firstRun) {
            getSharedPreferences("preferences", MODE_PRIVATE).edit().putBoolean("firstrun", false).commit();
            Intent _intent = new Intent(this, FirebaseService.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(_intent);
            } else {
                startService(_intent);
            }
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        Intent _intent = new Intent(this, FirebaseService.class);
        stopService(_intent);
    }
}
