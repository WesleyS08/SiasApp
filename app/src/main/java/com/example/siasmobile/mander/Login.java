package com.example.siasmobile.mander;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.TextView;

import com.example.siasmobile.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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

        // Configura o ícone de visibilidade da senha
        setupInitialPasswordVisibility();

        // Configura o ícone de visibilidade da senha
        passwordInputLayout.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePasswordVisibility();
            }
        });

        // Botão para ir a tela de cadastro
        TextView cadastroTextView = findViewById(R.id.other_textview);
        cadastroTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navegar para a tela de Cadastro
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
