package bemypet.com.br.bemypet_v1;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;

import bemypet.com.br.bemypet_v1.models.OnGetDataListener;
import bemypet.com.br.bemypet_v1.models.PetModel;
import bemypet.com.br.bemypet_v1.pojo.Pet;
import bemypet.com.br.bemypet_v1.pojo.PontoGeo;
import bemypet.com.br.bemypet_v1.utils.Utils;

public class MapaPetsDisponiveisActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private MarkerOptions options = new MarkerOptions();
    List<Pet> pets = new ArrayList<>();

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


        //petModel.salvar(pet);
        //metodo para buscar do arquivo local json. nao é mais necessario
        //List<Pet> pets = Utils.getPetsFromJson(Utils.readJsonFromFile(this, "pets.json"));

        listarPets(googleMap);

    }

    /**
     * Metodo que recebe a lista de pets somente quando esta acaba de ser carregada no firebase
     * O mapa tem de estar dentro do onSuccess para poder carregar os dados, caso contrario, os dados
     * ainda nao estarao carregados quando forem chamados
     * @param googleMap
     */
    private void listarPets(GoogleMap googleMap) {
        final GoogleMap localMap = googleMap;
        PetModel petModel = new PetModel();
        petModel.listar(new OnGetDataListener() {
            @Override
            public void onStart() {
                //DO SOME THING WHEN START GET DATA HERE

            }

            @Override
            public void onSuccess(DataSnapshot data) {
                Pet pet = null;
                //DO SOME THING WHEN GET DATA SUCCESS HERE
                for (DataSnapshot postSnapshot: data.getChildren()) {

                    mMap = localMap;

                    pet = postSnapshot.getValue(Pet.class);
                    System.out.println(pet.nome);
                    System.out.println(pet.toString());
                    options.position(new LatLng((pet.localizacao.lat), pet.localizacao.lon));
                    options.title(pet.nome);
                    mMap.addMarker(options);


                }
                if(pet != null) {
                    PontoGeo ponto = pet.localizacao;
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(ponto.lat, ponto.lon)));
                    localMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(ponto.lat, ponto.lon)));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(ponto.lat, ponto.lon), 12));
                }
            }

            @Override
            public void onFailed(DatabaseError databaseError) {
                //DO SOME THING WHEN GET DATA FAILED HERE
            }
        });
    }
}