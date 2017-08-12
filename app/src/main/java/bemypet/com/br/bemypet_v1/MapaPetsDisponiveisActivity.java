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

public class MapaPetsDisponiveisActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private MarkerOptions options = new MarkerOptions();
    private ArrayList<LatLng> latlngs = new ArrayList<>();

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

        latlngs.add(new LatLng(-29.9834165, -51.1244774)); //some latitude and logitude value
        latlngs.add(new LatLng(-30.0934487,-51.2395593));
        latlngs.add(new LatLng(-30.0186029,-51.0206237));
        latlngs.add(new LatLng(-30.0170309,-51.1660766));



        for (LatLng point : latlngs) {
            options.position(point);
            options.title("someTitle");
            options.snippet("someDesc");
            mMap.addMarker(options);
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latlngs.get(latlngs.size()-1)));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latlngs.get(latlngs.size()-1)));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlngs.get(latlngs.size()-1), 12));


    }
}