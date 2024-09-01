package com.example.siasmobile.Onboarding;


import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.siasmobile.R;
import com.example.siasmobile.mander.Login;
import com.example.siasmobile.Onboarding.Adapter.OnboardingPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class OnboardingActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private OnboardingPagerAdapter onboardingPagerAdapter;
    private Button btnFinishOnboarding;
    private TextView[] steps;
    private ValueAnimator currentBackgroundAnimator;
    private ValueAnimator currentTextAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        viewPager = findViewById(R.id.viewPager);
        btnFinishOnboarding = findViewById(R.id.btnFinishOnboarding);

        // Inicializando TextViews das etapas
        steps = new TextView[]{
                findViewById(R.id.step1),
                findViewById(R.id.step2),
                findViewById(R.id.step3)
        };

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
        setStepBackground(steps[0], R.color.laranjaPrincipal);
        steps[0].setTextColor(getResources().getColor(R.color.white));

        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                btnFinishOnboarding.setVisibility(position == layouts.size() - 1 ? View.VISIBLE : View.GONE);

                // Reseta o estado das etapas
                resetStepStates();

                // Atualiza a visibilidade dos ícones de checagem e anima o progresso
                if (position < steps.length) {
                    animateStepTransition(steps[position]);
                }
            }
        });

        btnFinishOnboarding.setOnClickListener(v -> {
            Log.d("OnboardingActivity", "Botão Continuar clicado");
            // Marca o onboarding como concluído
            SharedPreferences sharedPref = getSharedPreferences("onboarding", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean("completed", true);
            editor.apply();

            // Redireciona para a tela de login
            Intent intent = new Intent(OnboardingActivity.this, Login.class);
            startActivity(intent);
            finish(); // Finaliza a OnboardingActivity para não voltar a ela com o botão de voltar
        });

    }

    // Parte que oculta os botões e a navbar do celular
    private void hideSystemUI() {
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

    // Reseta as cores do progress bar
    private void resetStepStates() {
        for (TextView step : steps) {
            setStepBackground(step, R.color.white);
            step.setTextColor(getResources().getColor(R.color.laranjaPrincipal));
        }
    }

    private void setStepBackground(TextView step, int color) {
        Drawable background = step.getBackground();
        if (background instanceof GradientDrawable) {
            GradientDrawable backgroundDrawable = (GradientDrawable) background;
            backgroundDrawable.setColor(getResources().getColor(color));
        } else {
            step.setBackgroundColor(getResources().getColor(color));
        }
    }

    // Animação para as transições
    private void animateStepTransition(TextView activeStep) {
        Drawable background = activeStep.getBackground();

        // Cancela animações anteriores para evitar sobreposição
        if (currentBackgroundAnimator != null && currentBackgroundAnimator.isRunning()) {
            currentBackgroundAnimator.cancel();
        }
        if (currentTextAnimator != null && currentTextAnimator.isRunning()) {
            currentTextAnimator.cancel();
        }

        if (background instanceof GradientDrawable) {
            GradientDrawable backgroundDrawable = (GradientDrawable) background;
            int colorFrom = getResources().getColor(R.color.white);
            int colorTo = getResources().getColor(R.color.laranjaPrincipal);

            currentBackgroundAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
            currentBackgroundAnimator.setDuration(300);
            currentBackgroundAnimator.addUpdateListener(animator -> backgroundDrawable.setColor((int) animator.getAnimatedValue()));

            // Animação da cor do texto da etapa ativa
            int textColorFrom = getResources().getColor(R.color.laranjaPrincipal);
            int textColorTo = getResources().getColor(android.R.color.white);

            currentTextAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), textColorFrom, textColorTo);
            currentTextAnimator.setDuration(500);
            currentTextAnimator.addUpdateListener(animator -> {
                activeStep.setTextColor((int) animator.getAnimatedValue());
                activeStep.invalidate(); // Força o redesenho
            });

            currentBackgroundAnimator.start();
            currentTextAnimator.start();
        } else {
            activeStep.setBackgroundColor(getResources().getColor(R.color.laranjaPrincipal));
            activeStep.setTextColor(getResources().getColor(android.R.color.white));
        }
    }
}
