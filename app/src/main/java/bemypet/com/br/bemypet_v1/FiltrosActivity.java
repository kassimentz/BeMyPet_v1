package bemypet.com.br.bemypet_v1;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import bemypet.com.br.bemypet_v1.utils.Utils;

public class FiltrosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtros);

        Toolbar myChildToolbar = (Toolbar) findViewById(R.id.filtrosToolbar);
        setSupportActionBar(myChildToolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
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
                Utils.showToastMessage(this, "cancelar filtro");
                return true;

            case R.id.action_do_filter:
                Utils.showToastMessage(this, "realizar filtro");
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
