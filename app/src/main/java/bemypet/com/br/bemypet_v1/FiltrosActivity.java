package bemypet.com.br.bemypet_v1;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

import com.appyvet.rangebar.RangeBar;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import bemypet.com.br.bemypet_v1.pojo.Filtros;
import bemypet.com.br.bemypet_v1.utils.MultiSpinner;
import bemypet.com.br.bemypet_v1.utils.Utils;

public class FiltrosActivity extends AppCompatActivity {

    private MultiSpinner spinnerRaca;
    private SeekBar seekBarRaioBusca;
    private TextView txtSeekBarPesoValue, txtSeekBarRaioBuscaValue;
    private CheckBox chkEspecieCao, chkEspecieGato, chkEspecieOutros, chkSexoMacho,
            chkSexoFemea, chkCastradoSim, chkCastradoNao, chkVermifugadoSim,
            chkVermifugadoNao, chkSociavelPessoas, chkSociavelCaes, chkSociavelGatos,
            chkSociavelOutros, chkTemperamentoBravo, chkTemperamentoComCuidado,
            chkTemperamentoConviveBem, chkTemperamentoMuitoDocil;
    Integer raioDeBusca = 0;
    String idadeInicial = "", idadeFinal = "", pesoInicial = "", pesoFinal = "";
    Filtros filtros;

    private TextView edtIdadeMinima, edtIdadeMaxima, edtPesoMinimo, edtPesoMaximo;
    private ImageView showIdadeMinimaDialog, showIdadeMaximaDialog, showPesoMinimoDialog, showPesoMaximoDialog;
    static Dialog d ;

    List<String> racasFiltro = new ArrayList<>();
    List<String> racasCao = new ArrayList<>();
    List<String> racasGato = new ArrayList<>();
    List<String> racasOutros = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtros);

        Toolbar myChildToolbar = (Toolbar) findViewById(R.id.filtrosToolbar);
        setSupportActionBar(myChildToolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        initializeVariables();
        filtros = new Filtros();
        if(Utils.fileExists(this, "filtros.json")) {
            lerFiltros();
        }
    }

    //inicializando os elementos do layout
    private void initializeVariables() {

        racasFiltro.add("Qualquer");
        racasFiltro.add("SRD (Sem Raça Definida)");

        racasCao.add("Shih Tzu");
        racasCao.add("Yorkshire Terrier");
        racasCao.add("Poodle");
        racasCao.add("Lhasa Apso");
        racasCao.add("Buldogue Francês");
        racasCao.add("Maltês");
        racasCao.add("Golden Retriever");
        racasCao.add("Labrador");
        racasCao.add("Pug");
        racasCao.add("Dachshund");
        racasCao.add("Spitz Alemão");
        racasCao.add("Pinscher");
        racasCao.add("Schnauzer");
        racasCao.add("Beagle");
        racasCao.add("Cocker Spaniel");
        racasCao.add("Border Collie");
        racasCao.add("Buldogue Inglês");
        racasCao.add("American Pitbull");
        racasCao.add("Chow Chow");

        racasGato.add("Persa");
        racasGato.add("Himalaia");
        racasGato.add("Siamês");
        racasGato.add("Maine Coon");
        racasGato.add("Angorá");
        racasGato.add("Sphynx");
        racasGato.add("Ragdoll");
        racasGato.add("Ashera");
        racasGato.add("American Shorthair");
        racasGato.add("Siberiano");
        racasGato.add("Egípcio");

        racasOutros.add("Outros");

        spinnerRaca = (MultiSpinner) findViewById(R.id.spinnerRacas);
        spinnerRaca.setItems(racasFiltro);

        chkEspecieCao = (CheckBox) findViewById(R.id.chk_especie_cao);
        chkEspecieCao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(chkEspecieCao.isChecked()) {
                    racasFiltro.addAll(racasCao);
                    spinnerRaca.setItems(racasFiltro);
                } else {
                    racasFiltro.removeAll(racasCao);
                    spinnerRaca.setItems(racasFiltro);

                }
            }
        });

        chkEspecieGato = (CheckBox) findViewById(R.id.chk_especie_gato);
        chkEspecieGato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(chkEspecieGato.isChecked()) {
                    racasFiltro.addAll(racasGato);
                    spinnerRaca.setItems(racasFiltro);
                } else {
                    racasFiltro.removeAll(racasGato);
                    spinnerRaca.setItems(racasFiltro);

                }
            }
        });

        chkEspecieOutros = (CheckBox) findViewById(R.id.chk_especie_outros);
        chkEspecieOutros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(chkEspecieOutros.isChecked()) {
                    racasFiltro.addAll(racasOutros);
                    spinnerRaca.setItems(racasFiltro);
                } else {
                    racasFiltro.removeAll(racasOutros);
                    spinnerRaca.setItems(racasFiltro);

                }
            }
        });

        chkSexoMacho = (CheckBox) findViewById(R.id.chk_sexo_macho);
        chkSexoFemea = (CheckBox) findViewById(R.id.chk_sexo_femea);
        chkCastradoSim = (CheckBox) findViewById(R.id.chk_castrado_sim);
        chkCastradoNao = (CheckBox) findViewById(R.id.chk_castrado_nao);
        chkVermifugadoSim = (CheckBox) findViewById(R.id.chk_vermifugado_sim);
        chkVermifugadoNao = (CheckBox) findViewById(R.id.chk_vermifugado_nao);


        edtIdadeMinima = (TextView) findViewById(R.id.edtIdadeMinima);
        edtIdadeMaxima = (TextView) findViewById(R.id.edtIdadeMaxima);

        edtPesoMinimo = (TextView) findViewById(R.id.edtPesoMinimo);
        edtPesoMaximo = (TextView) findViewById(R.id.edtPesoMaximo);

        showIdadeMinimaDialog = (ImageView) findViewById(R.id.showIdadeMinimaDialog);
        showIdadeMinimaDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogIdadeMinima();
            }
        });

        showIdadeMaximaDialog = (ImageView) findViewById(R.id.showIdadeMaximaDialog);
        showIdadeMaximaDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogIdadeMaxima();
            }
        });


        showPesoMinimoDialog = (ImageView) findViewById(R.id.showPesoMinimoDialog);
        showPesoMinimoDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogPesoMinimo();
            }
        });

        showPesoMaximoDialog = (ImageView) findViewById(R.id.showPesoMaximoDialog);
        showPesoMaximoDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogPesoMaximo();
            }
        });

        seekBarRaioBusca = (SeekBar) findViewById(R.id.seekBarRaioBusca);
        txtSeekBarRaioBuscaValue = (TextView) findViewById(R.id.txtSeekBarRaioBuscaValue);

        seekBarRaioBusca.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                raioDeBusca = progresValue;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                txtSeekBarRaioBuscaValue.setText("Raio de Busca: "+String.valueOf(raioDeBusca)+ " km.");
            }
        });

        chkSociavelPessoas = (CheckBox) findViewById(R.id.chk_sociavel_pessoas);
        chkSociavelCaes = (CheckBox) findViewById(R.id.chk_sociavel_caes);
        chkSociavelGatos = (CheckBox) findViewById(R.id.chk_sociavel_gatos);
        chkSociavelOutros = (CheckBox) findViewById(R.id.chk_sociavel_outros);

        chkTemperamentoBravo = (CheckBox) findViewById(R.id.chk_temperamento_bravo);
        chkTemperamentoComCuidado = (CheckBox) findViewById(R.id.chk_temperamento_cuidado);
        chkTemperamentoConviveBem = (CheckBox) findViewById(R.id.chk_temperamento_convive);
        chkTemperamentoMuitoDocil = (CheckBox) findViewById(R.id.chk_temperamento_docil);
    }

    private void showDialogIdadeMinima() {

        final NumberPicker picker = new NumberPicker(getApplicationContext());
        picker.setMinValue(1);
        picker.setMaxValue(100);

        final FrameLayout layout = new FrameLayout(getApplicationContext());
        layout.addView(picker, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER));
        layout.setBackgroundResource(R.drawable.backgroun_button_orange);

        new AlertDialog.Builder(FiltrosActivity.this)
                .setView(layout)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(picker.getValue() < Integer.valueOf(getEdtIdadeMaxima())) {
                            setEdtIdadeMinima(String.valueOf(picker.getValue()));
                        } else {
                            Utils.showToastMessage(getApplicationContext(), "A idade mínima deve ser menor que a máxima");
                        }

                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    private void showDialogIdadeMaxima() {

        final NumberPicker picker = new NumberPicker(getApplicationContext());
        picker.setMinValue(1);
        picker.setMaxValue(100);

        final FrameLayout layout = new FrameLayout(getApplicationContext());
        layout.addView(picker, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER));
        layout.setBackgroundResource(R.drawable.backgroun_button_orange);

        new AlertDialog.Builder(FiltrosActivity.this)
                .setView(layout)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // do something with picker.getValue()
                        if(picker.getValue() > Integer.valueOf(getEdtIdadeMinima())) {
                            setEdtIdadeMaxima(String.valueOf(picker.getValue()));
                        } else {
                            Utils.showToastMessage(getApplicationContext(), "A idade máxima deve ser maior que a mínima");
                        }


                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    private void showDialogPesoMaximo() {

        final NumberPicker picker = new NumberPicker(getApplicationContext());
        picker.setMinValue(1);
        picker.setMaxValue(100);

        final FrameLayout layout = new FrameLayout(getApplicationContext());
        layout.addView(picker, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER));
        layout.setBackgroundResource(R.drawable.backgroun_button_orange);

        new AlertDialog.Builder(FiltrosActivity.this)
                .setView(layout)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if(picker.getValue() > Integer.valueOf(getEdtPesoMinimo())) {
                            setEdtPesoMaximo(String.valueOf(picker.getValue()));
                        } else {
                            Utils.showToastMessage(getApplicationContext(), "O peso máximo deve ser maior que o mínimo");
                        }
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    private void showDialogPesoMinimo() {

        final NumberPicker picker = new NumberPicker(getApplicationContext());
        picker.setMinValue(1);
        picker.setMaxValue(100);

        final FrameLayout layout = new FrameLayout(getApplicationContext());
        layout.addView(picker, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER));
        layout.setBackgroundResource(R.drawable.backgroun_button_orange);

        new AlertDialog.Builder(FiltrosActivity.this)
                .setView(layout)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // do something with picker.getValue()
                        if(picker.getValue() < Integer.valueOf(getEdtPesoMaximo())) {
                            setEdtPesoMinimo(String.valueOf(picker.getValue()));
                        } else {
                            Utils.showToastMessage(getApplicationContext(), "O peso mínimo deve ser menor que o máximo");
                        }

                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_filtros, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_cancel:
                cancelarFiltro();
                return true;

            case R.id.action_do_filter:
                aplicarFiltro();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    //TODO LEITURA DE FILTROS NOVAMENTE
    private void lerFiltros() {
        String data = Utils.readStringFromFile(this, "filtros.json");

            Filtros filtrosSalvos = new Gson().fromJson(data, Filtros.class);

            //se existem filtros salvos, preencher os campos conforme o que estava salvo
            if (filtrosSalvos != null) {

                //setando a especie
                for (String especie : filtrosSalvos.especies) {
                    if(especie.equalsIgnoreCase(chkEspecieCao.getText().toString())) {
                        chkEspecieCao.setChecked(Boolean.TRUE);
                        racasFiltro.addAll(racasCao);
                        spinnerRaca.setItems(racasFiltro);
                    }

                    if(especie.equalsIgnoreCase(chkEspecieGato.getText().toString())) {
                        chkEspecieGato.setChecked(Boolean.TRUE);
                        racasFiltro.addAll(racasGato);
                        spinnerRaca.setItems(racasFiltro);
                    }

                    if(especie.equalsIgnoreCase(chkEspecieOutros.getText().toString())) {
                        chkEspecieOutros.setChecked(Boolean.TRUE);
                        racasFiltro.addAll(racasOutros);
                        spinnerRaca.setItems(racasFiltro);
                    }
                }

                if(filtrosSalvos.sexo.equalsIgnoreCase("TODOS")) {
                    chkSexoFemea.setChecked(Boolean.TRUE);
                    chkSexoMacho.setChecked(Boolean.TRUE);
                } else if(filtrosSalvos.sexo.equalsIgnoreCase("NENHUM")) {
                    chkSexoFemea.setChecked(Boolean.FALSE);
                    chkSexoMacho.setChecked(Boolean.FALSE);
                } else if(filtrosSalvos.sexo.equalsIgnoreCase(chkSexoFemea.getText().toString())) {
                    chkSexoMacho.setChecked(Boolean.FALSE);
                    chkSexoFemea.setChecked(Boolean.TRUE);
                } else {
                    chkSexoMacho.setChecked(Boolean.TRUE);
                    chkSexoFemea.setChecked(Boolean.FALSE);
                }


                //setando a raça
                spinnerRaca.setSelection(filtrosSalvos.especies);

                //setando a idade
                if(filtrosSalvos.idadeFinal != null && filtrosSalvos.idadeFinal.length() > 0
                        && filtrosSalvos.idadeInicial != null && filtrosSalvos.idadeInicial.length() > 0) {
                    setEdtIdadeMinima(filtrosSalvos.idadeInicial);
                    setEdtIdadeMaxima(filtrosSalvos.idadeFinal);
                }
                //setando o peso
                if(filtrosSalvos.pesoInicial != null && filtrosSalvos.pesoInicial.length() > 0
                        && filtrosSalvos.pesoFinal != null && filtrosSalvos.pesoFinal.length() > 0) {
                    setEdtPesoMinimo(filtrosSalvos.pesoInicial);
                    setEdtPesoMaximo(filtrosSalvos.pesoFinal);
                }

                //setando castrado
                if(filtrosSalvos.castrado.equalsIgnoreCase("Sim")) {
                    chkCastradoSim.setChecked(Boolean.TRUE);
                    chkCastradoNao.setChecked(Boolean.FALSE);
                } else if(filtrosSalvos.castrado.equalsIgnoreCase("Não")){
                    chkCastradoSim.setChecked(Boolean.FALSE);
                    chkCastradoNao.setChecked(Boolean.TRUE);
                } else if(filtrosSalvos.castrado.equalsIgnoreCase("TODOS")) {
                    chkCastradoSim.setChecked(Boolean.TRUE);
                    chkCastradoNao.setChecked(Boolean.TRUE);
                } else {
                    chkCastradoSim.setChecked(Boolean.FALSE);
                    chkCastradoNao.setChecked(Boolean.FALSE);
                }

                //setando vermifugado

                if(filtrosSalvos.vermifugado.equalsIgnoreCase("Sim")) {
                    chkVermifugadoSim.setChecked(Boolean.TRUE);
                    chkVermifugadoNao.setChecked(Boolean.FALSE);
                } else if(filtrosSalvos.vermifugado.equalsIgnoreCase("Não")) {
                    chkVermifugadoSim.setChecked(Boolean.FALSE);
                    chkVermifugadoNao.setChecked(Boolean.TRUE);
                } else if(filtrosSalvos.vermifugado.equalsIgnoreCase("TODOS")) {
                    chkVermifugadoNao.setChecked(Boolean.TRUE);
                    chkVermifugadoSim.setChecked(Boolean.TRUE);
                } else {
                    chkVermifugadoSim.setChecked(Boolean.FALSE);
                    chkVermifugadoNao.setChecked(Boolean.FALSE);
                }


                //setando valores de sociavel
                for (String sociavel : filtrosSalvos.sociavel) {
                    if (sociavel.equalsIgnoreCase(chkSociavelPessoas.getText().toString()))
                        chkSociavelPessoas.setChecked(Boolean.TRUE);

                    if (sociavel.equalsIgnoreCase(chkSociavelCaes.getText().toString()))
                        chkSociavelCaes.setChecked(Boolean.TRUE);

                    if (sociavel.equalsIgnoreCase(chkSociavelGatos.getText().toString()))
                        chkSociavelGatos.setChecked(Boolean.TRUE);

                    if (sociavel.equalsIgnoreCase(chkSociavelOutros.getText().toString()))
                        chkSociavelOutros.setChecked(Boolean.TRUE);
                }

                //setando valores de temperamento
                for (String temperamento : filtrosSalvos.temperamento) {
                    if (temperamento.equalsIgnoreCase(chkTemperamentoBravo.getText().toString()))
                        chkTemperamentoBravo.setChecked(Boolean.TRUE);

                    if (temperamento.equalsIgnoreCase(chkTemperamentoComCuidado.getText().toString()))
                        chkTemperamentoComCuidado.setChecked(Boolean.TRUE);

                    if (temperamento.equalsIgnoreCase(chkTemperamentoConviveBem.getText().toString()))
                        chkTemperamentoConviveBem.setChecked(Boolean.TRUE);

                    if (temperamento.equalsIgnoreCase(chkTemperamentoMuitoDocil.getText().toString()))
                        chkTemperamentoMuitoDocil.setChecked(Boolean.TRUE);
                }
            }

            //setando o raio de busca
            if (filtrosSalvos.raioDeBusca != null) {
                seekBarRaioBusca.setProgress(Integer.valueOf(filtrosSalvos.raioDeBusca));
                txtSeekBarRaioBuscaValue.setText("Raio de Busca: " + String.valueOf(filtrosSalvos.raioDeBusca) + " km.");
            }


    }

    private void cancelarFiltro() {
        this.finish();
    }

    private void aplicarFiltro() {
        preencherFiltro();
        salvarFiltroJson(filtros);

        Intent intent = new Intent(this, InicialActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("filtro", new Gson().toJson(filtros));
        intent.putExtras(bundle);
        startActivity(intent);
        this.finish();

    }

    private void salvarFiltroJson(Filtros filtros) {
        Gson gson = new Gson();
        String json = gson.toJson(filtros);
        if(Utils.saveJsonFile(this, "filtros.json", json)) {
            System.out.println("json filtros");
            System.out.println(json);
            Utils.showToastMessage(this, "Filtro salvo com sucesso");
        } else {
            Utils.showToastMessage(this, "Erro ao salvar filtros");
        }
    }

    private void preencherFiltro() {
        //pegar o valor da especie selecionado

        List<String> especies = new ArrayList<>();
        if(chkEspecieCao.isChecked()) {
            especies.add(chkEspecieCao.getText().toString());
        }
        if(chkEspecieGato.isChecked()) {
            especies.add(chkEspecieGato.getText().toString());
        }
        if(chkEspecieOutros.isChecked()) {
            especies.add(chkEspecieOutros.getText().toString());
        }

        filtros.especies = especies;

        //pegar o valor do sexo selecionado
        //se ambos ou nenhum estiverem marcado, salva como TODOS
        if((chkSexoFemea.isChecked() && chkSexoMacho.isChecked())){
            filtros.sexo = "TODOS";
        } else if(!chkSexoFemea.isChecked() && !chkSexoMacho.isChecked()) {
            filtros.sexo = "NENHUM";
        } else if(chkSexoMacho.isChecked()){
            filtros.sexo = chkSexoMacho.getText().toString();
        } else if(chkSexoFemea.isChecked()) {
            filtros.sexo = chkSexoFemea.getText().toString();
        }

        //pegar o valor de raca selecionado
        filtros.raca = spinnerRaca.getSelectedStrings();

        //pegar o valor de idade selecionado
        filtros.idadeInicial = getEdtIdadeMinima();
        filtros.idadeFinal = getEdtIdadeMaxima();

        //pegar o valor de peso selecionado
        filtros.pesoInicial = getEdtPesoMinimo();
        filtros.pesoFinal = getEdtPesoMaximo();

        //pegar o valor de castrado selecionado
        //se ambos ou nenhum estiverem marcado, salva como TODOS
        if((chkCastradoSim.isChecked() && chkCastradoNao.isChecked()) ){
            filtros.castrado = "TODOS";
        } else if (!chkCastradoSim.isChecked() && !chkCastradoNao.isChecked()) {
            filtros.castrado = "NENHUM";
        }else if(chkCastradoNao.isChecked()){
            filtros.castrado = chkCastradoNao.getText().toString();
        } else if(chkCastradoSim.isChecked()) {
            filtros.castrado = chkCastradoSim.getText().toString();
        }


        //pegar o valor de vermifugado selecionado
        //se ambos ou nenhum estiverem marcado, salva como TODOS
        if((chkVermifugadoSim.isChecked() && chkVermifugadoNao.isChecked()) ){
            filtros.vermifugado = "TODOS";
        } else if(!chkVermifugadoSim.isChecked() && !chkVermifugadoNao.isChecked()) {
            filtros.vermifugado = "NENHUM";
        } else if(chkVermifugadoNao.isChecked()){
            filtros.vermifugado = chkVermifugadoNao.getText().toString();
        } else if(chkVermifugadoSim.isChecked()) {
            filtros.vermifugado = chkVermifugadoSim.getText().toString();
        }

        //pegar os valores de sociavel selecionados
        List<String> sociavel = new ArrayList<>();
        if(chkSociavelPessoas.isChecked()){
            sociavel.add(chkSociavelPessoas.getText().toString());
        }

        if(chkSociavelCaes.isChecked()){
            sociavel.add(chkSociavelCaes.getText().toString());
        }

        if(chkSociavelGatos.isChecked()){
            sociavel.add(chkSociavelGatos.getText().toString());
        }

        if(chkSociavelOutros.isChecked()){
            sociavel.add(chkSociavelOutros.getText().toString());
        }

        filtros.sociavel = sociavel;

        //pegar os valores de temperamento selecionados
        List<String> temperamento = new ArrayList<>();
        if(chkTemperamentoBravo.isChecked()) {
            temperamento.add(chkTemperamentoBravo.getText().toString());
        }

        if(chkTemperamentoComCuidado.isChecked()) {
            temperamento.add(chkTemperamentoComCuidado.getText().toString());
        }

        if(chkTemperamentoConviveBem.isChecked()) {
            temperamento.add(chkTemperamentoConviveBem.getText().toString());
        }

        if(chkTemperamentoMuitoDocil.isChecked()) {
            temperamento.add(chkTemperamentoMuitoDocil.getText().toString());
        }

        filtros.temperamento = temperamento;

        //pegar o valor de raio de busca selecionado
        filtros.raioDeBusca = raioDeBusca;
    }

    public String getEdtIdadeMinima() {
        return edtIdadeMinima.getText().toString();
    }

    public void setEdtIdadeMinima(String text) {
        this.edtIdadeMinima.setText(text);
    }

    public String getEdtIdadeMaxima() {
        return edtIdadeMaxima.getText().toString();
    }

    public void setEdtIdadeMaxima(String text) {
        this.edtIdadeMaxima.setText(text);
    }

    public String getEdtPesoMinimo() {
        return edtPesoMinimo.getText().toString();
    }

    public void setEdtPesoMinimo(String text) {
        this.edtPesoMinimo.setText(text);
    }

    public String getEdtPesoMaximo() {
        return edtPesoMaximo.getText().toString();
    }

    public void setEdtPesoMaximo(String text) {
        this.edtPesoMaximo.setText(text);
    }
}