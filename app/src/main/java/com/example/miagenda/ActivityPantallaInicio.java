package com.example.miagenda;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ActivityPantallaInicio  extends AppCompatActivity  implements View.OnClickListener {
    Button inicio;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_inicio);
        inicio=(Button) findViewById(R.id.btnInicio);
        inicio.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btnInicio){
            Intent intent = new Intent(ActivityPantallaInicio.this, ActivityPantallaPrincipal.class);
            startActivity(intent);

        }


    }
}
