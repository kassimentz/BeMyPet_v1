package bemypet.com.br.bemypet_v1.services;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import bemypet.com.br.bemypet_v1.InicialActivity;
import bemypet.com.br.bemypet_v1.R;
import bemypet.com.br.bemypet_v1.pojo.Notificacoes;
import bemypet.com.br.bemypet_v1.utils.Constants;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by kassianesmentz on 09/09/17.
 */

public class NotificacoesService {

    public static void sendNotification(String to, String body, Notificacoes notificacao, Context context) {
        Gson gson = new Gson();

        String title = "Be My Pet";
        int icon = R.drawable.logo1;
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(to);
        String strNotificacao = gson.toJson(notificacao);
        sendMessage(jsonArray,title,body,icon, strNotificacao, context);
    }

    public static void sendMessage(final JSONArray recipients, final String title, final String body,
                                   final int icon, final String notificacao, final Context context) {

        new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... params) {
                try {
                    JSONObject root = new JSONObject();
                    JSONObject notification = new JSONObject();
                    notification.put("body", body);
                    notification.put("title", title);
                    notification.put("icon", icon);

                    JSONObject data = new JSONObject();
                    data.put("notificacao", notificacao);
                    root.put("notification", notification);
                    root.put("data", data);
                    root.put("registration_ids", recipients);

                    String result = postToFCM(root.toString());
                    return result;
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                try {
                    JSONObject resultJson = new JSONObject(result);
                    int success, failure;
                    success = resultJson.getInt("success");
                    failure = resultJson.getInt("failure");
                    Toast.makeText(context, "Notificação enviada.", Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Falha no envio da solicitação. Um erro ocorreu. Tente novamente.", Toast.LENGTH_LONG).show();
                }
            }
        }.execute();
    }

    public static String postToFCM(String bodyString) throws IOException {

        OkHttpClient mClient = new OkHttpClient();

        final MediaType JSON
                = MediaType.parse("application/json; charset=utf-8");

        RequestBody body = RequestBody.create(JSON, bodyString);
        Request request = new Request.Builder()
                .url(Constants.NOTIFICATION_URL)
                .post(body)
                .addHeader("Authorization", "key="+ Constants.NOTIFICATION_KEY)
                .build();
        Response response = mClient.newCall(request).execute();
        return response.body().string();
    }
}
