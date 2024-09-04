package com.example.siasmobile.Notificacao;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import androidx.core.app.NotificationCompat;

import com.example.siasmobile.R;

public class NotificationUtils {

    private static final String CHANNEL_ID = "default_channel_id";
    private static final String CHANNEL_NAME = "Default Channel";

    public void sendNotification(Context context, String title, String message) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Configura o canal de notificação (para Android 8.0 e superior)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        // Cria a notificação
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID) // Usando o context passado como parâmetro
                .setSmallIcon(R.drawable.sias) // Ícone da notificação
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        // Exibe a notificação
        notificationManager.notify(1, builder.build());
    }
}