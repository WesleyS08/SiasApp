package com.example.siasmobile.mander;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.example.siasmobile.R;
import com.google.android.gms.common.SignInButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class Login extends AppCompatActivity {

    private TextInputEditText passwordEditText;
    private TextInputLayout passwordInputLayout;
    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inicialização dos elementos do layout
        passwordEditText = findViewById(R.id.passwordEditText);
        passwordInputLayout = findViewById(R.id.passwordInputLayout);
        SignInButton signInButton = findViewById(R.id.signInButton);
        TextView cadastroTextView = findViewById(R.id.other_textview); // Cadastro TextView

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
