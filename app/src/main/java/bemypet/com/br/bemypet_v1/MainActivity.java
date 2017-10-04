package bemypet.com.br.bemypet_v1;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;


import bemypet.com.br.bemypet_v1.models.FirebaseConnection;
import bemypet.com.br.bemypet_v1.pojo.Usuario;
import bemypet.com.br.bemypet_v1.utils.Utils;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient googleApiClient;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    private Usuario usuarioLogado;

    private Boolean novoCadastro = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getBundle();

        if(getUsuarioLogado() == null) {
            usuarioLogado = new Usuario();
            setUsuarioLogado(usuarioLogado);
        }

        //LOGIN GOOGLE

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuthListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    setUserData(user);
                }
                else {
                    goLoginScreen();
                }
            }
        };

    }

    private void getBundle() {

        if (Utils.getUsuarioSharedPreferences(getApplicationContext()) != null) {
            setUsuarioLogado(Utils.getUsuarioSharedPreferences(getApplicationContext()));
        }

    }

    private void setUserData(FirebaseUser user) {

        getUsuarioLogado().email = user.getEmail();
        getUsuarioLogado().id = user.getUid();
        Log.d("PROV", "ID: " + user.getUid());

        for (UserInfo profile : user.getProviderData()) {
            Log.d("PROV", "Provider: " + profile.getProviderId());

            if (profile.getProviderId().equals("google.com")) {
                getUsuarioLogado().nome = user.getDisplayName();
                getUsuarioLogado().imagens.add(user.getPhotoUrl().toString());
            }
        }

        buscarUsuarios();


    }



    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(firebaseAuthListener);
    }

    private void goLoginScreen() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }



    public void iniciar(View v) {

        Intent intent = new Intent(MainActivity.this, InicialActivity.class);
        startActivity(intent);
        MainActivity.this.finish();

//        buscarUsuarios();


//        Intent intent = new Intent(this, EscolhaActivity.class);

        //Intent intent = new Intent(this, CadastroPetActivity.class);

        //salvarUsuario(Utils.instanciarUsuario("kassi mentz", getApplicationContext()));



    }



    private void salvarUsuario() {

        //LINHAS ADICIONADAS PARA SALVAR O TOKEN QUE SERA UTILIZADO PARA O PUSH NOTIFICATION
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("FirebaseInstanceId", "Refreshed token: " + refreshedToken);

        getUsuarioLogado().token = refreshedToken;

        System.out.println(getUsuarioLogado().toString());
        FirebaseConnection.getConnection();
        DatabaseReference connectedReference = FirebaseDatabase.getInstance().getReference(".info/connected");

        connectedReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {

                    FirebaseConnection.getDatabase().child("usuarios").child(String.valueOf(getUsuarioLogado().id)).setValue(getUsuarioLogado());
                    //salva dados na memério quando faz login
                    Utils.salvarUsuarioSharedPreferences(getApplicationContext(), getUsuarioLogado());


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
                    System.out.println("usuario "+usuario.id);
                    System.out.println(getUsuarioLogado().id);
                    //ff0c5d8e-558e-4c45-8a34-ef34dc8a3dc1
                    //b96b7698-aede-48df-9ffe-89704646768a
                    if(usuario.id.equalsIgnoreCase(getUsuarioLogado().id)) {

                        //TODO Esta verificacao terá de ser realizada no login do usuario.
                        if(usuario.denuncias != null && usuario.denuncias.size() > 10) {
                            AlertDialog.Builder dialogAprovado = new AlertDialog.Builder(getApplicationContext());
                            dialogAprovado.setTitle("Número máximo de denúncias atingido!");
                            dialogAprovado
                                    .setMessage("Você possui mais de 10 denúncias no aplicativo. Isso significa que você está impedido temporariamente de utilizar o aplicativo. Entre em contato com os administradores.")
                                    .setCancelable(false)
                                    .setPositiveButton("OK",new DialogInterface.OnClickListener() {

                                        public void onClick(DialogInterface dialog, int id) {
                                            MainActivity.this.finish();
                                        }
                                    });

                            AlertDialog alert = dialogAprovado.create();
                            alert.show();
                        } else {
                            Utils.salvarUsuarioSharedPreferences(getApplicationContext(), usuario);
                            novoCadastro = false;

                        }

                    }


                }

                if(novoCadastro){

                    //se ainda não existe salva dados no firebase
                    salvarUsuario();

                }else {

                    Intent intent = new Intent(MainActivity.this, InicialActivity.class);
                    startActivity(intent);
                    MainActivity.this.finish();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });



    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onStop() {
        super.onStop();
        if(firebaseAuthListener != null){
            firebaseAuth.removeAuthStateListener(firebaseAuthListener);
        }
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


    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public void setUsuarioLogado(Usuario usuarioLogado) {
        this.usuarioLogado = usuarioLogado;
    }
}
