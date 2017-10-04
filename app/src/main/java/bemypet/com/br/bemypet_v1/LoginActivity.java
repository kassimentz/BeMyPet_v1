package bemypet.com.br.bemypet_v1;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.lang.reflect.Array;
import java.util.Arrays;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient googleApiClient;
    private SignInButton btnLoginGoogle;
    private static final int SIGN_IN_CODE_GOOGLE = 777;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    private ProgressBar progress_bar_login;
    private LinearLayout form_login;

    private LinearLayout layouCadastroEmail, layouLoginCadastroEmail;

    private TextView loginEmailApp;
    private TextView criarLoginEmailApp;

    private static final String TAG = "EmailPassword";

    private String nomeNovoUsuarioEmail = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        setContentView(R.layout.activity_login);

        //LOGIN GOOGLE

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        btnLoginGoogle = (SignInButton) findViewById(R.id.btnLoginGoogle);
        btnLoginGoogle.setSize(btnLoginGoogle.SIZE_WIDE);
        btnLoginGoogle.setColorScheme(btnLoginGoogle.COLOR_DARK);

        btnLoginGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent, SIGN_IN_CODE_GOOGLE);
            }
        });


        // [START initialize_auth]
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuthListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    goMainScreen();
                }

            }
        };


        progress_bar_login = (ProgressBar) findViewById(R.id.progress_bar_login);
        form_login = (LinearLayout) findViewById(R.id.form_login);
        layouLoginCadastroEmail = (LinearLayout) findViewById(R.id.layouLoginCadastroEmail);
        layouCadastroEmail = (LinearLayout) findViewById(R.id.layouCadastroEmail);

        loginEmailApp = (TextView) findViewById(R.id.loginEmailApp);
        loginEmailApp.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

        criarLoginEmailApp = (TextView) findViewById(R.id.criarLoginEmailApp);
        criarLoginEmailApp.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(firebaseAuthListener);

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        //LOGIN GOOGLE
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //LOGIN GOOGLE
        if(requestCode == SIGN_IN_CODE_GOOGLE){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if(result.isSuccess()){
            firebaseAuthWithGoogle(result.getSignInAccount());
        }else{
            Toast.makeText(this, R.string.mensagem_erro_login, Toast.LENGTH_SHORT).show();
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount signInAccount) {

        progress_bar_login.setVisibility(View.VISIBLE);
        form_login.setVisibility(View.GONE);

        AuthCredential credencial = GoogleAuthProvider.getCredential(signInAccount.getIdToken(), null);
        firebaseAuth.signInWithCredential(credencial).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progress_bar_login.setVisibility(View.GONE);
                form_login.setVisibility(View.VISIBLE);

                if(!task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), R.string.mensagem_erro_login, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void goMainScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("nomeNovoUsuarioEmail", nomeNovoUsuarioEmail);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(firebaseAuthListener != null){
            firebaseAuth.removeAuthStateListener(firebaseAuthListener);
        }
    }

    public void mostraCamposCriaLogin(View v){
        showCriaLoginDialog();
    }

    private void showCriaLoginDialog(){

        final Dialog cadastro = new Dialog(this);

        cadastro.requestWindowFeature(Window.FEATURE_NO_TITLE);
        cadastro.setContentView(R.layout.create_account_login);
        final EditText nome = (EditText) cadastro.findViewById(R.id.edtNomeCadastro);
        final EditText email = (EditText) cadastro.findViewById(R.id.edtEmailCadastro);
        final EditText senha = (EditText) cadastro.findViewById(R.id.edtSenhaCadastro);

        cadastro.setTitle("Login em BeMyPet");

        Button btnCadastrar = (Button) cadastro.findViewById(R.id.btnCadastrar);
        Button btnCancel = (Button) cadastro.findViewById(R.id.btnCancel);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String compare = "gmail.";
                if(!email.getText().toString().toUpperCase().contains(compare.toUpperCase())) {
                    if (email.getText().toString().trim().length() > 0 && senha.getText().toString().trim().length() > 0 && nome.getText().toString().trim().length() > 0) {
                        // Validate Your login credential here than display message
                        createAccount(email.getText().toString().trim(), senha.getText().toString().trim(), nome.getText().toString().trim());

//                    Toast.makeText(LoginActivity.this, "Login Sucessfull, "+email.getText().toString(), Toast.LENGTH_LONG).show();

                        // Redirect to dashboard / home screen.
                        cadastro.dismiss();
                    } else {
                        Toast.makeText(LoginActivity.this, "Por favor, informe email e senha", Toast.LENGTH_LONG).show();

                    }
                } else {
                    AlertDialog.Builder dialogAprovado = new AlertDialog.Builder(LoginActivity.this);
                    dialogAprovado.setTitle("Login com Gmail");
                    dialogAprovado
                            .setMessage("Para realizar cadastro com gmail, você deve utilizar Fazer Login com Google")
                            .setCancelable(false)
                            .setPositiveButton("OK",new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int id) {
                                    cadastro.dismiss();

                                }
                            });

                    AlertDialog alert = dialogAprovado.create();
                    alert.show();
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cadastro.dismiss();
            }
        });

        cadastro.show();
    }


    public void mostraCamposEmail(View v) {
        showLoginDialog();
    }

    private void showLoginDialog() {

        final Dialog login = new Dialog(this);

        login.requestWindowFeature(Window.FEATURE_NO_TITLE);
        login.setContentView(R.layout.login_email);
        final EditText email = (EditText) login.findViewById(R.id.edtEmailLogin);
        final EditText senha = (EditText) login.findViewById(R.id.edtSenhaLogin);

        login.setTitle("Login em BeMyPet");

        Button btnLogin = (Button) login.findViewById(R.id.btnLogin);
        Button btnCancel = (Button) login.findViewById(R.id.btnCancel);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String compare = "gmail.";
                if(!email.getText().toString().toUpperCase().contains(compare.toUpperCase())) {
                    if (email.getText().toString().trim().length() > 0 && senha.getText().toString().trim().length() > 0) {
                        // Validate Your login credential here than display message
                        signIn(email.getText().toString().trim(), senha.getText().toString().trim());

//                    Toast.makeText(LoginActivity.this, "Login Sucessfull, "+email.getText().toString(), Toast.LENGTH_LONG).show();

                        // Redirect to dashboard / home screen.
                        login.dismiss();
                    } else {
                        Toast.makeText(LoginActivity.this, "Por favor, informe email e senha", Toast.LENGTH_LONG).show();

                    }
                } else {
                    AlertDialog.Builder dialogAprovado = new AlertDialog.Builder(LoginActivity.this);
                    dialogAprovado.setTitle("Login com Gmail");
                    dialogAprovado
                            .setMessage("Para realizar login com gmail, você deve utilizar Fazer Login com Google")
                            .setCancelable(false)
                            .setPositiveButton("OK",new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int id) {
                                    login.dismiss();

                                }
                            });

                    AlertDialog alert = dialogAprovado.create();
                    alert.show();
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login.dismiss();
            }
        });

        login.show();
    }


    private void signIn(final String email, final String password) {
        Log.d(TAG, "signIn:" + email);


        // [START sign_in_with_email]
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            goMainScreen();
                        } else {
                            Toast.makeText(LoginActivity.this, R.string.mensagem_erro_login, Toast.LENGTH_LONG).show();

                        }

                        // [END_EXCLUDE]
                    }
                });
        // [END sign_in_with_email]
    }


    private void createAccount(String email, String password, final String nome) {
        Log.d(TAG, "createAccount:" + email);
//        if (!validateForm()) {
//            return;
//        }

        progress_bar_login.setVisibility(View.VISIBLE);
        form_login.setVisibility(View.GONE);

        // [START create_user_with_email]
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();

                            nomeNovoUsuarioEmail = nome;
                            goMainScreen();
//                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), R.string.mensagem_erro_login, Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                        }

                        // [START_EXCLUDE]
                        progress_bar_login.setVisibility(View.GONE);
                        form_login.setVisibility(View.VISIBLE);
                        // [END_EXCLUDE]
                    }
                });
        // [END create_user_with_email]
    }
}
