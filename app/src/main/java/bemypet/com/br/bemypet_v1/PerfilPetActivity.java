package bemypet.com.br.bemypet_v1;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.CallbackManager;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import bemypet.com.br.bemypet_v1.adapters.CustomGridMesmaNinhadaBaseAdapter;
import bemypet.com.br.bemypet_v1.pojo.Pet;
import bemypet.com.br.bemypet_v1.pojo.Usuario;
import bemypet.com.br.bemypet_v1.utils.Constants;
import bemypet.com.br.bemypet_v1.utils.Utils;


public class PerfilPetActivity extends AppCompatActivity {

    private Pet pet;

    //temporário até puxar do banco
    GridView grid;
    List<String> nomes = new ArrayList<>();
    List<String> images = new ArrayList<>();

    private TextView user_profile_name, especiePerfilPet, sexoPerfilPet, racaPerfilPet, idadePerfilPet,
            pesoPerfilPet, castradoPerfilPet, vermifugadoPerfilPet, sociavelPerfilPet, temperamentoPerfilPet, txtOutrasInformacoes;
    private ImageView header_cover_image, user_profile_photo, imgFavoritarPet;
    private Button buttonPerfil;

    private Usuario usuarioLogado;

    Boolean esconderBotaoAdotar = Boolean.FALSE;
    Boolean petFavoritado = Boolean.TRUE;

    CallbackManager callbackManager;
    ShareDialog shareDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_pet);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.visualizarPetToolbar);
        setSupportActionBar(myToolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle(R.string.activity_title_peril_pet);

        initializeVariables();
        getBundle();
        if(getPet() != null) {
            preencherDados();
        }

        //TODO ninhada esta comentada nessa versao
//        CustomGridMesmaNinhadaBaseAdapter adapter = new CustomGridMesmaNinhadaBaseAdapter (PerfilPetActivity.this, nomes, images);
//        grid=(GridView)findViewById(R.id.grid);
//        grid.setAdapter(adapter);
//        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//                Toast.makeText(PerfilPetActivity.this, "You Clicked at " +nomes.get(position), Toast.LENGTH_SHORT).show();
//
//            }
//        });

        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_share, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;

            case R.id.share_twiiter:
                shareTwitter();
                return true;

            case R.id.share_facebook:
                shareFacebook();
                return true;

            case R.id.share_whatsapp:
                shareWhatsapp();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == 2404) {
            if(data != null) {
                String jsonObj = data.getStringExtra("dataPet");
                Pet pet = new Gson().fromJson(jsonObj, Pet.class);
                setPet(pet);
                System.out.println(pet.nome);
                preencherDados();
            }
        }
    }

    private void initializeVariables() {
        header_cover_image = (ImageView) findViewById(R.id.header_cover_image);
        user_profile_photo = (ImageView) findViewById(R.id.user_profile_photo);
        user_profile_name = (TextView) findViewById(R.id.user_profile_name);
        txtOutrasInformacoes = (TextView) findViewById(R.id.txtOutrasInformacoes);
        imgFavoritarPet = (ImageView) findViewById(R.id.imgFavoritarPet);
        especiePerfilPet = (TextView) findViewById(R.id.especiePerfilPet);
        sexoPerfilPet = (TextView) findViewById(R.id.sexoPerfilPet);
        racaPerfilPet = (TextView) findViewById(R.id.racaPerfilPet);
        idadePerfilPet = (TextView) findViewById(R.id.idadePerfilPet);
        pesoPerfilPet = (TextView) findViewById(R.id.pesoPerfilPet);
        castradoPerfilPet = (TextView) findViewById(R.id.castradoPerfilPet);
        vermifugadoPerfilPet = (TextView) findViewById(R.id.vermifugadoPerfilPet);
        sociavelPerfilPet = (TextView) findViewById(R.id.sociavelPerfilPet);
        temperamentoPerfilPet = (TextView) findViewById(R.id.temperamentoPerfilPet);
        buttonPerfil = (Button) findViewById(R.id.ButtonPerfil);
    }

    private void getBundle() {

        String jsonObj = null;
        String key = "";
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            jsonObj = extras.getString("pet");
            if(extras.containsKey("key")) {
                key = extras.getString("key");
                if(!key.isEmpty()) {
                    setEsconderBotaoAdotar(Boolean.TRUE);
                }
            }
        }
        Pet pet = new Gson().fromJson(jsonObj, Pet.class);

        if(pet != null) {
            setPet(pet);
        }

        Usuario usuarioTmp = Utils.getUsuarioSharedPreferences(getApplicationContext());
        if(usuarioTmp != null) {
            if (!usuarioTmp.getLogradouro().isEmpty()) {
                setUsuarioLogado(usuarioTmp);
                if(getPet().atualDonoID.equalsIgnoreCase(getUsuarioLogado().id)) {
                    setEsconderBotaoAdotar(Boolean.TRUE);
                }
                for (Pet petTmp : getUsuarioLogado().petsFavoritos) {
                    if(petTmp.id.equalsIgnoreCase(getPet().id)) {
                        imgFavoritarPet.setImageResource(R.drawable.fav1);
                        petFavoritado = Boolean.TRUE;
                        break;
                    } else {
                        imgFavoritarPet.setImageResource(R.drawable.fav2_v);
                        petFavoritado = Boolean.FALSE;
                    }
                }

            } else {
                Utils.showToastMessage(getApplicationContext(), "Para favoritar um pet, primeiro complete seus dados: ");
                Intent intent = new Intent(getApplicationContext(), CadastroUsuarioActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("origem", "favoritarPet");
                bundle.putSerializable("pet", new Gson().toJson(getPet()));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }

    }

    private void preencherDados() {

        if(getEsconderBotaoAdotar()) {
            buttonPerfil.setVisibility(View.INVISIBLE);
        } else {
            buttonPerfil.setVisibility(View.VISIBLE);
        }

        if(getPet().imagens.size() > 0) {
            Glide.with(this).load(getPet().imagens.get(0)).apply(RequestOptions.circleCropTransform()).into(user_profile_photo);
            Glide.with(this).load(getPet().imagens.get(getPet().imagens.size()-1)).into(header_cover_image);
        }


        if(getPet().nome != null) {
            user_profile_name.setText(getPet().nome);
        }
        if(getPet().especie != null) {
            especiePerfilPet.setText(getPet().especie);
        }
        if(getPet().sexo != null) {
            sexoPerfilPet.setText(getPet().sexo);
        }
        if(getPet().raca != null) {
            racaPerfilPet.setText(getPet().raca);
        }
        if(getPet().idadeAproximada != null) {
            idadePerfilPet.setText(getPet().idadeAproximada);
        }
        if(getPet().pesoAproximado != null) {
            pesoPerfilPet.setText(String.valueOf(getPet().pesoAproximado));
        }
        if(getPet().castrado != null) {
            castradoPerfilPet.setText(getPet().castrado);
        }
        if(getPet().vermifugado != null) {
            vermifugadoPerfilPet.setText(getPet().vermifugado);
        }
        if(getPet().sociavel != null) {
            StringBuilder stringSociavel = new StringBuilder();
            for (String sociavel : getPet().sociavel) {
                stringSociavel.append(sociavel);
                stringSociavel.append(", ");
            }

            stringSociavel.deleteCharAt(stringSociavel.length()-2);
            sociavelPerfilPet.setText(stringSociavel.toString());

        }
        if(getPet().temperamento != null) {
            StringBuilder stringTemperamento = new StringBuilder();
            for (String temperamento : getPet().temperamento) {
                stringTemperamento.append(temperamento);
                stringTemperamento.append(", ");
            }

            stringTemperamento.deleteCharAt(stringTemperamento.length()-2);
            temperamentoPerfilPet.setText(stringTemperamento.toString());
        }

        if(getPet().informacoesAdicionais != null) {
            txtOutrasInformacoes.setText(getPet().informacoesAdicionais);
        }
    }

    public void editarPerfilPet(View v) {
        Intent intent = new Intent(this, CadastroPetActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("origem", "editarPet");
        bundle.putSerializable("pet", new Gson().toJson(getPet()));
        intent.putExtras(bundle);
        startActivityForResult(intent, 1);
        this.finish();
    }

    public void confirmarSolicitacaoAdocao(View v) {

        Intent intent = new Intent(this, CadastroUsuarioActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("pet", new Gson().toJson(getPet()));
        intent.putExtras(bundle);
        startActivityForResult(intent, 1);
    }

    public void favoritarPet(View v) {
        if(petFavoritado == Boolean.FALSE) {
            getUsuarioLogado().addFavorito(getPet());
            imgFavoritarPet.setImageResource(R.drawable.fav1);
            petFavoritado = Boolean.TRUE;
        } else {
            getUsuarioLogado().removerFavorito(getPet());
            imgFavoritarPet.setImageResource(R.drawable.fav2_v);
        }
        updateUsuario();
    }

    private void updateUsuario() {
        final DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("usuarios");
        myRef.child(getUsuarioLogado().id).child("petsFavoritos").setValue(getUsuarioLogado().petsFavoritos);

        //atualizando o usuario logado com os novos dados
        Utils.salvarUsuarioSharedPreferences(getApplicationContext(), getUsuarioLogado());
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public Boolean getEsconderBotaoAdotar() {
        return esconderBotaoAdotar;
    }

    public void setEsconderBotaoAdotar(Boolean esconderBotaoAdotar) {
        this.esconderBotaoAdotar = esconderBotaoAdotar;
    }

    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public void setUsuarioLogado(Usuario usuarioLogado) {
        this.usuarioLogado = usuarioLogado;
    }

    private void shareWhatsapp() {

        Intent shareIntent = new Intent();
        shareIntent = new Intent();
        shareIntent.setPackage(Constants.WHATSAPP);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Be My Pet - Encontre seu novo amigo!");
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, getPet().nome+ " está a procura de um novo lar! Baixe o BeMyPet e ajude-nos a encontrar um lar para nosso amiguinhos! https://play.google.com/store/apps/details?id=" +getPackageName());
        shareIntent.putExtra(Intent.EXTRA_STREAM, getPet().imagens.get(0));
        shareIntent.setType("image/*");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        boolean installed = checkAppInstall(Constants.WHATSAPP);
        if (installed) {
            startActivity(shareIntent);
        } else {
            Utils.showToastMessage(getApplicationContext(),  "O aplicativo necessita ser instalado antes.");
        }
    }

    private void shareFacebook() {

        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentTitle(getPet().nome + " está a procura de um novo lar!")
                    .setImageUrl(Uri.parse(getPet().imagens.get(0)))
                    .setContentDescription("Ajude "+getPet().nome+" a encontrar um lar!")
                    .setContentUrl(Uri.parse(getPet().imagens.get(0)))
                    //.setContentUrl(Uri.parse("https://play.google.com/store/apps/details?id=" +getPackageName()))
                    .build();

            shareDialog.show(linkContent);  // Show facebook ShareDialog
        }
    }

    public void shareTwitter() {

        try {
            Bitmap bmImg = Ion.with(getApplicationContext()).load(pet.imagens.get(0)).asBitmap().get();
            String title = "Be My Pet - Encontre seu novo amigo!"; //Title you wants to share

            String pathofBmp = MediaStore.Images.Media.insertImage(getContentResolver(), bmImg,"BeMyPet", null);
            Uri bmpUri = Uri.parse(pathofBmp);

            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, title);
            shareIntent.setType("*/*");
            shareIntent.setPackage(Constants.TWITTER);
            shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
            shareIntent.putExtra(Intent.EXTRA_TEXT, getPet().nome+ " está a procura de um novo lar! Baixe o BeMyPet e ajude-nos a encontrar um lar para nosso amiguinhos! https://play.google.com/store/apps/details?id=" +getPackageName());


            boolean installed = checkAppInstall(Constants.TWITTER);
            if (installed) {
                startActivity(shareIntent);
            } else {
                Utils.showToastMessage(getApplicationContext(),  "O aplicativo necessita ser instalado antes.");
            }


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    private boolean checkAppInstall(String uri) {
        PackageManager pm = getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
        }

        return false;
    }

}
