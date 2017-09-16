package bemypet.com.br.bemypet_v1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;

import bemypet.com.br.bemypet_v1.adapters.SwipeListViewAdapter;
import bemypet.com.br.bemypet_v1.models.FirebaseConnection;
import bemypet.com.br.bemypet_v1.pojo.Notificacoes;
import bemypet.com.br.bemypet_v1.pojo.Usuario;
import bemypet.com.br.bemypet_v1.utils.Utils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void iniciar(View v) {

//        Intent intent = new Intent(this, EscolhaActivity.class);
        Intent intent = new Intent(this, InicialActivity.class);
        //Intent intent = new Intent(this, CadastroPetActivity.class);
        startActivity(intent);
//        salvarUsuario(Utils.instanciarUsuario("kassi 2"));
        testarBuscarLatLongPorEndereco();
        buscarUsuarios();
        this.finish();

    }

    private void testarBuscarLatLongPorEndereco() {
        LatLng latLong =  Utils.getLocationFromAddress(this, "Gabriel Franco da Luz, 560, Porto Alegre, Brasil");
        if(latLong != null) {
            System.out.println(latLong.toString());
        }
    }

    /**
     * Coloquei este metodo para buscar um usuario no banco e salvar ele no app, como se estivesse logado
     * Fiz isso para poder testar as notificacoes somente do usuario logado
     */
    private void buscarUsuarios() {

        FirebaseDatabase.getInstance().getReference().child("usuarios").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Usuario usuario = null;

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    usuario = postSnapshot.getValue(Usuario.class);
                    if(usuario.id.equalsIgnoreCase("b96b7698-aede-48df-9ffe-89704646768a")) {
                        Utils.salvarUsuarioSharedPreferences(getApplicationContext(), usuario);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
    }



}
