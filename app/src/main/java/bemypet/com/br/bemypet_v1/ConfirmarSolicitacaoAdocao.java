package bemypet.com.br.bemypet_v1;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;

import bemypet.com.br.bemypet_v1.pojo.Adotante;
import bemypet.com.br.bemypet_v1.pojo.Pet;

public class ConfirmarSolicitacaoAdocao extends AppCompatActivity {

    private Pet pet;
    private Adotante adotante;
    private ImageView header_cover_image, user_profile_photo;
    private TextView user_profile_name, txtParagrafo3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmar_solicitacao_adocao);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.confirmarSolicitacaoAdocaoToolbar);
        setSupportActionBar(myToolbar);

        ActionBar ab = getSupportActionBar();
        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        ab.setTitle(R.string.activity_title_confirmar_solicitacao_adocao);

        initializeVariables();
        getBundle();
        if(getPet() != null) {
            preencherDados();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        String dataPet = new Gson().toJson(getPet());
        Intent intent = new Intent();
        intent.putExtra("dataPet", dataPet);
        setResult(RESULT_OK, intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                String dataPet = new Gson().toJson(getPet());
                Intent intent = new Intent();
                intent.putExtra("dataPet", dataPet);
                setResult(RESULT_OK, intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initializeVariables() {

        header_cover_image = (ImageView) findViewById(R.id.header_cover_image);
        user_profile_photo = (ImageView) findViewById(R.id.user_profile_photo);
        user_profile_name = (TextView) findViewById(R.id.user_profile_name);
        txtParagrafo3 = (TextView) findViewById(R.id.txtParagrafo3);
    }

    private void getBundle() {
        String jsonObj = null;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            jsonObj = extras.getString("pet");
        }
        Pet pet = new Gson().fromJson(jsonObj, Pet.class);

        if(pet != null) {
            setPet(pet);
            System.out.println(pet.toString());
        }
    }

    private void preencherDados() {

        if(getPet().imagens.size() > 0) {
            // Loading profile image

            Glide.with(this).load(getPet().imagens.get(0)).apply(RequestOptions.circleCropTransform()).into(user_profile_photo);
            Glide.with(this).load(getPet().imagens.get(getPet().imagens.size()-1)).into(header_cover_image);
        }


        if(!getPet().nome.isEmpty()) {
            user_profile_name.setText(getPet().nome);
            txtParagrafo3.setText("Nesse momento, "+getPet().nome+" já te ama muito!");
        }

        System.out.println(getPet().doador.nome);
    }

    public void cancelarConfirmacaoSolicitacaoAdocao(View v) {
        this.finish();
    }

    public void aplicarConfirmacaoSolicitacaoAdocao(View v) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

            alertDialogBuilder.setTitle("Solicitação enviada!");

            alertDialogBuilder
                    .setMessage("Uma mensagem foi enviada para o dono do pet. Aguarde um retorno para saber se tudo deu certo na adoção de "+getPet().nome+"!")
                    .setCancelable(false)
                    .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //TODO ENVIAR NOTIFICACAO
                            //TODO setar status do pet para "em adocao" para que ele nao apareça mais nas buscas

                            Intent intent = new Intent(getApplication(), InicialActivity.class);
                            startActivity(intent);
                            ConfirmarSolicitacaoAdocao.this.finish();
                        }
                    });

            AlertDialog alertDialog = alertDialogBuilder.create();

            alertDialog.show();
    }




    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public Adotante getAdotante() {
        return adotante;
    }

    public void setAdotante(Adotante adotante) {
        this.adotante = adotante;
    }

}
