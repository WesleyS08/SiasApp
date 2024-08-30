package com.example.siasmobile.mander;

import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import com.example.siasmobile.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {

    // Definição dos elementos do layout
    private TextInputEditText passwordEditText;
    private TextInputLayout passwordInputLayout;
    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Oculta a barra de navegação assim que a atividade é criada
        hideNavigationBar();

        // Inicialização dos elementos do layout
        passwordEditText = findViewById(R.id.passwordEditText);
        passwordInputLayout = findViewById(R.id.passwordInputLayout);

        // Configura o ícone de visibilidade da senha
        passwordInputLayout.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePasswordVisibility();
            }
        });
    }

    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            // Esconder a senha
            passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            passwordInputLayout.setEndIconDrawable(getDrawable(R.drawable.ic_visibility_off)); // ícone de senha oculta
        } else {
            // Mostrar a senha
            passwordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            passwordInputLayout.setEndIconDrawable(getDrawable(R.drawable.ic_visibility)); // ícone de senha visível
        }
        // Coloca o cursor no final do texto
        passwordEditText.setSelection(passwordEditText.getText().length());
        isPasswordVisible = !isPasswordVisible;
    }

    private void hideNavigationBar() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideNavigationBar();
        }
    }
}
