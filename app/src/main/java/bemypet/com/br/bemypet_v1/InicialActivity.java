package bemypet.com.br.bemypet_v1;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;

import bemypet.com.br.bemypet_v1.fragment.ConfiguracoesFragment;
import bemypet.com.br.bemypet_v1.fragment.MeusPetsFavoritosFragment;
import bemypet.com.br.bemypet_v1.fragment.NotificacoesMensagensFragment;
import bemypet.com.br.bemypet_v1.fragment.TelaInicialFragment;
import bemypet.com.br.bemypet_v1.pojo.Filtros;
import bemypet.com.br.bemypet_v1.pojo.Usuario;
import bemypet.com.br.bemypet_v1.utils.Utils;

public class InicialActivity extends AppCompatActivity {

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private ImageView imgProfile;
    private TextView txtNomeUsuario, txtTipoUsuario;
    private Toolbar toolbar;
    private FloatingActionButton fab;

    // index to identify current nav menu item
    public static int navItemIndex = 0;

    // tags used to attach the fragments
    private static final String TAG_HOME = "home";
    private static final String TAG_MENSAGENS = "notificacoes";
    private static final String TAG_MEUS_PETS = "meuspets";
    private static final String TAG_CONFIGURACOES = "configuracoes";
    public static String CURRENT_TAG = TAG_HOME;

    // toolbar titles respected to selected nav menu item
    private String[] activityTitles;

    // flag to load home fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;

    private Filtros filtroActivity;
    private Usuario usuarioLogado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicial);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setOverflowIcon(getResources().getDrawable(R.drawable.ic_filter_list_white_24dp));

        mHandler = new Handler();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        navHeader = navigationView.getHeaderView(0);
        txtNomeUsuario = (TextView) navHeader.findViewById(R.id.nomeUsuario);
        txtTipoUsuario = (TextView) navHeader.findViewById(R.id.tipoUsuario);
        imgProfile = (ImageView) navHeader.findViewById(R.id.img_profile);

        LinearLayout header = (LinearLayout) navHeader.findViewById(R.id.headerProfile);
        updateUsuario();
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(InicialActivity.this, PerfilUsuarioActivity.class);
                drawer.closeDrawers();
                startActivity(intent);
            }
        });

        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);

//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });


        loadNavHeader();

        setUpNavigationView();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadHomeFragment();
        }
    }

    private void updateUsuario() {
        Usuario usuarioLogado = Utils.getUsuarioSharedPreferences(getApplicationContext());
        if(usuarioLogado != null) {
            System.out.println(usuarioLogado);
            setUsuarioLogado(usuarioLogado);
        } else {
            System.out.print("nao tem usuario no shared");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        String jsonObj = null;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            jsonObj = extras.getString("filtro");
        }
        Filtros filtro = new Gson().fromJson(jsonObj, Filtros.class);
        if(filtro!=null) {
            setFiltroActivity(filtro);
        }

        updateUsuario();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }

        if (shouldLoadHomeFragOnBackPress) {
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                loadHomeFragment();
                return;
            }
        }
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if (navItemIndex == 0) {
            getMenuInflater().inflate(R.menu.inicial, menu);
        }

        // when fragment is notifications, load the menu created for notifications
        if (navItemIndex == 3) {
            getMenuInflater().inflate(R.menu.mensagens, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_filtro) {
            Intent intent = new Intent(this, FiltrosActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_novo_pet) {
            Intent intent;
            if(!getUsuarioLogado().getLogradouro().isEmpty()) {
                intent = new Intent(getApplicationContext(), CadastroPetActivity.class);
            } else {
                Utils.showToastMessage(getApplicationContext(), "Para cadastrar um pet, primeiro complete seus dados: ");
                intent = new Intent(getApplicationContext(), CadastroUsuarioActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("origem", "cadastroPet");
                intent.putExtras(bundle);
            }
            startActivity(intent);
            return true;
        }

        // user is in notifications fragment
        // and selected 'Mark all as Read'
        if (id == R.id.action_mark_all_read) {
            Utils.showToastMessage(getApplicationContext(), "Todas notificações marcadas como lidas");
        }

        // user is in notifications fragment
        // and selected 'Clear All'
        if (id == R.id.action_clear_notifications) {
            Utils.showToastMessage(getApplicationContext(), "Limpar todas notificações");
        }

        return super.onOptionsItemSelected(item);
    }

    private void setUpNavigationView() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                switch (menuItem.getItemId()) {

                    case R.id.nav_home:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_HOME;
                        break;
                    case R.id.nav_mensagens:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_MENSAGENS;
                        break;
                    case R.id.nav_meus_pets:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_MEUS_PETS;
                        break;
//                    case R.id.nav_configuracoes:
//                        navItemIndex = 3;
//                        CURRENT_TAG = TAG_CONFIGURACOES;
//                        break;
                    case R.id.nav_ajuda:
                        startActivity(new Intent(InicialActivity.this, EscolhaActivity.class));
                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_sobre:
                        startActivity(new Intent(InicialActivity.this, SobreNosActivity.class));
                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_privacy_policy:
                        startActivity(new Intent(InicialActivity.this, PoliticaDePrivacidadeActivity.class));
                        drawer.closeDrawers();
                        return true;
                    default:
                        navItemIndex = 0;
                }

                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

                loadHomeFragment();

                return true;
            }
        });


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        drawer.setDrawerListener(actionBarDrawerToggle);

        actionBarDrawerToggle.syncState();
    }

    /***
     * Load navigation menu header information
     * like background image, profile image
     * name, website, notifications action view (dot)
     */
    private void loadNavHeader() {

        if(getUsuarioLogado() == null) {
            updateUsuario();
        }
        txtNomeUsuario.setText(getUsuarioLogado().nome);
        txtTipoUsuario.setText(getUsuarioLogado().email);

        // Loading profile image
        if(getUsuarioLogado().imagens != null && getUsuarioLogado().imagens.size() > 0) {
            Glide.with(this).load(getUsuarioLogado().imagens.get(0)).apply(RequestOptions.circleCropTransform()).into(imgProfile);
        }
        // showing dot next to notifications label
        //navigationView.getMenu().getItem(2).setActionView(R.layout.menu_dot);
    }

    private Fragment getHomeFragment() {

        switch (navItemIndex) {
            case 0:
                // home
                TelaInicialFragment telaInicialFragment = new TelaInicialFragment();
                return telaInicialFragment;
            case 1:
                NotificacoesMensagensFragment notificacoesMensagensFragment = new NotificacoesMensagensFragment();
                return notificacoesMensagensFragment;
            case 2:
                MeusPetsFavoritosFragment meusPetsFavoritosFragment = new MeusPetsFavoritosFragment();
                return meusPetsFavoritosFragment;
            case 3:
                ConfiguracoesFragment configuracoesFragment = new ConfiguracoesFragment();
                return configuracoesFragment;

            default:
                return new TelaInicialFragment();
        }
    }

    /***
     * Returns respected fragment that user
     * selected from navigation menu
     */
    private void loadHomeFragment() {

        selectNavMenu();

        setToolbarTitle();

        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();

            toggleFab();
            return;
        }

        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {

                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }

        toggleFab();

        drawer.closeDrawers();

        invalidateOptionsMenu();

    }

    private void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    private void toggleFab() {
        if (navItemIndex == 0)
            fab.show();
        else
            fab.hide();
    }

    public Filtros getFiltroActivity() {
        return filtroActivity;
    }

    public void setFiltroActivity(Filtros filtroActivity) {
        this.filtroActivity = filtroActivity;
    }

    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public void setUsuarioLogado(Usuario usuarioLogado) {
        this.usuarioLogado = usuarioLogado;
    }
}
