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
import bemypet.com.br.bemypet_v1.pojo.Pet;
import bemypet.com.br.bemypet_v1.pojo.Usuario;
import bemypet.com.br.bemypet_v1.utils.Constants;
import bemypet.com.br.bemypet_v1.utils.Utils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void iniciar(View v) {

        buscarUsuarios();
//        Intent intent = new Intent(this, EscolhaActivity.class);

        //Intent intent = new Intent(this, CadastroPetActivity.class);

        //salvarUsuario(Utils.instanciarUsuario("kassi mentz", getApplicationContext()));



    }


//    private void salvarUsuario(Usuario entidade) {
//        final Usuario usuario = entidade;
//
//        //LINHAS ADICIONADAS PARA SALVAR O TOKEN QUE SERA UTILIZADO PARA O PUSH NOTIFICATION
//        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
//        Log.d("FirebaseInstanceId", "Refreshed token: " + refreshedToken);
//
//        usuario.token = refreshedToken;
//
//        FirebaseConnection.getConnection();
//        DatabaseReference connectedReference = FirebaseDatabase.getInstance().getReference(".info/connected");
//
//        connectedReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot snapshot) {
//                boolean connected = snapshot.getValue(Boolean.class);
//                if (connected) {
//                    FirebaseConnection.getDatabase().child("usuarios").child(String.valueOf(usuario.id)).setValue(usuario);
//
//                    GeoFire geoFire = new GeoFire(FirebaseConnection.getDatabase().child("geofire"));
//                    geoFire.setLocation(usuario.id, new GeoLocation(usuario.localizacao.lat, usuario.localizacao.lon), new GeoFire.CompletionListener() {
//                        @Override
//                        public void onComplete(String key, DatabaseError error) {
//                            if (error != null) {
//                                System.err.println("There was an error saving the location to GeoFire: " + error);
//                            } else {
//                                System.out.println("Location saved on server successfully!");
//
//                            }
//                        }
//                    });
//
//
//                } else {
//                    //logar erro
//                }
//            }
//            @Override
//            public void onCancelled(DatabaseError error) {
//                //Log.i("Cancel", "Listener was cancelled");
//            }
//        });
//
//    }

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
                    System.out.println(usuario);
                    if(usuario.id.equalsIgnoreCase("b96b7698-aede-48df-9ffe-89704646768a")) {
                        System.out.println("salvando usuario "+ usuario.nome);
                        Utils.salvarUsuarioSharedPreferences(getApplicationContext(), usuario);
                        Intent intent = new Intent(MainActivity.this, InicialActivity.class);
                        startActivity(intent);
                        MainActivity.this.finish();
                    }


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });


    }
//    private void buscarUsuarios() {
//
//        FirebaseDatabase.getInstance().getReference().child("usuarios").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                Usuario usuario = null;
//
//                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
//                    usuario = postSnapshot.getValue(Usuario.class);
//                    System.out.print("usuario "+ usuario.toString());
//                    if(usuario.id.equalsIgnoreCase("ff0c5d8e-558e-4c45-8a34-ef34dc8a3dc1")) {
//                        System.out.println("salvando usuario "+ usuario.nome);
//                        Utils.salvarUsuarioSharedPreferences(getApplicationContext(), usuario);
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) { }
//        });
//    }



}
