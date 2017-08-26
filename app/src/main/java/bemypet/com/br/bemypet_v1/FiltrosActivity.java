package bemypet.com.br.bemypet_v1;

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

import com.bumptech.glide.util.Util;

import bemypet.com.br.bemypet_v1.pojo.Filtros;
import bemypet.com.br.bemypet_v1.utils.Utils;

public class FiltrosActivity extends AppCompatActivity {

    private RadioGroup radioGroupEspecie, radioGroupSexo, radioGroupCastrado, radioGroupVermifugado;
    private RadioButton radioEspecieButton, radioSexoButton, radioCastradoButton, radioVermifugadoButton;
    private Spinner spinnerRaca;
    private SeekBar seekBarIdade, seekBarPeso, seekBarRaioBusca;
    private TextView txtSeekBarIdadeValue, txtSeekBarPesoValue, txtSeekBarRaioBuscaValue;
    int idade = 0, peso = 0, raioDeBusca = 0;


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



    }

    private void initializeVariables() {
        radioGroupEspecie = (RadioGroup) findViewById(R.id.radioGroupEspecie);
        radioGroupSexo = (RadioGroup) findViewById(R.id.radioGroupSexo);
        spinnerRaca = (Spinner) findViewById(R.id.spinnerRacas);

        seekBarIdade = (SeekBar) findViewById(R.id.seekBarIdade);
        txtSeekBarIdadeValue = (TextView) findViewById(R.id.txtSeekBarIdadeValue);
        seekBarIdade.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                idade = progresValue;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                txtSeekBarIdadeValue.setText("Idade Aproximada: "+String.valueOf(idade)+ " anos.");
            }
        });

        seekBarPeso = (SeekBar) findViewById(R.id.seekBarPeso);
        txtSeekBarPesoValue = (TextView) findViewById(R.id.txtSeekBarPesoValue);

        seekBarPeso.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                peso = progresValue;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                txtSeekBarPesoValue.setText("Peso Aproximado: "+String.valueOf(peso)+ " kg.");
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

    private void cancelarFiltro() {
        Utils.showToastMessage(this, "cancelar filtro");
    }

    private void aplicarFiltro() {


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
        filtros.idade = String.valueOf(idade);

        //pegar o valor de peso selecionado
        filtros.peso = String.valueOf(peso);

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


        //pegar os valores de temperamento selecionados


        //pegar o valor de raio de busca selecionado
        filtros.raioDeBusca = raioDeBusca;
    }


}
