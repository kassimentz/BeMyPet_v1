package bemypet.com.br.bemypet_v1;

import android.content.Intent;
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
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import bemypet.com.br.bemypet_v1.models.FirebaseConnection;
import bemypet.com.br.bemypet_v1.pojo.Filtros;
import bemypet.com.br.bemypet_v1.pojo.Pet;
import bemypet.com.br.bemypet_v1.pojo.PontoGeo;
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

    private void initializeVariables() {
        radioGroupEspecie = (RadioGroup) findViewById(R.id.radioGroupEspecie);
        radioGroupSexo = (RadioGroup) findViewById(R.id.radioGroupSexo);
        spinnerRaca = (Spinner) findViewById(R.id.spinnerRacas);

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
                pesoFinal = leftPinValue;
                txtSeekBarPesoValue.setText("Peso de "+ leftPinValue + " até "+ leftPinValue+" anos.");
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
        //TODO para realizar a leitura dos valores do arquivo de filtros... e setar esses valores como valores iniciais
//        String data = Utils.readStringFromFile(this, "filtros.json");
//        Filtros f = new Gson().fromJson(data, Filtros.class);
//        System.out.println(f.toString());
    }

    private void cancelarFiltro() {
        Utils.showToastMessage(this, "cancelar filtro");
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
            Utils.showToastMessage(this, "Json salvo com sucesso");
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
        if(radioCastradoButton.getText().toString().equalsIgnoreCase("Sim")) {
            filtros.castrado = Boolean.TRUE;
        } else {
            filtros.castrado = Boolean.FALSE;
        }

        //pegar o valor de vermifugado selecionado
        int selectedVermifugado = radioGroupVermifugado.getCheckedRadioButtonId();
        radioVermifugadoButton = (RadioButton) findViewById(selectedVermifugado);
        if(radioVermifugadoButton.getText().toString().equalsIgnoreCase("Sim")) {
            filtros.vermifugado = Boolean.TRUE;
        } else {
            filtros.vermifugado = Boolean.FALSE;
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

        System.out.println(sociavel.toString());
        System.out.println(temperamento.toString());

        //pegar o valor de raio de busca selecionado
        filtros.raioDeBusca = raioDeBusca;
    }


}