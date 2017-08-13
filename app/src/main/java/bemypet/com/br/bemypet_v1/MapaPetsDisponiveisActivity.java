package bemypet.com.br.bemypet_v1;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import bemypet.com.br.bemypet_v1.pojo.Pet;
import bemypet.com.br.bemypet_v1.pojo.PontoGeo;
import bemypet.com.br.bemypet_v1.utils.Utils;

public class MapaPetsDisponiveisActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private MarkerOptions options = new MarkerOptions();

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
        mMap = googleMap;

        List<Pet> pets = Utils.getPetsFromJson(Utils.readJsonFromFile(this, "pets.json"));

        if(!pets.isEmpty()) {
            for (Pet pet : pets) {
                options.position(new LatLng((pet.getLocalizacao().getLat()), pet.getLocalizacao().getLon()));
                options.title(pet.getNome());
                mMap.addMarker(options);
            }
            PontoGeo ponto = pets.get(pets.size() - 1).getLocalizacao();
            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(ponto.getLat(), ponto.getLon())));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(ponto.getLat(), ponto.getLon())));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(ponto.getLat(), ponto.getLon()), 12));
        }

    }
}