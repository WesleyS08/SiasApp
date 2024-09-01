package com.example.siasmobile.mander;

import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.siasmobile.R;

public class Cadastro extends AppCompatActivity {

    private View tooltipView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        tooltipView = LayoutInflater.from(this).inflate(R.layout.tooltip_layout, null);

        findViewById(R.id.info_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTooltip("Caso seja de RH digite um CNPJ caso o contario um CPF.");
            }
        });
    }

    private void showTooltip(String message) {
        ((TextView) tooltipView.findViewById(R.id.tooltip_text)).setText(message);

        Toast toast = new Toast(getApplicationContext());
        toast.setView(tooltipView);
        toast.setGravity(Gravity.TOP | Gravity.START, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();

        // Remove the tooltip
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                toast.cancel();
            }
        }, 1000);
    }

}
