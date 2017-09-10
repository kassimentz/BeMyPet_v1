package bemypet.com.br.bemypet_v1.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.util.ArrayMap;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import bemypet.com.br.bemypet_v1.InicialActivity;
import bemypet.com.br.bemypet_v1.R;
import bemypet.com.br.bemypet_v1.VisualizarAdocaoAprovadaActivity;
import bemypet.com.br.bemypet_v1.VisualizarAdocaoReprovadaActivity;
import bemypet.com.br.bemypet_v1.VisualizarDenunciaActivity;
import bemypet.com.br.bemypet_v1.VisualizarSolicitacaoAdocaoActivity;
import bemypet.com.br.bemypet_v1.pojo.Notificacoes;
import bemypet.com.br.bemypet_v1.utils.Constants;

/**
 * Created by kassianesmentz on 09/09/17.
 */


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FirebaseMessageService";
    Bitmap bitmap;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        //Define sound URI
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.logo1)
                        .setContentTitle("Notificação Be My Pet")
                        .setSound(soundUri)
                        .setAutoCancel(true)
                        .setContentText(remoteMessage.getNotification().getBody());

        Log.d(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        String strNotificacao = remoteMessage.getData().get("notificacao");
        Notificacoes n = new Gson().fromJson(strNotificacao, Notificacoes.class);
        System.out.println(n.toString());
        Bundle bundle = new Bundle();
        bundle.putSerializable("notificacao", new Gson().toJson(n));
        Intent intent = null;

        if(n.topico.equalsIgnoreCase(Constants.TOPICO_SOLICITACAO_ADOCAO)) {
            intent = new Intent(this, VisualizarSolicitacaoAdocaoActivity.class);
        }

        if(n.topico.equalsIgnoreCase(Constants.TOPICO_ADOÇÃO_APROVADA)) {
            intent = new Intent(this, VisualizarAdocaoAprovadaActivity.class);
        }

        if(n.topico.equalsIgnoreCase(Constants.TOPICO_ADOÇÃO_REPROVADA)) {
            intent = new Intent(this, VisualizarAdocaoReprovadaActivity.class);
        }

        if(n.topico.equalsIgnoreCase(Constants.TOPICO_DENUNCIA)) {
            intent = new Intent(this, VisualizarDenunciaActivity.class);
        }

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(InicialActivity.class);

        intent.putExtras(bundle);

        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify((int) System.currentTimeMillis(), mBuilder.build());

    }

}