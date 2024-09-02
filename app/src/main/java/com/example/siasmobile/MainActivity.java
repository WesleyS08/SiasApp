package com.example.siasmobile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.siasmobile.Onboarding.OnboardingActivity;
import com.example.siasmobile.mander.Login;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Verifica se o onboarding foi concluído
        SharedPreferences sharedPref = getSharedPreferences("onboarding", MODE_PRIVATE);
        boolean onboardingCompleted = sharedPref.getBoolean("completed", false);

        if (onboardingCompleted) {
            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
            finish();
        } else {
            // Caso contrário, redireciona para a tela de onboarding
            Intent intent = new Intent(MainActivity.this, OnboardingActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
