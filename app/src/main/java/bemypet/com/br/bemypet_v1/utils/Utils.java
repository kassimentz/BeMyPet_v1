package bemypet.com.br.bemypet_v1.utils;

import android.content.Context;
import android.content.res.AssetManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
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

    public static List<Pet> getPetsFromJson(String jsonString) {
        List<Pet> petList = new ArrayList<>();

        if(!jsonString.isEmpty()) {
            Gson gson = new Gson();
            Type listPetType = new TypeToken<ArrayList<Pet>>() {}.getType();
            petList = gson.fromJson(jsonString, listPetType);
        }

        return petList;
    }

    public static PontoGeo getLatLongDispositivo(Context context) {
        // GPSTracker class
        GPSTracker gps;
        Double latitude = null;
        Double longitude = null;
        PontoGeo ponto = new PontoGeo();

        gps = new GPSTracker(context);
        if (gps.canGetLocation()) {
            ponto.lat = gps.getLatitude();
            ponto.lon = gps.getLongitude();
        } else {
            gps.showSettingsAlert();
        }

        return ponto;
    }
}
