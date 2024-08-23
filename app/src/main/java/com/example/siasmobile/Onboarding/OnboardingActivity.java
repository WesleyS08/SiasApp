package com.example.siasmobile.Onboarding;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.siasmobile.MainActivity;
import com.example.siasmobile.Onboarding.Adapter.OnboardingPagerAdapter;
import com.example.siasmobile.R;

import java.util.ArrayList;
import java.util.List;

public class OnboardingActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private OnboardingPagerAdapter onboardingPagerAdapter;
    private Button btnFinishOnboarding;
    private TextView step1, step2, step3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);


        viewPager = findViewById(R.id.viewPager);
        btnFinishOnboarding = findViewById(R.id.btnFinishOnboarding);

        // Encontre os TextViews das etapas
        step1 = findViewById(R.id.step1);
        step2 = findViewById(R.id.step2);
        step3 = findViewById(R.id.step3);

        // Lista de layouts das telas
        List<Integer> layouts = new ArrayList<>();
        layouts.add(R.layout.onboarding_screen1);
        layouts.add(R.layout.onboarding_screen2);
        layouts.add(R.layout.onboarding_screen3);

        onboardingPagerAdapter = new OnboardingPagerAdapter(this, layouts);
        viewPager.setAdapter(onboardingPagerAdapter);

        // Configura o modo tela cheia
        hideSystemUI();

        // Inicialmente, defina o fundo da primeira etapa como laranja
        setStepBackground(step1, R.color.laranjaPrincipal);
        step1.setTextColor(getResources().getColor(R.color.white));

        // Listener para mudar a visibilidade do botão e animar o progresso das etapas
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                // Exibe o botão "Começar" apenas na última tela
                if (position == layouts.size() - 1) {
                    btnFinishOnboarding.setVisibility(View.VISIBLE);
                } else {
                    btnFinishOnboarding.setVisibility(View.GONE);
                }

                // Reseta o estado das etapas
                resetStepStates();

                // Atualiza a visibilidade dos ícones de checagem e anima o progresso
                switch (position) {
                    case 0:
                        animateStepTransition(step1);
                        break;
                    case 1:
                        animateStepTransition(step2);
                        break;
                    case 2:
                        animateStepTransition(step3);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        btnFinishOnboarding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Salva no SharedPreferences que o onboarding foi concluído
                SharedPreferences sharedPref = getSharedPreferences("onboarding", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean("completed", true);
                editor.apply();

                // Navega para a MainActivity
                Intent intent = new Intent(OnboardingActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private void hideSystemUI() {
        // Hides the system UI
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void resetStepStates() {
        // Reseta o fundo de todas as etapas para branco
        resetStepBackgrounds();

        // Define a cor do texto para todas as etapas
        step1.setTextColor(getResources().getColor(R.color.laranjaPrincipal));
        step2.setTextColor(getResources().getColor(R.color.laranjaPrincipal));
        step3.setTextColor(getResources().getColor(R.color.laranjaPrincipal));
    }

    private void resetStepBackgrounds() {
        // Define o fundo das etapas como branco
        setStepBackground(step1, R.color.white);
        setStepBackground(step2, R.color.white);
        setStepBackground(step3, R.color.white);
    }

    private void setStepBackground(TextView step, int color) {
        // Atualiza a cor de fundo do TextView
        GradientDrawable backgroundDrawable = (GradientDrawable) step.getBackground();
        backgroundDrawable.setColor(getResources().getColor(color));
    }

    private void animateStepTransition(TextView activeStep) {
        // Animação da cor de fundo da etapa ativa
        GradientDrawable backgroundDrawable = (GradientDrawable) activeStep.getBackground();
        int colorFrom = getResources().getColor(R.color.white);
        int colorTo = getResources().getColor(R.color.laranjaPrincipal);

        ValueAnimator backgroundColorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        backgroundColorAnimator.setDuration(300); // Duração da animação
        backgroundColorAnimator.addUpdateListener(animator -> {
            backgroundDrawable.setColor((int) animator.getAnimatedValue());
        });
        backgroundColorAnimator.start();

        // Animação da cor do texto da etapa ativa
        int textColorFrom = getResources().getColor(R.color.laranjaPrincipal);
        int textColorTo = getResources().getColor(android.R.color.white);

        ValueAnimator textColorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), textColorFrom, textColorTo);
        textColorAnimator.setDuration(500); // Duração da animação
        textColorAnimator.addUpdateListener(animator -> {
            activeStep.setTextColor((int) animator.getAnimatedValue());
        });
        textColorAnimator.start();
    }
}