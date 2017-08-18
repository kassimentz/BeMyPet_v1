package bemypet.com.br.bemypet_v1.models;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import bemypet.com.br.bemypet_v1.MapaPetsDisponiveisActivity;
import bemypet.com.br.bemypet_v1.pojo.Filtros;
import bemypet.com.br.bemypet_v1.pojo.Pet;
import bemypet.com.br.bemypet_v1.pojo.PontoGeo;
import bemypet.com.br.bemypet_v1.services.GPSTracker;
import bemypet.com.br.bemypet_v1.utils.Utils;

/**
 * Created by kassianesmentz on 15/08/17.
 */

public class PetModel implements InterfaceModel<Pet> {

    private List<Pet> pets = new ArrayList<>();
    private Context context;

    public PetModel(Context c) {
        this.context = c;
    }

    @Override
    public void salvar(Pet entidade) {
        final Pet pet = entidade;

        FirebaseConnection.getConnection();
        DatabaseReference connectedReference = FirebaseDatabase.getInstance().getReference(".info/connected");


        connectedReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {
                    FirebaseConnection.getDatabase().child("pets").child(String.valueOf(pet.id)).setValue(pet);
                } else {
                    //logar erro
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                //Log.i("Cancel", "Listener was cancelled");
            }
        });


        GeoFire geoFire = new GeoFire(FirebaseConnection.getDatabase().child("geofire"));
        geoFire.setLocation(pet.id, new GeoLocation(pet.localizacao.lat, pet.localizacao.lon), new GeoFire.CompletionListener() {
            @Override
            public void onComplete(String key, DatabaseError error) {
                if (error != null) {
                    System.err.println("There was an error saving the location to GeoFire: " + error);
                } else {
                    System.out.println("Location saved on server successfully!");
                }
            }
        });
    }

    @Override
    public void listar(OnGetDataListener lis) {
        final OnGetDataListener listener = lis;
        listener.onStart();


        FirebaseDatabase.getInstance().getReference().child("pets").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listener.onSuccess(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onFailed(databaseError);
            }
        });

    }


    @Override
    public Pet procurarPorId(Integer id) {
        return null;
    }

    public List<Pet> getPetsDisponiveis() { return null; }

    public List<Pet> getPetsProximos(PontoGeo ponto) { return null; }

    public List<Pet> filtrarPets(Filtros filtros) { return null; }


    /**
     *
     * Getters e setters
     */
    public List<Pet> getPets() {
        return pets;
    }

    public void addPets(Pet p) {
        this.pets.add(p);
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
