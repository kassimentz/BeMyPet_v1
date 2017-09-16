package bemypet.com.br.bemypet_v1;

import android.app.Application;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import bemypet.com.br.bemypet_v1.services.BeMyPetRetrofitInterface;
import bemypet.com.br.bemypet_v1.services.LoggingInterceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by kassianesmentz on 15/08/17.
 */

public class BeMyPetApplication extends Application {

    public BeMyPetRetrofitInterface service;

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        DatabaseReference scoresRef = FirebaseDatabase.getInstance().getReference("bemypetv1");
        scoresRef.keepSynced(true);

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor( new LoggingInterceptor() ).build();

        Gson gson = new GsonBuilder().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();

        service = retrofit.create(BeMyPetRetrofitInterface.class);
    }
}
