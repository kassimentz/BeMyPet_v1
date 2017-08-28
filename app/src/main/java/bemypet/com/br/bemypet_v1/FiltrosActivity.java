package bemypet.com.br.bemypet_v1;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.RadioButton;
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
    private RadioButton radioEspecieButton, radioSexoButton, radioCastradoButton, radioVermifugadoButton;
    private Spinner spinnerRaca;
    private SeekBar seekBarRaioBusca;
    private RangeBar rangeIdade, rangePeso;
    private TextView txtSeekBarIdadeValue, txtSeekBarPesoValue, txtSeekBarRaioBuscaValue;
    private CheckBox chkSociavelPessoas, chkSociavelCaes, chkSociavelGatos, chkSociavelOutros,
            chkTemperamentoBravo, chkTemperamentoComCuidado, chkTemperamentoConviveBem, chkTemperamentoMuitoDocil;
    Integer raioDeBusca = 0;
    String idadeInicial = "", idadeFinal = "", pesoInicial = "", pesoFinal = "";
    Filtros filtros;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtros);

        Toolbar myChildToolbar = (Toolbar) findViewById(R.id.filtrosToolbar);
        setSupportActionBar(myChildToolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        Spinner spinner = (Spinner) findViewById(R.id.spinnerRacas);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.raca_array, R.layout.spinner_style);
        spinner.setAdapter(adapter);


        initializeVariables();
        filtros = new Filtros();
        lerFiltros();
    }

    //inicializando os elementos do layout
    private void initializeVariables() {

        spinnerRaca = (Spinner) findViewById(R.id.spinnerRacas);

        radioGroupEspecie = (RadioGroup) findViewById(R.id.radioGroupEspecie);

        radioGroupEspecie.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {

                int selectedEspecie = radioGroupEspecie.getCheckedRadioButtonId();
                radioEspecieButton = (RadioButton) findViewById(selectedEspecie);
                if(radioEspecieButton.getText().toString().equalsIgnoreCase("Cão")) {
                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),
                            R.array.raca_cao, R.layout.spinner_style);
                    spinnerRaca.setAdapter(adapter);
                }

                if(radioEspecieButton.getText().toString().equalsIgnoreCase("Gato")) {
                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),
                            R.array.raca_gato, R.layout.spinner_style);
                    spinnerRaca.setAdapter(adapter);
                }

                if(radioEspecieButton.getText().toString().equalsIgnoreCase("Outros")) {
                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),
                            R.array.raca_outro, R.layout.spinner_style);
                    spinnerRaca.setAdapter(adapter);
                }
            }
        });
        radioGroupSexo = (RadioGroup) findViewById(R.id.radioGroupSexo);


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

        radioGroupCastrado = (RadioGroup) findViewById(R.id.radioGroupCastrado);
        radioGroupVermifugado = (RadioGroup) findViewById(R.id.radioGroupVermifugado);

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

    private void lerFiltros() {
        String data = Utils.readStringFromFile(this, "filtros.json");
        if(data != null) {
            Filtros filtrosSalvos = new Gson().fromJson(data, Filtros.class);

            //se existem filtros salvos, preencher os campos conforme o que estava salvo
            if (filtrosSalvos != null) {

                //setando a especie
                for (int i = 0; i < radioGroupEspecie.getChildCount(); i++) {
                    RadioButton child = (RadioButton) radioGroupEspecie.getChildAt(i);
                    if (child.getText().toString().equalsIgnoreCase(filtrosSalvos.especie)) {
                        child.setChecked(true);

                        if (child.getText().toString().equalsIgnoreCase("Cão")) {
                            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),
                                    R.array.raca_cao, R.layout.spinner_style);
                            spinnerRaca.setAdapter(adapter);
                        }

                        if (child.getText().toString().equalsIgnoreCase("Gato")) {
                            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),
                                    R.array.raca_gato, R.layout.spinner_style);
                            spinnerRaca.setAdapter(adapter);
                        }

                        if (child.getText().toString().equalsIgnoreCase("Outros")) {
                            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),
                                    R.array.raca_outro, R.layout.spinner_style);
                            spinnerRaca.setAdapter(adapter);
                        }

                    }
                }

                //setando o sexo
                for (int i = 0; i < radioGroupSexo.getChildCount(); i++) {
                    RadioButton child = (RadioButton) radioGroupSexo.getChildAt(i);
                    if (child.getText().toString().equalsIgnoreCase(filtrosSalvos.sexo)) {
                        child.setChecked(Boolean.TRUE);
                    }
                }

                //setando a raça
                spinnerRaca.setSelection(Utils.getSpinnerIndex(spinnerRaca, filtrosSalvos.raca));

                //setando a idade
                rangeIdade.setRangePinsByIndices(Integer.valueOf(filtrosSalvos.idadeInicial), Integer.valueOf(filtrosSalvos.idadeFinal));

                //setando o peso
                rangePeso.setRangePinsByIndices(Integer.valueOf(filtrosSalvos.pesoInicial), Integer.valueOf(filtrosSalvos.pesoFinal));

                //setando castrado
                for (int i = 0; i < radioGroupCastrado.getChildCount(); i++) {
                    RadioButton child = (RadioButton) radioGroupCastrado.getChildAt(i);
                    if (child.getText().toString().equalsIgnoreCase(filtrosSalvos.castrado)) {
                        child.setChecked(true);
                    }
                }

                //setando vermifugado
                for (int i = 0; i < radioGroupVermifugado.getChildCount(); i++) {
                    RadioButton child = (RadioButton) radioGroupVermifugado.getChildAt(i);
                    if (child.getText().toString().equalsIgnoreCase(filtrosSalvos.vermifugado)) {
                        child.setChecked(true);
                    }
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
            Utils.showToastMessage(this, "Filtro salvo com sucesso");
        } else {
            Utils.showToastMessage(this, "Erro ao salvar filtros");
        }
    }

    private void preencherFiltro() {
        //pegar o valor da especie selecionado
        int selectedEspecie = radioGroupEspecie.getCheckedRadioButtonId();
        radioEspecieButton = (RadioButton) findViewById(selectedEspecie);
        filtros.especie = radioEspecieButton.getText().toString();

        //pegar o valor do sexo selecionado
        int selectedSexo = radioGroupSexo.getCheckedRadioButtonId();
        radioSexoButton = (RadioButton) findViewById(selectedSexo);
        filtros.sexo = radioSexoButton.getText().toString();

        //pegar o valor de raca selecionado
        filtros.raca = spinnerRaca.getSelectedItem().toString();

        //pegar o valor de idade selecionado
        filtros.idadeInicial = idadeInicial;
        filtros.idadeFinal = idadeFinal;

        //pegar o valor de peso selecionado
        filtros.pesoInicial = pesoInicial;
        filtros.pesoFinal = pesoFinal;

        //pegar o valor de castrado selecionado
        int selectedCastrado = radioGroupCastrado.getCheckedRadioButtonId();
        radioCastradoButton = (RadioButton) findViewById(selectedCastrado);
        filtros.castrado = radioCastradoButton.getText().toString();


        //pegar o valor de vermifugado selecionado
        int selectedVermifugado = radioGroupVermifugado.getCheckedRadioButtonId();
        radioVermifugadoButton = (RadioButton) findViewById(selectedVermifugado);
        filtros.vermifugado = radioVermifugadoButton.getText().toString();

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