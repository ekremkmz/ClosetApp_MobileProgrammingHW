package tr.edu.yildiz.ekremkamaz;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class AutoStartReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent _intent = new Intent(context,FirebaseService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(_intent);
        } else {
            context.startService(_intent);
        }
    }
}
