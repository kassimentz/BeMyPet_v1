package bemypet.com.br.bemypet_v1.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.koushikdutta.ion.Ion;

import java.util.Collections;
import java.util.concurrent.ExecutionException;

import bemypet.com.br.bemypet_v1.InicialActivity;
import bemypet.com.br.bemypet_v1.PerfilPetActivity;
import bemypet.com.br.bemypet_v1.R;
import bemypet.com.br.bemypet_v1.models.FirebaseConnection;
import bemypet.com.br.bemypet_v1.pojo.Filtros;
import bemypet.com.br.bemypet_v1.pojo.Pet;
import bemypet.com.br.bemypet_v1.pojo.PontoGeo;
import bemypet.com.br.bemypet_v1.services.GPSTrackerService;
import bemypet.com.br.bemypet_v1.utils.Constants;
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
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private View rootView;
    GoogleMap map;
    MapView mMapView;
    private MarkerOptions options = new MarkerOptions();
    private PontoGeo ponto = new PontoGeo();
    GPSTrackerService gps;

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
        }
        catch (InflateException e){
            Log.e("MAPA", "Inflate exception");
        }
        return rootView;
    }


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
        System.out.println("caiu aqi");
        if(getMap() != null) {
            exibirMapa();
        } else {
            System.out.println("o mapa é nulo");
        }
        mMapView.onResume();


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        setMap(googleMap);
        gps = new GPSTrackerService(getContext());
        if(!gps.canGetLocation()) {
            gps.showSettingsAlert();
        }
        exibirMapa();
    }

    private void exibirMapa() {
        if(getPontoLocalizacao()) {
            InicialActivity activity = (InicialActivity) getActivity();
            Filtros filtro  = activity.getFiltroActivity();
            System.out.println("tem filtro");
            System.out.println(filtro);
            if(filtro != null) {
                buscarPetsPorFiltro(filtro);
            } else {
                listarPets();
            }

            getMap().setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {

                    Pet p = (Pet) marker.getTag();
                    Intent intent = new Intent(getContext(), PerfilPetActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("pet", new Gson().toJson(p));
                    intent.putExtras(bundle);
                    startActivity(intent);

                    return false;
                }
            });
        }
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

                    pet = postSnapshot.getValue(Pet.class);
                    if(pet.cadastroAtivo && pet.status.equalsIgnoreCase(Constants.STATUS_PET_DISPONIVEL)) {
                        final Pet bkpPet = pet;
                        gerarMarcadorNoMapa(bkpPet);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });

        setZoomIn();
    }

    private void gerarMarcadorNoMapa(final Pet bkpPet) {
        String img = "";
        if(bkpPet.imagens.size() > 0) {
            img = bkpPet.imagens.get(0);
        } else {
            img = "https://firebasestorage.googleapis.com/v0/b/bemypetv1.appspot.com/o/images%2Fperfil_ninhada.png?alt=media&token=c2c3a028-479b-4bd2-8bd1-df7fc661bb66";
        }
        Glide.with(getActivity())
                .asBitmap()
                .load(img)
                .apply(RequestOptions.circleCropTransform())
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        options.position(new LatLng((bkpPet.localizacao.lat), bkpPet.localizacao.lon));
                        options.title(bkpPet.nome);
                        Bitmap smallMarker = Bitmap.createScaledBitmap(resource, 70, 70, false);
                        options.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
                        Marker m = getMap().addMarker(options);
                        m.setTag(bkpPet);
                    }
                });
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

                final String idFound = key;
                FirebaseDatabase.getInstance().getReference().child("pets").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Pet pet = null;

                        for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                            pet = postSnapshot.getValue(Pet.class);
                            if(pet.id.equals(idFound)) {
                                final Pet bkpPet = pet;
                                gerarMarcadorNoMapa(bkpPet);

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

    private void buscarPetsPorFiltro(final Filtros filtro) {

        GeoFire geoFire = new GeoFire(FirebaseConnection.getDatabase().child("geofire"));
        GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(getPonto().lat, getPonto().lon), filtro.raioDeBusca);

        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {

            @Override
            public void onKeyEntered(String key, GeoLocation location) {

                final String idFound = key;
                FirebaseDatabase.getInstance().getReference().child("pets").addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Pet pet = null;
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            pet = postSnapshot.getValue(Pet.class);
                            if(pet == null) {
                                Utils.showToastMessage(getContext(), "Nenhum pet corresponde aos filtros");
                            } else
                            //somente testar os filtros se o pet estiver disponivel para adocao = cadastro ativo
                            if (pet.cadastroAtivo && pet.status.equalsIgnoreCase(Constants.STATUS_PET_DISPONIVEL)) {
                                //realizar os filtros no pet
                                Pet petFiltrado = filtrarPet(pet, filtro);
                                if (petFiltrado != null) {

                                    final Pet bkpPet = pet;
                                    gerarMarcadorNoMapa(bkpPet);
                                }

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
     * Metodo que verifica se o pet esta conforme os filtros
     * @param pet
     * @param filtro
     * @return pet
     */

    private Pet filtrarPet(Pet pet, Filtros filtro) {
        Boolean petValido = Boolean.FALSE;

        if(filtro.especies.contains(pet.especie) || filtro.especies.isEmpty()) {
            petValido = Boolean.TRUE;
        } else {
            petValido = Boolean.FALSE;
            return null;
        }

        if(pet.sexo.equalsIgnoreCase(filtro.sexo) || filtro.sexo.equalsIgnoreCase("TODOS") || filtro.sexo.equalsIgnoreCase("NENHUM")) {
            petValido = Boolean.TRUE;
        } else {
            petValido = Boolean.FALSE;
            return null;
        }

        if(filtro.raca.contains(pet.raca) || filtro.raca.contains("Qualquer") || filtro.raca.isEmpty()) {
            petValido = Boolean.TRUE;
        } else {
            petValido = Boolean.FALSE;
            return null;
        }

        if(pet.idadeAproximada != null && pet.idadeAproximada.length() > 0
                && filtro.idadeInicial != null && filtro.idadeInicial.length() > 0) {
            if (Integer.valueOf(pet.idadeAproximada) >= Integer.valueOf(filtro.idadeInicial) &&
                    Integer.valueOf(pet.idadeAproximada) <= Integer.valueOf(filtro.idadeFinal)) {
                petValido = Boolean.TRUE;
            } else {
                petValido = Boolean.FALSE;
                return null;
            }
        }else {
            petValido = Boolean.TRUE;
        }

        if(pet.pesoAproximado != null && pet.pesoAproximado > 0
                && filtro.pesoInicial != null && filtro.pesoInicial.length() >0
                && filtro.pesoFinal != null && filtro.pesoFinal.length() > 0) {
            if (pet.pesoAproximado >= Integer.valueOf(filtro.pesoInicial) &&
                    pet.pesoAproximado <= Integer.valueOf(filtro.pesoFinal)) {
                petValido = Boolean.TRUE;
            } else {
                petValido = Boolean.FALSE;
                return null;
            }
        } else {
            petValido = Boolean.TRUE;
        }

        if(pet.castrado.equalsIgnoreCase(filtro.castrado) || filtro.castrado.equalsIgnoreCase("TODOS") || filtro.castrado.equalsIgnoreCase("NENHUM")) {
            petValido = Boolean.TRUE;
        } else {
            petValido = Boolean.FALSE;
            return null;
        }

        if(pet.vermifugado.equalsIgnoreCase(filtro.vermifugado) || filtro.vermifugado.equalsIgnoreCase("TODOS") || filtro.vermifugado.equalsIgnoreCase("NENHUM")) {
            petValido = Boolean.TRUE;
        } else {
            petValido = Boolean.FALSE;
            return null;
        }

        //verifica se ao menos um elemento da lista sociavel do pet existe no filtro
        if(pet.sociavel != null) {
            if (!Collections.disjoint(pet.sociavel, filtro.sociavel) || filtro.sociavel.isEmpty()) {
                petValido = Boolean.TRUE;

            } else {
                petValido = Boolean.FALSE;
                return null;

            }
        } else {
            petValido = Boolean.TRUE;
        }

        if(pet.temperamento != null) {
            //verifica se ao menos um elemento da lista temperamento do pet existe no filtro
            if (!Collections.disjoint(pet.temperamento, filtro.temperamento) || filtro.temperamento.isEmpty()) {
                petValido = Boolean.TRUE;

            } else {
                petValido = Boolean.FALSE;
                return null;
            }
        } else {
            petValido = Boolean.TRUE;
        }


        if(petValido) {
            return pet;
        } else {
            Utils.showToastMessage(getContext(), "Nenhum pet corresponde aos filtros");
            return null;
        }
    }

    /**
     * Método que busca a localiazacao pelo gps do dispositivo
     */
    private boolean getPontoLocalizacao() {

        GPSTrackerService gpsTrackerService = new GPSTrackerService(getContext());
        PontoGeo pontoGps = new PontoGeo(gpsTrackerService.getLatitude(), gpsTrackerService.getLongitude());
        gpsTrackerService.stopUsingGPS();
        if(pontoGps != null && pontoGps.lat != 0.0) {
            setPonto(pontoGps);
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    /**
     * Método que utiliza a localizacao encontrada no GPS do dispositivo para dar zoom no mapa
     */
    private void setZoomIn() {
        getMap().moveCamera(CameraUpdateFactory.newLatLng(new LatLng(getPonto().lat, getPonto().lon)));
        getMap().moveCamera(CameraUpdateFactory.newLatLng(new LatLng(getPonto().lat, getPonto().lon)));
        getMap().animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(getPonto().lat, getPonto().lon), 12));
        gps.stopUsingGPS();
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