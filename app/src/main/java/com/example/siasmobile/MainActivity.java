package com.example.siasmobile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.siasmobile.Onboarding.OnboardingActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Verifica no SharedPreferences se o onboarding já foi completado
        SharedPreferences sharedPref = getSharedPreferences("onboarding", MODE_PRIVATE);
        boolean onboardingCompleted = sharedPref.getBoolean("completed", false);

        if (!onboardingCompleted) {
            // Se o onboarding não foi completado, redireciona para a OnboardingActivity
            Intent intent = new Intent(this, OnboardingActivity.class);
            startActivity(intent);
            finish();
        } else {
            setContentView(R.layout.activity_cadastro);
        }
    }
}
