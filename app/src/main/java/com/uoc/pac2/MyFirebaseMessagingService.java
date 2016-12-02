package com.uoc.pac2;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import static android.content.Intent.ACTION_DELETE;
import static com.uoc.pac2.utils.Constants.ACTION_VIEW_BOOK_DETAIL;

/**
 * @author Ruben Carmona
 * @project TFM - PAC2
 * @date 11/2016
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    /**
     * Método llamado cuando se recibe un mensaje remoto
     *
     * @param remoteMessage Mensaje recibido de Firebase Cloud Messaging.
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Mostrar una notificación al recibir un mensaje de Firebase
        sendNotification(remoteMessage.getNotification().getBody());
    }

    /**
     * Crea y muestra una notificación al recibir un mensaje de Firebase
     *
     * @param messageBody Texto a mostrar en la notificación
     */
    private void sendNotification(String messageBody) {

        Intent intent = new Intent(this, BookListActivity.class);
        intent.setAction(ACTION_DELETE);
        PendingIntent borrarIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);
        Intent intent2 = new Intent(this, BookListActivity.class);
        intent2.setAction(ACTION_VIEW_BOOK_DETAIL);
        PendingIntent resendIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent2, 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_pages_black)
                        .setContentTitle("Mi primera notificación")
                        .setContentText("Hola mundo")
                        .setStyle(new NotificationCompat.BigTextStyle().bigText("Estos son los detalles expandidos de la notificación anterior, aquí se puede escribir más texto para que lo lea el usuario."))
                        .addAction(new NotificationCompat.Action(android.R.drawable.ic_menu_delete, "Borrar", borrarIntent))
                        .addAction(new NotificationCompat.Action(R.drawable.ic_book_black, "View Book", resendIntent));

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // Mostrar la notificación
        mNotificationManager.notify(0, mBuilder.build());
    }
}