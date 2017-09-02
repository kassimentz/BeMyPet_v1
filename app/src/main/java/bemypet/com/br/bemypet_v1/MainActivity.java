package bemypet.com.br.bemypet_v1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import bemypet.com.br.bemypet_v1.models.FirebaseConnection;
import bemypet.com.br.bemypet_v1.pojo.Adotante;
import bemypet.com.br.bemypet_v1.pojo.Denuncias;
import bemypet.com.br.bemypet_v1.pojo.Doador;
import bemypet.com.br.bemypet_v1.pojo.Notificacoes;
import bemypet.com.br.bemypet_v1.pojo.Pet;
import bemypet.com.br.bemypet_v1.pojo.PontoGeo;
import bemypet.com.br.bemypet_v1.pojo.Usuario;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void iniciar(View v) {

//        Intent intent = new Intent(this, EscolhaActivity.class);
//        Intent intent = new Intent(this, PerfilUsuarioActivity.class);
        Intent intent = new Intent(this, InicialActivity.class);
        startActivity(intent);

//        System.out.println("inserindo doador no firebase");
//        inserirDoadorTeste();
//        System.out.println("inserindo adotante no firebase");
//        inserirAdotanteTeste();
//
//        System.out.println("inserindos adotante e doador no firebase");

    }

    private void inserirDoadorTeste() {
        Doador doador = new Doador();

        doador.nome = "nome doador";
        List<String> imagens = new ArrayList<>();
        imagens.add("https://firebasestorage.googleapis.com/v0/b/bemypet-61485.appspot.com/o/images%2Fimages%20(2).jpg?alt=media&token=8b0bb91b-f11c-4d59-a9a6-9a972732e284");
        doador.imagens = imagens;
        doador.dataNascimento = "28/11/1986";
        doador.cpf = "001.239.752.23";
        doador.localizacao = new PontoGeo(-29.856, -51.234);
        doador.cep = 91120415;
        doador.endereco = "gabriel franco da luz";
        doador.numero = 560;
        doador.complemento = "apto 206F";
        doador.bairro = "sarandi";
        doador.cidade = "porto alegre";
        doador.estado = "RS";
        doador.telefone = "434343434";
        doador.email = "cassio@teste.com";
        doador.meusPets = new ArrayList<Pet>();
        doador.petsFavoritos = new ArrayList<Pet>();
        doador.denuncias = new ArrayList<Denuncias>();
        doador.notificacoes = new ArrayList<Notificacoes>();

        salvar(doador);
    }

    private void inserirAdotanteTeste() {
        Adotante adotante = new Adotante();
        adotante.nome = "nome adotante";
        List<String> imagens = new ArrayList<>();
        imagens.add("https://firebasestorage.googleapis.com/v0/b/bemypet-61485.appspot.com/o/images%2F1472955573864images.jpg?alt=media&token=e5853030-e4aa-4c90-826a-b11a36594eda");
        adotante.imagens = imagens;
        adotante.dataNascimento = "08/01/1987";
        adotante.cpf = "011.109.750.23";
        adotante.localizacao = new PontoGeo(-29.856, -51.234);
        adotante.cep = 90820030;
        adotante.endereco = "diomario moojen";
        adotante.numero = 150;
        adotante.complemento = "apto 101";
        adotante.bairro = "cristal";
        adotante.cidade = "porto alegre";
        adotante.estado = "RS";
        adotante.telefone = "434343434";
        adotante.email = "kassi@teste.com";
        adotante.meusPets = new ArrayList<Pet>();
        adotante.petsFavoritos = new ArrayList<Pet>();
        adotante.denuncias = new ArrayList<Denuncias>();
        adotante.notificacoes = new ArrayList<Notificacoes>();
        adotante.jaTeveOutrosPets = Boolean.TRUE;
        adotante.quantosPetsTeve = 2;
        List<String> tiposPetsTeve = new ArrayList<>();
        tiposPetsTeve.add("gato");
        adotante.tiposPetsTeve = tiposPetsTeve;
        adotante.oQueAconteceuComEles = "continuam comigo";
        adotante.temPetAtualmente = Boolean.TRUE;
        adotante.quantosPetsTem = 2;
        adotante.tiposPetsTem = tiposPetsTeve;
        adotante.tipoMoradia = "apto";
        adotante.possuiPatio = Boolean.FALSE;
        adotante.temCuidadoContraPestes = Boolean.TRUE;
        adotante.possuiTelasProtecao = Boolean.TRUE;
        adotante.informacoesAdicionais = "bla bla bla";

        salvar(adotante);
    }

    private void salvar(Usuario entidade) {
        final Usuario usuario = entidade;

        FirebaseConnection.getConnection();
        DatabaseReference connectedReference = FirebaseDatabase.getInstance().getReference(".info/connected");


        connectedReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {
                    FirebaseConnection.getDatabase().child("usuarios").child(String.valueOf(usuario.id)).setValue(usuario);

                    GeoFire geoFire = new GeoFire(FirebaseConnection.getDatabase().child("geofire"));
                    geoFire.setLocation(usuario.id, new GeoLocation(usuario.localizacao.lat, usuario.localizacao.lon), new GeoFire.CompletionListener() {
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

}
