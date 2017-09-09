package bemypet.com.br.bemypet_v1;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.maps.android.PolyUtil;
import com.koushikdutta.ion.Ion;

import java.util.concurrent.ExecutionException;

import bemypet.com.br.bemypet_v1.pojo.Adocao;
import bemypet.com.br.bemypet_v1.pojo.Notificacoes;
import bemypet.com.br.bemypet_v1.pojo.Pet;
import bemypet.com.br.bemypet_v1.pojo.PontoGeo;
import bemypet.com.br.bemypet_v1.pojo.Usuario;
import bemypet.com.br.bemypet_v1.pojo.map.Retorno;
import bemypet.com.br.bemypet_v1.utils.Constants;
import bemypet.com.br.bemypet_v1.utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VisualizarAdocaoAprovadaActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private TextView txtAdocaoAprovada, txtTelefoneDoador, txtEmailDoador;
    private Notificacoes notificacao;
    private Adocao adocao;
    private Usuario doador;
    private Pet pet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_adocao_aprovada);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.visualizarAdocaoAprovadaToolbar);
        setSupportActionBar(myToolbar);

        ActionBar ab = getSupportActionBar();
        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        ab.setTitle(R.string.activity_title_adocao_aprovada);

        initializeVariables();

        getBundle();

        if(getNotificacao() != null) {
            preencherDados();
        }
    }

    private void initializeVariables() {
        txtAdocaoAprovada = (TextView) findViewById(R.id.txtAdocaoAprovada);
        txtTelefoneDoador = (TextView) findViewById(R.id.txtTelefoneDoador);
        txtEmailDoador = (TextView) findViewById(R.id.txtEmailDoador);
    }

    private void getBundle() {

        String jsonNotificacao = null;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            jsonNotificacao = extras.getString("notificacao");
        }
        Notificacoes notificacao = new Gson().fromJson(jsonNotificacao, Notificacoes.class);
        if(notificacao != null) {
            setNotificacao(notificacao);
            setAdocao(notificacao.adocao);
            setDoador(notificacao.adocao.doador);
            setPet(notificacao.adocao.pet);
        }
    }

    private void preencherDados() {

        if(getPet().nome != null && getDoador().nome != null) {
            StringBuilder texto = new StringBuilder();
            texto.append("A adoção de ");
            texto.append(getPet().nome);
            texto.append(" foi aprovada! Converse com ");
            texto.append(getDoador().nome);
            texto.append(" e combine para buscá-lo!");
            txtAdocaoAprovada.setText(texto.toString());
        }

        if(getDoador().telefone != null) {
            txtTelefoneDoador.setText(getDoador().telefone);
        }

        if(getDoador().email != null) {
            txtEmailDoador.setText(getDoador().email);
        }
    }

    private void updateNotificacao() {
        final DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("notificacoes");
        myRef.child(getNotificacao().id).child("lida").setValue(getNotificacao().lida);
    }

    public void fecharVisualizacaoAprovacao(View v) {
        getNotificacao().lida = Boolean.TRUE;
        updateNotificacao();
        this.finish();
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

        Bitmap bmImg = null;
        try {
            bmImg = Ion.with(this).load(pet.imagens.get(0)).asBitmap().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        MarkerOptions options = new MarkerOptions();
        LatLng petLocal = new LatLng(getPet().localizacao.lat, getPet().localizacao.lon);
        options.icon(BitmapDescriptorFactory.fromBitmap(Utils.getRoundedCroppedBitmap(bmImg, 45)));
        options.position(petLocal);
        options.title(getPet().nome);
        Marker m = mMap.addMarker(options);
        setZoomIn(petLocal);
        showRota();
    }

    /**
     * Método que utiliza a localizacao encontrada no GPS do dispositivo para dar zoom no mapa
     */
    private void setZoomIn(LatLng local) {

        mMap.moveCamera(CameraUpdateFactory.newLatLng(local));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(local));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(local, 12));
    }

    private void showRota(){


        StringBuilder origem = new StringBuilder();
        origem.append(getAdocao().adotante.endereco+", ");
        origem.append(getAdocao().adotante.numero+", ");
        origem.append(getAdocao().adotante.cidade);


        StringBuilder destino = new StringBuilder();
        destino.append(getDoador().endereco+", ");
        destino.append(getDoador().numero+", ");
        destino.append(getDoador().cidade);
        Call<Retorno> call = ((BeMyPetApplication) getApplication()).service.searchPositions(origem.toString(), destino.toString(), Constants.GOOGLE_MAPS_API_RELEASE);

        call.enqueue(new Callback<Retorno>() {
            @Override
            public void onResponse(Call<Retorno> call, Response<Retorno> response) {
                String points = response.body().routes.get(0).overview_polyline.points;
                if(points != null) {
                    PolylineOptions polylineOptions = new PolylineOptions();
                    polylineOptions.addAll(PolyUtil.decode(points));
                    polylineOptions.color(getResources().getColor(R.color.GreenButton));
                    mMap.addPolyline(polylineOptions);
                }

            }

            @Override
            public void onFailure(Call<Retorno> call, Throwable t) {
            }
        });


    }

    public Notificacoes getNotificacao() {
        return notificacao;
    }

    public void setNotificacao(Notificacoes notificacao) {
        this.notificacao = notificacao;
    }

    public Adocao getAdocao() {
        return adocao;
    }

    public void setAdocao(Adocao adocao) {
        this.adocao = adocao;
    }

    public Usuario getDoador() {
        return doador;
    }

    public void setDoador(Usuario doador) {
        this.doador = doador;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

}
