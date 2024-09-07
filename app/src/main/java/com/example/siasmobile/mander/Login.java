package com.example.siasmobile.mander;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.example.siasmobile.R;
import com.example.siasmobile.bancodedados.Supabase;
import com.google.android.gms.common.SignInButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private EditText  email, senha;
    private Button btnLogin;
    private FirebaseAuth mAuth;
    private Supabase supabaseService;
    private View tooltipView;
    private TextInputEditText passwordEditText;
    private TextInputLayout passwordInputLayout;
    private boolean isPasswordVisible = false;

    //Array de mensagens para serem exibidas.
    String[] mensagens = {"Preencha todos os Campos", "Login realizado com sucesso!",
            "As senhas não são iguais", "O email não foi verificado"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        supabaseService = new Supabase(this);

        // Inicialização dos elementos do layout
        passwordEditText = findViewById(R.id.passwordEditText);
        passwordInputLayout = findViewById(R.id.passwordInputLayout);
        SignInButton signInButton = findViewById(R.id.signInButton);
        TextView cadastroTextView = findViewById(R.id.other_textview); // Cadastro TextView
        TextInputLayout emailLayout = findViewById(R.id.input_email);
        TextView esqueceu_senha = findViewById(R.id.forgotPasswordTextView);


        email = emailLayout.getEditText();
        senha = passwordInputLayout.getEditText();
        btnLogin = findViewById(R.id.Login);


        esqueceu_senha.setOnClickListener(v ->{ //cria uma nova tela para esqueceu sua senha.
         // será necessário outra tela para isso?
          Intent intent = new Intent(Login.this, Esqueceu.class);
          startActivity(intent);
        });

        //exibição para o LogCat como depuração se esta tudo funcionando como planejado
        tooltipView = LayoutInflater.from(this).inflate(R.layout.tooltip_layout, null);
        Log.d("LoginActivity", "Layout do tooltip inflado");


        btnLogin.setOnClickListener(v -> {

            String Email = email.getText().toString().trim();
            String Senha = senha.getText().toString().trim();


            // Realizar o login com Firebase Authentication
            mAuth.signInWithEmailAndPassword(Email, Senha)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            // Login bem-sucedido
                            FirebaseUser user = mAuth.getCurrentUser();
                            Snackbar Newsnackbar = Snackbar.make(v, mensagens[1], Snackbar.LENGTH_LONG);
                            Newsnackbar.setBackgroundTint(Color.WHITE);
                            Newsnackbar.setTextColor(Color.BLACK);
                            Newsnackbar.show();

                            if (user != null) {
                                // Aqui você pode verificar se o usuário verificou o e-mail, se necessário
                                if (user.isEmailVerified()) {
                                    // Redireciona para a próxima tela, por exemplo, a tela principal do aplicativo
                                    //criado uma nova tela inicial do usuario, HomeUser
                                    Intent intent = new Intent(Login.this, HomeUser.class);
                                    startActivity(intent);
                                    finish(); // Fecha a activity de login para não permitir o retorno
                                } else {
                                    // E-mail não verificado
                                    Snackbar snackbar = Snackbar.make(v, mensagens[3], Snackbar.LENGTH_LONG);
                                    snackbar.setBackgroundTint(Color.WHITE);
                                    snackbar.setTextColor(Color.BLACK);
                                    snackbar.show();
                                }
                            }
                        } else {
                            // Login falhou
                            String erro;
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthInvalidUserException e) {
                                erro = "Nenhuma conta encontrada com esse e-mail!";
                                Log.d("LoginActivity", "Usuário não encontrado: " + e.getMessage());
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                erro = "Senha inválida!";
                                Log.d("LoginActivity", "Senha inválida: " + e.getMessage());
                            } catch (Exception e) {
                                erro = "Erro ao tentar fazer login!";
                                Log.e("LoginActivity", "Erro: " + e.getMessage());
                            }
                            Snackbar snackbar = Snackbar.make(v, mensagens[3], Snackbar.LENGTH_SHORT);
                            snackbar.setBackgroundTint(Color.WHITE);
                            snackbar.setTextColor(Color.BLACK);
                            snackbar.show();
                        }
                    });

            //FALTANDO BACKEND DO FIREBASE E SUPABASE NO CÓDIGO, POSSUI ERROS NO CADASTRO COM OS BANCOS

        });

        // Configura o esquema de cores do botão do Google
        signInButton.setColorScheme(SignInButton.COLOR_DARK);

        // Configura o ícone de visibilidade da senha
        setupInitialPasswordVisibility();
        passwordInputLayout.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePasswordVisibility();
            }
        });

        // Configura o clique para navegar para a página de Cadastro
        cadastroTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Cadastro.class);
                startActivity(intent);
            }
        });

    }

    private void setupInitialPasswordVisibility() {
        // Inicializa com a senha oculta e o ícone de olho fechado
        passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        passwordInputLayout.setEndIconDrawable(ContextCompat.getDrawable(this, R.drawable.ic_visibility_off));
    }

    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            // Esconder a senha
            passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            // Atualiza o ícone para olho fechado
            passwordInputLayout.setEndIconDrawable(ContextCompat.getDrawable(this, R.drawable.ic_visibility_off));
        } else {
            // Mostrar a senha
            passwordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            // Atualiza o ícone para olho aberto
            passwordInputLayout.setEndIconDrawable(ContextCompat.getDrawable(this, R.drawable.ic_visibility));
        }
        // Coloca o cursor no final do texto
        passwordEditText.setSelection(passwordEditText.getText().length());
        // Alterna o estado da visibilidade da senha
        isPasswordVisible = !isPasswordVisible;
    }
}
