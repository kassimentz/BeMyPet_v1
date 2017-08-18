package bemypet.com.br.bemypet_v1;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import bemypet.com.br.bemypet_v1.models.FirebaseConnection;
import bemypet.com.br.bemypet_v1.models.OnGetDataListener;
import bemypet.com.br.bemypet_v1.models.PetModel;
import bemypet.com.br.bemypet_v1.pojo.Pet;
import bemypet.com.br.bemypet_v1.pojo.PontoGeo;
import bemypet.com.br.bemypet_v1.services.GPSTracker;
import bemypet.com.br.bemypet_v1.utils.Utils;

public class MapaPetsDisponiveisActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private MarkerOptions options = new MarkerOptions();
    PetModel petModel = new PetModel(this);

    public GoogleMap getmMap() {
        return mMap;
    }

    public void setmMap(GoogleMap mMap) {
        this.mMap = mMap;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa_pets_disponiveis);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        List<Pet> pets = Utils.getPetsFromJson(Utils.readJsonFromFile(this, "pets.json"));
//        for (Pet p:pets) {
//            petModel.salvar(p);
//            System.out.println(p.nome);
//        }
        setmMap(googleMap);

        //metodo para buscar do arquivo local json. nao é mais necessario
        //List<Pet> pets = Utils.getPetsFromJson(Utils.readJsonFromFile(this, "pets.json"));

        listarPets();
        //listarPetsProximos();
    }

    /**
     * Metodo que recebe a lista de pets somente quando esta acaba de ser carregada no firebase
     * O mapa tem de estar dentro do onSuccess para poder carregar os dados, caso contrario, os dados
     * ainda nao estarao carregados quando forem chamados
     *
     */
    private void listarPets() {

        PontoGeo ponto = Utils.getLatLongDispositivo(this, MapaPetsDisponiveisActivity.this);
        petModel.listar(new OnGetDataListener() {
            @Override
            public void onStart() {}

            @Override
            public void onSuccess(DataSnapshot data) {
                Pet pet = null;
                for (DataSnapshot postSnapshot: data.getChildren()) {

                    mMap = getmMap();

                    pet = postSnapshot.getValue(Pet.class);
                    options.position(new LatLng((pet.localizacao.lat), pet.localizacao.lon));
                    options.title(pet.nome);
                    mMap.addMarker(options);


                }

            }

            @Override
            public void onFailed(DatabaseError databaseError) {}
        });

        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(ponto.lat, ponto.lon)));
        getmMap().moveCamera(CameraUpdateFactory.newLatLng(new LatLng(ponto.lat, ponto.lon)));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(ponto.lat, ponto.lon), 12));
    }

    public void listarPetsProximos() {

        //buscar a latitude e longitude do dispositivo
        PontoGeo ponto = Utils.getLatLongDispositivo(this, MapaPetsDisponiveisActivity.this);
        GeoFire geoFire = new GeoFire(FirebaseConnection.getDatabase().child("geofire"));
        GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(ponto.lat, ponto.lon), 20.0);

        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {

                System.out.println(key);
                final String idFound = key;
                FirebaseDatabase.getInstance().getReference().child("pets").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Pet pet = null;

                        for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                            pet = postSnapshot.getValue(Pet.class);
                            if(pet.id.equals(idFound)) {
                                System.out.println(pet.nome);
                                mMap = getmMap();
                                options.position(new LatLng((pet.localizacao.lat), pet.localizacao.lon));
                                options.title(pet.nome);
                                mMap.addMarker(options);

                            }
                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onKeyExited(String key) {
                System.out.println(String.format("Key %s is no longer in the search area", key));
            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {
                System.out.println(String.format("Key %s moved within the search area to [%f,%f]", key, location.latitude, location.longitude));
            }

            @Override
            public void onGeoQueryReady() {
                System.out.println("All initial data has been loaded and events have been fired!");
            }

            @Override
            public void onGeoQueryError(DatabaseError error) {
                System.err.println("There was an error with this query: " + error);
            }
        });


        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(ponto.lat, ponto.lon)));
        getmMap().moveCamera(CameraUpdateFactory.newLatLng(new LatLng(ponto.lat, ponto.lon)));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(ponto.lat, ponto.lon), 12));


    }
}