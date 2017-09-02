package bemypet.com.br.bemypet_v1;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
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
import bemypet.com.br.bemypet_v1.utils.Utils;

public class FiltrosActivity extends AppCompatActivity {

    private RadioGroup radioGroupEspecie, radioGroupSexo, radioGroupCastrado, radioGroupVermifugado;
    //private RadioButton radioEspecieButton, radioSexoButton, radioCastradoButton, radioVermifugadoButton;
    private Spinner spinnerRaca;
    private SeekBar seekBarRaioBusca;
    private RangeBar rangeIdade, rangePeso;
    private TextView txtSeekBarIdadeValue, txtSeekBarPesoValue, txtSeekBarRaioBuscaValue;
    private CheckBox chkEspecieCao, chkEspecieGato, chkEspecieOutros, chkSexoMacho,
            chkSexoFemea, chkCastradoSim, chkCastradoNao, chkVermifugadoSim,
            chkVermifugadoNao, chkSociavelPessoas, chkSociavelCaes, chkSociavelGatos,
            chkSociavelOutros, chkTemperamentoBravo, chkTemperamentoComCuidado,
            chkTemperamentoConviveBem, chkTemperamentoMuitoDocil;
    Integer raioDeBusca = 0;
    String idadeInicial = "", idadeFinal = "", pesoInicial = "", pesoFinal = "";
    Filtros filtros;

    List<String> racasFiltro = new ArrayList<>();
    List<String> racasCao = new ArrayList<>();
    List<String> racasGato = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtros);

        Toolbar myChildToolbar = (Toolbar) findViewById(R.id.filtrosToolbar);
        setSupportActionBar(myChildToolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);


        ArrayAdapter<String> racas_adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, racasFiltro);
        Spinner spinner = (Spinner) findViewById(R.id.spinnerRacas);
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
//                R.array.raca_array, R.layout.spinner_style);
        spinner.setAdapter(racas_adapter);


        initializeVariables();
        filtros = new Filtros();
        if(Utils.fileExists(this, "filtros.json")) {
            //lerFiltros();
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


        spinnerRaca = (Spinner) findViewById(R.id.spinnerRacas);

        chkEspecieCao = (CheckBox) findViewById(R.id.chk_especie_cao);
        chkEspecieCao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(chkEspecieCao.isChecked()) {
                    racasFiltro.addAll(racasCao);
                } else {
                    racasFiltro.removeAll(racasCao);
                }
            }
        });
        chkEspecieGato = (CheckBox) findViewById(R.id.chk_especie_gato);
        chkEspecieOutros = (CheckBox) findViewById(R.id.chk_especie_outros);
        chkSexoMacho = (CheckBox) findViewById(R.id.chk_sexo_macho);
        chkSexoFemea = (CheckBox) findViewById(R.id.chk_sexo_femea);
        chkCastradoSim = (CheckBox) findViewById(R.id.chk_castrado_sim);
        chkCastradoNao = (CheckBox) findViewById(R.id.chk_castrado_nao);
        chkVermifugadoSim = (CheckBox) findViewById(R.id.chk_vermifugado_sim);
        chkVermifugadoNao = (CheckBox) findViewById(R.id.chk_vermifugado_nao);

        rangeIdade = (RangeBar) findViewById(R.id.seekBarIdade);
        txtSeekBarIdadeValue = (TextView) findViewById(R.id.txtSeekBarIdadeValue);

        rangeIdade.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {
                idadeInicial =  leftPinValue;
                idadeFinal = rightPinValue;
                txtSeekBarIdadeValue.setText("Idade Aproximada: de "+ leftPinValue + " até "+ rightPinValue+" anos.");
            }
        });

        rangePeso = (RangeBar) findViewById(R.id.seekBarPeso);
        txtSeekBarPesoValue = (TextView) findViewById(R.id.txtSeekBarPesoValue);

        rangePeso.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex,int rightPinIndex, String leftPinValue, String rightPinValue) {
                pesoInicial = leftPinValue;
                pesoFinal = rightPinValue;
                txtSeekBarPesoValue.setText("Peso de "+ leftPinValue + " até "+ rightPinValue+" kg.");
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
//    private void lerFiltros() {
//        String data = Utils.readStringFromFile(this, "filtros.json");
//
//            Filtros filtrosSalvos = new Gson().fromJson(data, Filtros.class);
//
//            //se existem filtros salvos, preencher os campos conforme o que estava salvo
//            if (filtrosSalvos != null) {
//
//                //setando a especie
//                for (int i = 0; i < radioGroupEspecie.getChildCount(); i++) {
//                    RadioButton child = (RadioButton) radioGroupEspecie.getChildAt(i);
//                    if (child.getText().toString().equalsIgnoreCase(filtrosSalvos.especie)) {
//                        child.setChecked(true);
//
//                        if (child.getText().toString().equalsIgnoreCase("Cão")) {
//                            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),
//                                    R.array.raca_cao, R.layout.spinner_style);
//                            spinnerRaca.setAdapter(adapter);
//                        }
//
//                        if (child.getText().toString().equalsIgnoreCase("Gato")) {
//                            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),
//                                    R.array.raca_gato, R.layout.spinner_style);
//                            spinnerRaca.setAdapter(adapter);
//                        }
//
//                        if (child.getText().toString().equalsIgnoreCase("Outros")) {
//                            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),
//                                    R.array.raca_outro, R.layout.spinner_style);
//                            spinnerRaca.setAdapter(adapter);
//                        }
//
//                    }
//                }
//
//                //setando o sexo
//                for (int i = 0; i < radioGroupSexo.getChildCount(); i++) {
//                    RadioButton child = (RadioButton) radioGroupSexo.getChildAt(i);
//                    if (child.getText().toString().equalsIgnoreCase(filtrosSalvos.sexo)) {
//                        child.setChecked(Boolean.TRUE);
//                    }
//                }
//
//                //setando a raça
//                spinnerRaca.setSelection(Utils.getSpinnerIndex(spinnerRaca, filtrosSalvos.raca));
//
//                //setando a idade
//                rangeIdade.setRangePinsByIndices(Integer.valueOf(filtrosSalvos.idadeInicial), Integer.valueOf(filtrosSalvos.idadeFinal));
//
//                //setando o peso
//                rangePeso.setRangePinsByIndices(Integer.valueOf(filtrosSalvos.pesoInicial), Integer.valueOf(filtrosSalvos.pesoFinal));
//
//                //setando castrado
//                for (int i = 0; i < radioGroupCastrado.getChildCount(); i++) {
//                    RadioButton child = (RadioButton) radioGroupCastrado.getChildAt(i);
//                    if (child.getText().toString().equalsIgnoreCase(filtrosSalvos.castrado)) {
//                        child.setChecked(true);
//                    }
//                }
//
//                //setando vermifugado
//                for (int i = 0; i < radioGroupVermifugado.getChildCount(); i++) {
//                    RadioButton child = (RadioButton) radioGroupVermifugado.getChildAt(i);
//                    if (child.getText().toString().equalsIgnoreCase(filtrosSalvos.vermifugado)) {
//                        child.setChecked(true);
//                    }
//                }
//
//                //setando valores de sociavel
//                for (String sociavel : filtrosSalvos.sociavel) {
//                    if (sociavel.equalsIgnoreCase(chkSociavelPessoas.getText().toString()))
//                        chkSociavelPessoas.setChecked(Boolean.TRUE);
//
//                    if (sociavel.equalsIgnoreCase(chkSociavelCaes.getText().toString()))
//                        chkSociavelCaes.setChecked(Boolean.TRUE);
//
//                    if (sociavel.equalsIgnoreCase(chkSociavelGatos.getText().toString()))
//                        chkSociavelGatos.setChecked(Boolean.TRUE);
//
//                    if (sociavel.equalsIgnoreCase(chkSociavelOutros.getText().toString()))
//                        chkSociavelOutros.setChecked(Boolean.TRUE);
//                }
//
//                //setando valores de temperamento
//                for (String temperamento : filtrosSalvos.temperamento) {
//                    if (temperamento.equalsIgnoreCase(chkTemperamentoBravo.getText().toString()))
//                        chkTemperamentoBravo.setChecked(Boolean.TRUE);
//
//                    if (temperamento.equalsIgnoreCase(chkTemperamentoComCuidado.getText().toString()))
//                        chkTemperamentoComCuidado.setChecked(Boolean.TRUE);
//
//                    if (temperamento.equalsIgnoreCase(chkTemperamentoConviveBem.getText().toString()))
//                        chkTemperamentoConviveBem.setChecked(Boolean.TRUE);
//
//                    if (temperamento.equalsIgnoreCase(chkTemperamentoMuitoDocil.getText().toString()))
//                        chkTemperamentoMuitoDocil.setChecked(Boolean.TRUE);
//                }
//            }
//
//            //setando o raio de busca
//            if (filtrosSalvos.raioDeBusca != null) {
//                seekBarRaioBusca.setProgress(Integer.valueOf(filtrosSalvos.raioDeBusca));
//                txtSeekBarRaioBuscaValue.setText("Raio de Busca: " + String.valueOf(filtrosSalvos.raioDeBusca) + " km.");
//            }
//
//
//    }

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
        if(chkSexoMacho.isChecked()){
            filtros.sexo = chkSexoMacho.getText().toString();
        } else if(chkSexoFemea.isChecked()) {
            filtros.sexo = chkSexoFemea.getText().toString();
        } else {
            filtros.sexo = "TODOS";
        }

        //pegar o valor de raca selecionado
        filtros.raca = spinnerRaca.getSelectedItem().toString();

        //pegar o valor de idade selecionado
        filtros.idadeInicial = idadeInicial;
        filtros.idadeFinal = idadeFinal;

        //pegar o valor de peso selecionado
        filtros.pesoInicial = pesoInicial;
        filtros.pesoFinal = pesoFinal;

        //pegar o valor de castrado selecionado
        //se ambos ou nenhum estiverem marcado, salva como TODOS
        if(chkCastradoNao.isChecked()){
            filtros.castrado = chkCastradoNao.getText().toString();
        } else if(chkCastradoSim.isChecked()) {
            filtros.castrado = chkCastradoSim.getText().toString();
        } else {
            filtros.castrado = "TODOS";
        }


        //pegar o valor de vermifugado selecionado
        //se ambos ou nenhum estiverem marcado, salva como TODOS
        if(chkVermifugadoNao.isChecked()){
            filtros.vermifugado = chkVermifugadoNao.getText().toString();
        } else if(chkVermifugadoSim.isChecked()) {
            filtros.vermifugado = chkVermifugadoSim.getText().toString();
        } else {
            filtros.vermifugado = "TODOS";
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



}