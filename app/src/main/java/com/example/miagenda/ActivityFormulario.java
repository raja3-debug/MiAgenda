package com.example.miagenda;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class ActivityFormulario extends AppCompatActivity implements View.OnClickListener {
    EditText nombreTarea;
    Button btnCategoria, btnFecha, btnHora, btnGuardar, btnCancelar;
    TextView categoriaSeleccionada, fechaSeleccionada, horaSeleccionada;

    String CategoriaAsignada = "";
    String FechaAsignada = "";
    String HoraAsignada = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_tarea);
        nombreTarea=findViewById(R.id.nombreTarea);
        categoriaSeleccionada = findViewById(R.id.categoria);
        fechaSeleccionada = findViewById(R.id.fecha);
        horaSeleccionada = findViewById(R.id.hora);

        btnCategoria = findViewById(R.id.btnCategoria);
        btnFecha = findViewById(R.id.SeleccionarFecha);
        btnHora = findViewById(R.id.btnHora);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnCancelar = findViewById(R.id.btnCancelar);
        btnCategoria.setOnClickListener(this);
        btnFecha.setOnClickListener(this);
        btnHora.setOnClickListener(this);
        btnGuardar.setOnClickListener(this);
        btnCancelar.setOnClickListener(this);



    }



    @Override
    public void onClick(View v) {

        int id = v.getId();

        if (id == R.id.btnCategoria) {
            mostrarDialogoCategorias();
        } else if (id == R.id.SeleccionarFecha) {
            mostrarCalendarioFecha();
        } else if (id == R.id.btnHora) {
            mostrarHorario();
        } else if (id == R.id.btnGuardar) {
            guardarTarea();
        } else if (id == R.id.btnCancelar) {
            setResult(RESULT_CANCELED);
            finish();
        }

    }



    private void mostrarDialogoCategorias() {
         String[] categorias = {"Estudios", "Trabajo", "Personal", "Otros"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Elige una categoría");
        builder.setItems(categorias, (dialog, which) -> {
            CategoriaAsignada = categorias[which];
            categoriaSeleccionada.setText( CategoriaAsignada);
        });
        builder.show();
    }

    private void mostrarCalendarioFecha() {
        Calendar calendario = Calendar.getInstance();
        int anyo = calendario.get(Calendar.YEAR);
        int mes = calendario.get(Calendar.MONTH);
        int dia = calendario.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (DatePicker view, int year, int month, int dayOfMonth) -> {
                    // month empieza en 0, por eso se suma 1
                    FechaAsignada = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year);
                    fechaSeleccionada.setText(FechaAsignada);
                },
                anyo, mes, dia
        );
        datePickerDialog.show();
    }

    private void mostrarHorario() {
        Calendar calendario = Calendar.getInstance();
        int hora = calendario.get(Calendar.HOUR_OF_DAY);
        int minuto = calendario.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                (TimePicker view, int hourOfDay, int minute) -> {
                    HoraAsignada = String.format("%02d:%02d", hourOfDay, minute);
                    horaSeleccionada.setText(HoraAsignada);
                },
                hora, minuto, true
        );
        timePickerDialog.show();
    }

    private void guardarTarea() {
        String nombre = nombreTarea.getText().toString().trim();

        if (nombre.isEmpty() || CategoriaAsignada.isEmpty() || FechaAsignada.isEmpty() || HoraAsignada.isEmpty()) {
            Toast.makeText(this, "Rellena todos los campos obligatorios (*)", Toast.LENGTH_SHORT).show();
            return;
        }


        String detalleCompleto = "Categoría: " + CategoriaAsignada
                + "\nFecha: " + FechaAsignada
                + "\nHora: " + HoraAsignada;

        Intent resultado = new Intent();
        resultado.putExtra("nombre_tarea", nombre);
        resultado.putExtra("tarea_creada", detalleCompleto);
        setResult(RESULT_OK, resultado);
        finish();
    }
}
