package com.uoc.pac2;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.uoc.pac2.model.BookContent;
import com.uoc.pac2.utils.Constants;

import java.util.Map;

import static android.content.Intent.ACTION_DELETE;
import static com.uoc.pac2.utils.Constants.ACTION_VIEW_BOOK_DETAIL;

/**
 * @author Ruben Carmona
 * @project TFM - PAC3
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
        sendNotification(remoteMessage.getNotification(), remoteMessage.getData());
    }

    /**
     * Crea y muestra una notificación al recibir un mensaje de Firebase
     *
     * @param pNotification notificación recibida
     */
    private void sendNotification(RemoteMessage.Notification pNotification, Map<String, String> pData) {

        String title = pNotification.getTitle();
        String shortMessage = getString(R.string.common_message_received);
        String extendedMessage = pNotification.getBody();
        String bookPosition = "";
        String toastMessage = "";

        for (Map.Entry<String, String> entry : pData.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if ("book_position".equals(key)) {
                bookPosition = value;
                break;
            }
        }

        if (TextUtils.isEmpty(title)) {
            title = getString(R.string.common_message_title);
        }

        if (TextUtils.isEmpty(bookPosition)) {
            extendedMessage = getString(R.string.error_message_no_book_position_received);
        } else {
            try {
                BookContent.BookItem bookItem = BookContent.getBooks().get(Integer.valueOf(bookPosition));
                shortMessage = bookItem.getTitle();
                extendedMessage = bookItem.getAuthor() + " - " + shortMessage + "\n\n" + bookItem.getDescription().substring(0, 125) + "...";
                toastMessage = bookItem.getTitle() + " - " + getString(R.string.common_message_delete_ok);
            } catch (Exception ex) {
                Log.e(TAG, ex.getLocalizedMessage());
                extendedMessage = getString(R.string.error_message_book_position_not_valid);
                bookPosition = "";
            }
        }

        Intent intentDeleteBook = new Intent(this, BookListActivity.class);

        intentDeleteBook.setAction(ACTION_DELETE);
        intentDeleteBook.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intentDeleteBook.putExtra(Constants.BOOK_POSITION, bookPosition);
        intentDeleteBook.putExtra(Constants.TOAST_DELETE_MESSAGE, toastMessage);

        PendingIntent deleteBookIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intentDeleteBook, 0);

        Intent intentViewBook = new Intent(this, BookListActivity.class);

        intentViewBook.setAction(ACTION_DELETE);
        intentViewBook.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intentViewBook.putExtra(Constants.BOOK_POSITION, bookPosition);

        intentViewBook.setAction(ACTION_VIEW_BOOK_DETAIL);
        PendingIntent viewBookIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intentViewBook, 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_combination)
                        .setContentTitle(title)
                        .setContentText(shortMessage)
                        .setVibrate(new long[]{1000, 1000})
                        .setLights(Color.BLUE, 3000, 3000)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(extendedMessage));

        if (!TextUtils.isEmpty(bookPosition)) {
            mBuilder
                    .addAction(new NotificationCompat.Action(R.drawable.ic_delete_sweep, getString(R.string.common_delete), deleteBookIntent))
                    .addAction(new NotificationCompat.Action(R.drawable.ic_book, getString(R.string.common_view_book), viewBookIntent));
        }

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // Mostrar la notificación
        mNotificationManager.notify(Constants.NOTIFICATION_ID, mBuilder.build());
    }
}