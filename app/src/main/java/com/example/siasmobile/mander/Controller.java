package com.example.siasmobile.mander;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.siasmobile.R;

public class Controller extends AppCompatActivity {

    // Definição dos elementos do layout
    private ViewGroup transitionContainer;
    private View loginView;
    private View cadastroView;
    private TextView comeceTextView;
    private TextView subtituloTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controller);

        // Definição das variaveis
        transitionContainer = findViewById(R.id.transition_container);
        comeceTextView = findViewById(R.id.comece);
        subtituloTextView = findViewById(R.id.subtitulo);

        // Inflar as views para login e cadastro
        loginView = getLayoutInflater().inflate(R.layout.activity_login, transitionContainer, false);
        cadastroView = getLayoutInflater().inflate(R.layout.activity_cadastro, transitionContainer, false);

        // Exibir a view de login ao iniciar
        showView(loginView, getString(R.string.Titulo), getString(R.string.Descricao));

        // Listener para o botão de login
        findViewById(R.id.other_textview1).setOnClickListener(v -> {
            showView(loginView, getString(R.string.Titulo), getString(R.string.Descricao));
        });

        // Listener para o botão de cadastro
        findViewById(R.id.other_textview).setOnClickListener(v -> {
            showView(cadastroView, getString(R.string.TituloCadastro), getString(R.string.DescricaoCadastro));
        });

        // Listener para voltar ao login a partir do cadastro
        cadastroView.findViewById(R.id.other_textview1).setOnClickListener(v -> {
            showView(loginView, getString(R.string.Titulo), getString(R.string.Descricao));
        });
    }

    private void showView(View newView, String title, String subtitle) {
        // Animação para a view atual
        Animation slideOut = AnimationUtils.loadAnimation(this, R.anim.slide_out_left);
        // Animação para a nova view
        Animation slideIn = AnimationUtils.loadAnimation(this, R.anim.slide_in_right);

        // Adicionar animação à view atual
        transitionContainer.startAnimation(slideOut);

        // Remover todas as views atuais e adicionar a nova view
        transitionContainer.removeAllViews();
        transitionContainer.addView(newView);

        // Aplicar a animação na nova view
        newView.startAnimation(slideIn);

        // Atualizar o título e o subtítulo
        comeceTextView.setText(title);
        subtituloTextView.setText(subtitle);
    }
}
