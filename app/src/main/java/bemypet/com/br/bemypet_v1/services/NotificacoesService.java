package bemypet.com.br.bemypet_v1.services;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

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

    public static void sendNotificationDenuncia(String to, String body, Notificacoes notificacao, Context context) {
        String title = "Be My Pet";
        int icon = R.drawable.logo1;
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(to);
        //to, title, body, icon, message, adotante, doador, pet, tipoNotificacao
        sendMessagDenuncia(jsonArray,title,body,icon,notificacao.mensagem, notificacao.denuncia.denunciado.nome,
                notificacao.denuncia.denunciante.nome, notificacao.tipoNotificacao, context);
    }

    private static void sendMessagDenuncia(final JSONArray recipients, final String title, final String body, final int icon,
                                           final String mensagem, final String denunciado, final String denunciante,
                                           final String tipoNotificacao, final Context context) {

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
                    data.put("message", mensagem);
                    data.put("denunciado", denunciado);
                    data.put("denunciante", denunciante);
                    data.put("tipoNotificacao", tipoNotificacao);
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
//                    Intent i = new Intent(context, InicialActivity.class);
//                    context.startActivity(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Falha no envio da solicitação. Um erro ocorreu. Tente novamente.", Toast.LENGTH_LONG).show();
                }
            }
        }.execute();
    }


    public static void sendNotification(String to, String body, Notificacoes notificacao, Context context) {
        String title = "Be My Pet";
        int icon = R.drawable.logo1;
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(to);
        //to, title, body, icon, message, adotante, doador, pet, tipoNotificacao
        sendMessage(jsonArray,title,body,icon,notificacao.mensagem, notificacao.adocao.adotante.nome,
                notificacao.adocao.doador.nome, notificacao.adocao.pet.nome, notificacao.tipoNotificacao,
                notificacao.adocao.adotante.getLogradouro() , notificacao.adocao.doador.getLogradouro(), context);
    }

    public static void sendMessage(final JSONArray recipients, final String title, final String body,
                                   final int icon, final String message, final String cpfAdotante,
                                   final String cpfDoador, final String idPet, final String tipoNotificacao,
                                   final String origem, final String destino, final Context context) {

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
                    data.put("message", message);
                    data.put("cpfAdotante", cpfAdotante);
                    data.put("cpfDoador", cpfDoador);
                    data.put("idPet", idPet);
                    data.put("tipoNotificacao", tipoNotificacao);
                    data.put("origem", origem);
                    data.put("destino", destino);
                    root.put("notification", notification);
                    root.put("data", data);
                    root.put("registration_ids", recipients);

                    String result = postToFCM(root.toString());
                    Log.d("TermosAdocao", "Result: " + result);
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
//                    Intent i = new Intent(context, InicialActivity.class);
//                    context.startActivity(i);
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
