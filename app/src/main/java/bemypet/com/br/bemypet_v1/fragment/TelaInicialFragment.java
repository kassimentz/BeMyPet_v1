package bemypet.com.br.bemypet_v1.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.koushikdutta.ion.Ion;

import java.util.concurrent.ExecutionException;

import bemypet.com.br.bemypet_v1.R;
import bemypet.com.br.bemypet_v1.models.FirebaseConnection;
import bemypet.com.br.bemypet_v1.pojo.Pet;
import bemypet.com.br.bemypet_v1.pojo.PontoGeo;
import bemypet.com.br.bemypet_v1.utils.Utils;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TelaInicialFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TelaInicialFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TelaInicialFragment extends Fragment implements OnMapReadyCallback{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View rootView;
    GoogleMap map;
    MapView mMapView;

    private MarkerOptions options = new MarkerOptions();
    private PontoGeo ponto = new PontoGeo();


    private OnFragmentInteractionListener mListener;

    public TelaInicialFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TelaInicialFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TelaInicialFragment newInstance(String param1, String param2) {
        TelaInicialFragment fragment = new TelaInicialFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        try {
            rootView = inflater.inflate(R.layout.fragment_tela_inicial, container, false);
            MapsInitializer.initialize(this.getActivity());
            mMapView = (MapView) rootView.findViewById(R.id.mapView);
            mMapView.onCreate(savedInstanceState);
            mMapView.getMapAsync(this);
            getPontoLocalizacao();
        }
        catch (InflateException e){
            Log.e("MAPA", "Inflate exception");
        }
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        setMap(googleMap);
        listarPets();
    }

    /**
     * Metodo que recebe a lista de pets somente quando esta acaba de ser carregada no firebase
     * O mapa tem de estar dentro do onSuccess para poder carregar os dados, caso contrario, os dados
     * ainda nao estarao carregados quando forem chamados
     *
     */
    private void listarPets() {

        FirebaseDatabase.getInstance().getReference().child("pets").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Pet pet = null;
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {

                    map = getMap();
                    try {
                        pet = postSnapshot.getValue(Pet.class);
                        options.position(new LatLng((pet.localizacao.lat), pet.localizacao.lon));
                        options.title(pet.nome);
                        Bitmap bmImg = Ion.with(getContext()).load(pet.imagens.get(0)).asBitmap().get();
                        options.icon(BitmapDescriptorFactory.fromBitmap(Utils.getRoundedCroppedBitmap(bmImg, 90)));
                        map.addMarker(options);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });

        setZoomIn();
    }

    /**
     * Método para listar pets proximos ao ponto do dispositivo
     */
    private void listarPetsProximos() {

        GeoFire geoFire = new GeoFire(FirebaseConnection.getDatabase().child("geofire"));
        GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(getPonto().lat, getPonto().lon), 20.0);

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
                                map = getMap();
                                options.position(new LatLng((pet.localizacao.lat), pet.localizacao.lon));
                                options.title(pet.nome);
                                map.addMarker(options);

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

        setZoomIn();


    }

    /**
     * Método que busca a localiazacao pelo gps do dispositivo
     */
    private void getPontoLocalizacao() {
        PontoGeo ponto = Utils.getLatLongDispositivo(getContext(), null);
        setPonto(ponto);

    }

    /**
     * Método que utiliza a localizacao encontrada no GPS do dispositivo para dar zoom no mapa
     */
    private void setZoomIn() {
        getMap().moveCamera(CameraUpdateFactory.newLatLng(new LatLng(getPonto().lat, getPonto().lon)));
        getMap().moveCamera(CameraUpdateFactory.newLatLng(new LatLng(getPonto().lat, getPonto().lon)));
        getMap().animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(getPonto().lat, getPonto().lon), 12));
    }

    /**
     * Método para salva pets
     * @param entidade
     */
    private void salvarPet(Pet entidade) {
        final Pet pet = entidade;

        FirebaseConnection.getConnection();
        DatabaseReference connectedReference = FirebaseDatabase.getInstance().getReference(".info/connected");


        connectedReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {
                    FirebaseConnection.getDatabase().child("pets").child(String.valueOf(pet.id)).setValue(pet);

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
                } else {
                    //logar erro
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                //Log.i("Cancel", "Listener was cancelled");
            }
        });



    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    /*=========GETTERS E SETTERS=============*/

    public GoogleMap getMap() {
        return map;
    }

    public void setMap(GoogleMap mMap) {
        this.map = mMap;
    }

    public PontoGeo getPonto() {
        return ponto;
    }

    public void setPonto(PontoGeo ponto) {
        this.ponto = ponto;
    }
}