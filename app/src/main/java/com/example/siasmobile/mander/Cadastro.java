package com.example.siasmobile.mander;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.siasmobile.Notificacao.NotificationUtils;
import com.example.siasmobile.R;
import com.example.siasmobile.bancodedados.Supabase;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class Cadastro extends AppCompatActivity {

    private EditText edit_nome, edit_email, editSenha, confirmSenha;
    private Button btnCadastrar;
    private FirebaseAuth mAuth;
    private View tooltipView;
    private TextView termosTextView;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;
    private Supabase supabaseService;

    // Mensagens de erros usadas em parte dos códigos
    String[] mensagens = { "Preencha todos os Campos!!", "Cadastro realizado com sucesso!", "Falha ao criar a conta",
            "As senhas devem ser iguais", "CPF Inválido", "CNPJ Inválido" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        mAuth = FirebaseAuth.getInstance();
        supabaseService = new Supabase(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        findViewById(R.id.cadastro_google).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInWithGoogle();
            }
        });

        // Variáveis do código
        TextInputLayout nomeLayout = findViewById(R.id.input_nome);
        TextInputLayout emailLayout = findViewById(R.id.input_email);
        TextInputLayout senhaLayout = findViewById(R.id.input_senha);
        TextInputLayout confirmSenhaLayout = findViewById(R.id.input_confirm_senha);
        TextView loginTextView = findViewById(R.id.other_textview1);
        TextInputLayout identificadorLayout = findViewById(R.id.identificadorInputLayout);

        edit_nome = nomeLayout.getEditText();
        edit_email = emailLayout.getEditText();
        editSenha = senhaLayout.getEditText();
        confirmSenha = confirmSenhaLayout.getEditText();
        btnCadastrar = findViewById(R.id.Entrar);
        termosTextView = findViewById(R.id.termos);

        // Eventos de click do código
        termosTextView.setOnClickListener(v -> showTermsDialog());

        loginTextView.setOnClickListener(v -> {
            Intent intent = new Intent(Cadastro.this, Login.class);
            startActivity(intent);
        });

        // Infla o layout do tooltip
        tooltipView = LayoutInflater.from(this).inflate(R.layout.tooltip_layout, null);
        Log.d("CadastroActivity", "Layout do tooltip inflado");

        btnCadastrar.setOnClickListener(v -> {
            // As variáveis capturadas dentro da lambda são final ou efetivamente final
            String nome = edit_nome.getText().toString().trim();
            String email = edit_email.getText().toString().trim();
            String senha = editSenha.getText().toString().trim();
            String confSenha = confirmSenha.getText().toString().trim();
            String identificadorTexto = identificadorLayout.getEditText().getText().toString().trim();

            if (nome.isEmpty() || email.isEmpty() || identificadorTexto.isEmpty() || senha.isEmpty()) {
                Snackbar snackbar = Snackbar.make(v, mensagens[0], Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(Color.WHITE);
                snackbar.setTextColor(Color.BLACK);
                snackbar.show();
                return;
            }

            // Verifica a quantidade de caracteres do identificadorTexto
            String tipoIdentificador = null;
            int length = identificadorTexto.length();
            if (length == 11) {
                // Código para identificador com 11 caracteres (CPF)
                Log.d("CadastroActivity", "Identificador tem 11 caracteres");

                if (isValidCPF(identificadorTexto)) {
                    Log.d("CadastroActivity", "CPF válido");
                    tipoIdentificador = "CPF";
                } else {
                    Log.d("CadastroActivity", "CPF inválido");
                    Snackbar snackbar = Snackbar.make(v, mensagens[4], Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                    return;
                }

            } else if (length == 14) {
                // Código para identificador com 14 caracteres (CNPJ)
                Log.d("CadastroActivity", "Identificador tem 14 caracteres");

                if (isValidCNPJ(identificadorTexto)) {
                    Log.d("CadastroActivity", "CNPJ válido");
                    tipoIdentificador = "CNPJ";
                } else {
                    Log.d("CadastroActivity", "CNPJ inválido");
                    Snackbar snackbar = Snackbar.make(v, mensagens[5], Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                    return;
                }
            } else {
                Log.d("CadastroActivity", "Comprimento do identificador inválido");
                Snackbar snackbar = Snackbar.make(v, "Identificador inválido", Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(Color.WHITE);
                snackbar.setTextColor(Color.BLACK);
                snackbar.show();
                return;
            }

            if (senha.equals(confSenha)) {
                mAuth.createUserWithEmailAndPassword(email, senha)
                        .addOnCompleteListener(this, task -> {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                if (user != null) {
                                    sendEmailVerification(user);

                                    String jsonBody;
                                    if (length == 11) {
                                        jsonBody = "{ \"nome\": \"" + nome + "\", \"email\": \"" + email + "\", \"identificador\": \"" + identificadorTexto + "\", \"tipo_identificador\": \"CPF\" }";
                                    } else if (length == 14) {
                                        jsonBody = "{ \"nome\": \"" + nome + "\", \"email\": \"" + email + "\", \"identificador\": \"" + identificadorTexto + "\", \"tipo_identificador\": \"CNPJ\" }";
                                    } else {
                                        Log.e("CadastroActivity", "Identificador deve ter 11 (CPF) ou 14 (CNPJ) caracteres.");
                                        return;
                                    }

                                    supabaseService.insertUserData("usuarios", jsonBody, new Callback() {
                                        @Override
                                        public void onFailure(Call call, IOException e) {
                                            Log.e("CadastroActivity", "Erro ao inserir dados no Supabase: " + e.getMessage());
                                        }

                                        @Override
                                        public void onResponse(Call call, Response response) throws IOException {
                                            if (response.isSuccessful()) {
                                                Log.d("CadastroActivity", "Dados inseridos com sucesso: " + response.body().string());
                                            } else {
                                                Log.e("CadastroActivity", "Erro ao inserir dados: " + response.code() + " - " + response.body().string());
                                            }
                                        }
                                    });

                                    NotificationUtils notificationUtils = new NotificationUtils();
                                    notificationUtils.sendNotification(this, "Cadastro concluído", "Você foi registrado com sucesso!");
                                    Intent intent = new Intent(Cadastro.this, Login.class);
                                    startActivity(intent);
                                    finish();
                                }
                            } else {
                                String erro;
                                try {
                                    throw task.getException();
                                } catch (FirebaseAuthWeakPasswordException e) {
                                    erro = "A senha deve ter no mínimo 6 caracteres!";
                                    Log.d("CadastroActivity", "Senha fraca: " + e.getMessage());
                                } catch (FirebaseAuthUserCollisionException e) {
                                    erro = "Esse e-mail já está cadastrado!";
                                    Log.d("CadastroActivity", "Colisão de usuário: " + e.getMessage());
                                } catch (FirebaseAuthInvalidCredentialsException e) {
                                    erro = "E-mail inválido!";
                                    Log.d("CadastroActivity", "E-mail inválido: " + e.getMessage());
                                } catch (Exception e) {
                                    erro = "Erro ao cadastrar usuário!";
                                    Log.e("CadastroActivity", "Erro: " + e.getMessage());
                                }
                                Snackbar snackbar = Snackbar.make(v, erro, Snackbar.LENGTH_SHORT);
                                snackbar.setBackgroundTint(Color.WHITE);
                                snackbar.setTextColor(Color.BLACK);
                                snackbar.show();
                            }
                        });
            } else {
                Snackbar snackbar = Snackbar.make(v, mensagens[3], Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(Color.WHITE);
                snackbar.setTextColor(Color.BLACK);
                snackbar.show();
            }
        });
    }

    private void showTermsDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Termos de Uso")
                .setMessage("Aqui você pode adicionar os termos de uso...")
                .setPositiveButton("OK", null)
                .show();
    }

    private void sendEmailVerification(FirebaseUser user) {
        user.sendEmailVerification()
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d("CadastroActivity", "Email de verificação enviado.");
                    } else {
                        Log.e("CadastroActivity", "Falha ao enviar email de verificação: " + task.getException());
                    }
                });
    }

    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Log.w("CadastroActivity", "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            String nome = user.getDisplayName();
                            String email = user.getEmail();
                            String uid = user.getUid();

                            String jsonBody = "{ \"nome\": \"" + nome + "\", \"email\": \"" + email + "\", \"identificador\": \"" + uid + "\", \"tipo_identificador\": \"Google\" }";

                            supabaseService.insertUserData("usuarios", jsonBody, new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    Log.e("CadastroActivity", "Erro ao inserir dados no Supabase: " + e.getMessage());
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    if (response.isSuccessful()) {
                                        Log.d("CadastroActivity", "Dados inseridos com sucesso: " + response.body().string());
                                    } else {
                                        Log.e("CadastroActivity", "Erro ao inserir dados: " + response.code() + " - " + response.body().string());
                                    }
                                }
                            });

                            NotificationUtils notificationUtils = new NotificationUtils();
                            notificationUtils.sendNotification(this, "Cadastro concluído", "Você foi registrado com sucesso!");
                            Intent intent = new Intent(Cadastro.this, Login.class);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        Log.w("CadastroActivity", "signInWithCredential:failure", task.getException());
                        Toast.makeText(Cadastro.this, "Authentication Failed.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private boolean isValidCPF(String cpf) {
        cpf = cpf.replaceAll("\\D", "");

        if (cpf.length() != 11 || cpf.equals("00000000000")) {
            return false;
        }

        int sum = 0;
        int weight = 10;

        for (int i = 0; i < 9; i++) {
            sum += (cpf.charAt(i) - '0') * weight--;
        }

        int firstDigit = 11 - (sum % 11);
        if (firstDigit > 9) firstDigit = 0;

        sum = 0;
        weight = 11;

        for (int i = 0; i < 10; i++) {
            sum += (cpf.charAt(i) - '0') * weight--;
        }

        int secondDigit = 11 - (sum % 11);
        if (secondDigit > 9) secondDigit = 0;

        return cpf.charAt(9) == firstDigit + '0' && cpf.charAt(10) == secondDigit + '0';
    }


    private boolean isValidCNPJ(String cnpj) {
        cnpj = cnpj.replaceAll("\\D", ""); // Remove caracteres não numéricos

        if (cnpj.length() != 14 || cnpj.equals("00000000000000")) {
            return false;
        }

        int[] weights1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        int[] weights2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

        int sum = 0;
        for (int i = 0; i < 12; i++) {
            sum += (cnpj.charAt(i) - '0') * weights1[i];
        }
        int digit1 = (sum % 11) < 2 ? 0 : 11 - (sum % 11);

        sum = 0;
        for (int i = 0; i < 13; i++) {
            sum += (cnpj.charAt(i) - '0') * weights2[i];
        }
        int digit2 = (sum % 11) < 2 ? 0 : 11 - (sum % 11);

        return cnpj.charAt(12) == digit1 + '0' && cnpj.charAt(13) == digit2 + '0';
    }

}