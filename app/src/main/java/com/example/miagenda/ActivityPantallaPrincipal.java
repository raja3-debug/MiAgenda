package com.example.miagenda;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ActivityPantallaPrincipal extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    ListView list;
    TextView texto;
    Button nuevaTarea;
    ArrayAdapter<String> arr;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_principal);
        View v;
        list=findViewById(R.id.lista);
        nuevaTarea=(Button)findViewById(R.id.btnNuevaTarea);
        nuevaTarea.setOnClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btnNuevaTarea){

        }


    }
}
