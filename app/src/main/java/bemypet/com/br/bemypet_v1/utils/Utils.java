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
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import bemypet.com.br.bemypet_v1.pojo.Pet;
import bemypet.com.br.bemypet_v1.pojo.PontoGeo;
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
            FileWriter file = new FileWriter("/data/data/" + context.getPackageName() + "/" + jsonName);
            file.write(json);
            file.flush();
            file.close();
            return Boolean.TRUE;
        } catch (IOException e) {
            e.printStackTrace();
            return Boolean.FALSE;
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
            System.out.println("activity null");
            gps = new GPSTracker(context);
        } else {
            gps = new GPSTracker(context, activity);
            System.out.println("activity not null");
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
}
