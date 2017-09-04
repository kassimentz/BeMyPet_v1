package bemypet.com.br.bemypet_v1.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import bemypet.com.br.bemypet_v1.pojo.Denuncias;
import bemypet.com.br.bemypet_v1.pojo.Notificacoes;
import bemypet.com.br.bemypet_v1.pojo.Pet;
import bemypet.com.br.bemypet_v1.pojo.PontoGeo;
import bemypet.com.br.bemypet_v1.pojo.Usuario;
import bemypet.com.br.bemypet_v1.services.GPSTracker;

/**
 * Created by kassianesmentz on 12/08/17.
 */

public class Utils {

    public static String readJsonFromFile(Context context, String jsonName) {

        StringBuilder buf = new StringBuilder();
        try {
            InputStream json = context.getAssets().open(jsonName);
            BufferedReader in = new BufferedReader(new InputStreamReader(json, "UTF-8"));
            String str;

            while ((str=in.readLine()) != null) {
                buf.append(str);
            }

            in.close();
        } catch (IOException e) {
            e.getMessage();
        }

       return buf.toString();
    }

    public static Boolean saveJsonFile(Context context, String jsonName, String json) {

        try {
            FileOutputStream fileOutputStream = context.openFileOutput(jsonName ,Context.MODE_PRIVATE);
            fileOutputStream.write(json.getBytes("UTF-8"));
            fileOutputStream.close();
            return Boolean.TRUE;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return Boolean.FALSE;
        } catch (IOException e) {
            e.printStackTrace();
            return Boolean.FALSE;
        }

    }

    public static boolean fileExists(Context context, String fileName){
        File file = context.getFileStreamPath(fileName);
        return file.exists();
    }

    public static String readStringFromFile(Context context, String fileName) {

        try {
            FileInputStream fileInputStream = context.openFileInput(fileName);

            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuffer stringBuffer = new StringBuffer();
            String lines;
            while ((lines=bufferedReader.readLine())!=null) {
                stringBuffer.append(lines+"\n");
            }
            return stringBuffer.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<Pet> getPetsFromJson(String jsonString) {
        List<Pet> petList = new ArrayList<>();

        if(!jsonString.isEmpty()) {
            Gson gson = new Gson();
            Type listPetType = new TypeToken<ArrayList<Pet>>() {}.getType();
            petList = gson.fromJson(jsonString, listPetType);
        }

        return petList;
    }

    public static PontoGeo getLatLongDispositivo(Context context, Activity activity) {
        // GPSTracker class
        GPSTracker gps;
        Double latitude = null;
        Double longitude = null;
        PontoGeo ponto = new PontoGeo();

        if(activity == null) {
            gps = new GPSTracker(context);
        } else {
            gps = new GPSTracker(context, activity);
        }


        if (gps.canGetLocation()) {
            ponto.lat = gps.getLatitude();
            ponto.lon = gps.getLongitude();
        } else {
            gps.showSettingsAlert();
        }

        return ponto;
    }

    public static Bitmap getRoundedCroppedBitmap(Bitmap bitmap, int radius) {
        Bitmap finalBitmap;
        if (bitmap.getWidth() != radius || bitmap.getHeight() != radius)
            finalBitmap = Bitmap.createScaledBitmap(bitmap, radius, radius,false);
        else
            finalBitmap = bitmap;

        Bitmap output = Bitmap.createBitmap(finalBitmap.getWidth(),finalBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, finalBitmap.getWidth(),finalBitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.parseColor("#BAB399"));
        canvas.drawCircle(finalBitmap.getWidth() / 2 + 0.7f,
                finalBitmap.getHeight() / 2 + 0.7f,
                finalBitmap.getWidth() / 2 + 0.1f, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(finalBitmap, rect, rect, paint);
        finalBitmap.recycle();
        return output;
    }

    public static void showToastMessage(Context context, String msg) {
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        toast.show();
    }

    public static int getSpinnerIndex(Spinner spinner, String myString) {
        int index = 0;

        for (int i=0; i < spinner.getCount(); i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                index = i;
                break;
            }
        }
        return index;
    }

    public static String getCurrentDateTime() {
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String strDate = dateFormat.format(date);
        return strDate;
    }

    /**
     * Metodo utilizado para salvar um usuario em formato string (convertido usando o GSON)
     * dentro das shared preferences
     * @param context
     */
    public static void salvarUsuarioSharedPreferences(Context context) {
        Gson gson = new Gson();
        String json = gson.toJson(instanciarUsuario());
        ManagerPreferences.saveString(context, Constants.USUARIO_LOGADO, json);
        System.out.println("Usuario salvo");
    }

    /**
     * gera um usuario mockado enquanto os cadastros nao estao prontos
     * @return
     */
    private static Usuario instanciarUsuario() {
        Usuario usuario = new Usuario();

        usuario.nome = "nome doador";
        List<String> imagens = new ArrayList<>();
        imagens.add("https://firebasestorage.googleapis.com/v0/b/bemypet-61485.appspot.com/o/images%2Fimages%20(2).jpg?alt=media&token=8b0bb91b-f11c-4d59-a9a6-9a972732e284");
        usuario.imagens = imagens;
        usuario.dataNascimento = "28/11/1986";
        usuario.cpf = "001.239.752.23";
        usuario.localizacao = new PontoGeo(-29.856, -51.234);
        usuario.cep = 91120415;
        usuario.endereco = "gabriel franco da luz";
        usuario.numero = 560;
        usuario.complemento = "apto 206F";
        usuario.bairro = "sarandi";
        usuario.cidade = "porto alegre";
        usuario.estado = "RS";
        usuario.telefone = "434343434";
        usuario.email = "cassio@teste.com";
        usuario.meusPets = new ArrayList<Pet>();
        usuario.petsFavoritos = new ArrayList<Pet>();
        usuario.denuncias = new ArrayList<Denuncias>();
        usuario.notificacoes = new ArrayList<Notificacoes>();
        return usuario;
    }

    public static Usuario getUsuarioSharedPreferences(Context context) {

        String json = ManagerPreferences.getString(context, Constants.USUARIO_LOGADO);
        Gson gson = new Gson();
        Usuario usuario = new Gson().fromJson(json, Usuario.class);
        if(usuario != null) {
            return usuario;
        } else {
            return null;
        }
    }
}
