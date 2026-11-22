package com.example.miagenda;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import java.util.ArrayList;

public class ActivityPantallaPrincipal extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    ListView list;
    Button nuevaTarea;
    TextView agendaVacia;
    TextView totalTareas;
    ArrayList<String> listaTareas;
    ArrayList<String> listaDetalles;
     ArrayAdapter<String> arr;
    private final ActivityResultLauncher<Intent> launcher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == RESULT_OK
                                && result.getData() != null) {
                            Intent datos = result.getData();
                            String nombre = datos.getStringExtra("nombre_tarea");
                            String detalle = datos.getStringExtra("tarea_creada");

                            if (nombre != null && !nombre.isEmpty() && detalle != null)
                                listaTareas.add(nombre);
                                listaDetalles.add(detalle);
                                actualizarPantalla();
                        }
                    }
            );
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_principal);

        View v;
        list=findViewById(R.id.lista);
        nuevaTarea = findViewById(R.id.btnNuevaTarea);

        nuevaTarea.setOnClickListener(this);
        agendaVacia = findViewById(R.id.AgendaVacia);
        totalTareas = findViewById(R.id.totalTareas);
        list.setOnItemClickListener(this);
        listaTareas = new ArrayList<>();
        listaDetalles = new ArrayList<>();

        arr = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaTareas);
        list.setAdapter(arr);
        actualizarPantalla();



    }
    private void actualizarPantalla() {
        arr.notifyDataSetChanged();

        if (listaTareas.size() == 0) {
            agendaVacia.setVisibility(View.VISIBLE);
            list.setVisibility(View.GONE);
        } else {
            agendaVacia.setVisibility(View.GONE);
            list.setVisibility(View.VISIBLE);
        }

        totalTareas.setText("Total de tareas: " + listaTareas.size());
    }
    private NotificationManager notificator;
    private static final String CANAL_ID = "Canal_ID";
    private int idNotificacion = 1;

    private void lanzarNotificacionRecordatorio(String nombreTarea, String detalleTarea) {
        notificator = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && notificator != null) {
            NotificationChannel canal = new NotificationChannel(
                    CANAL_ID,
                    "Recordatorios MiAgenda",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            notificator.createNotificationChannel(canal);
        }

        Intent intent = new Intent(this, ActivityPantallaPrincipal.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(ActivityPantallaInicio.class);
        stackBuilder.addNextIntent(intent);

        int flags = PendingIntent.FLAG_UPDATE_CURRENT;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            flags |= PendingIntent.FLAG_IMMUTABLE;
        }

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, flags);

        String textoDetalle = detalleTarea.replace("\n", " · ");

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CANAL_ID)
                .setSmallIcon(android.R.drawable.stat_sys_warning)
                .setContentTitle("Recordatorio: " + nombreTarea)
                .setContentText(textoDetalle)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(nombreTarea + "\n" + detalleTarea))
                .setTicker("AVISO DE NOTIFICACIÓN")
                .setContentIntent(resultPendingIntent)
                .setAutoCancel(true);

        if (notificator != null) {
            notificator.notify(idNotificacion++, builder.build());
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        String nombre = listaTareas.get(position);
        String detalle = listaDetalles.get(position);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(nombre);
        builder.setMessage(detalle);
        builder.setPositiveButton("Terminar", (dialog, which) -> {

        });

        builder.setNegativeButton("Eliminar", (dialog, which) -> {
            AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(this);
            confirmBuilder.setTitle("Confirmar eliminación");
            confirmBuilder.setMessage("¿Seguro que quieres eliminar \"" + nombre + "\"?");

            confirmBuilder.setPositiveButton("Confirmar", (confirmDialog, confirmWhich) -> {
                listaTareas.remove(position);
                listaDetalles.remove(position);
                actualizarPantalla();
                Toast.makeText(this, "Tarea eliminada", Toast.LENGTH_SHORT).show();
            });

            confirmBuilder.setNegativeButton("Anular", null);
            confirmBuilder.show();
        });

        builder.setNeutralButton("Recordatorio", (dialog, which) -> {
            lanzarNotificacionRecordatorio(nombre, detalle);
            Toast.makeText(this, "Notificación generada", Toast.LENGTH_SHORT).show();
        });

        builder.show();

    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btnNuevaTarea){
            Intent intent = new Intent(ActivityPantallaPrincipal.this, ActivityFormulario.class);
            launcher.launch(intent);

        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_opcion_categorias, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.estudios) {
            mostrarConteoCategoria("Estudios");
            return true;
        } else if (id == R.id.trabajo) {
            mostrarConteoCategoria("Trabajo");
            return true;
        } else if (id == R.id.personal) {
            mostrarConteoCategoria("Personal");
            return true;
        } else if (id == R.id.otros) {
            mostrarConteoCategoria("Otros");
            return true;
        } else if (id==R.id.vaciar_agenda) {
            mostrarDialogoVaciarAgenda();

            
        } else if (id==R.id.acerca_de) {
            mostrarDialogoAcercaDe();
            
        }

        return super.onOptionsItemSelected(item);
    }
    private void mostrarDialogoVaciarAgenda() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Vaciar agenda");
        builder.setMessage("¿Seguro que quieres eliminar todas las tareas?");

        builder.setPositiveButton("Confirmar", (dialog, which) -> {
            listaTareas.clear();
            listaDetalles.clear();
            actualizarPantalla();
            Toast.makeText(this, "Agenda vaciada", Toast.LENGTH_SHORT).show();
        });


        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }
    private void mostrarDialogoAcercaDe() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Acerca de");
        builder.setMessage(
                "Aplicación: MiAgenda\n" +
                        "Versión: 1.0\n" +
                        "Desarrollador: RAJAE BOUZAMBOU EL GHALBZOURI"
        );
        builder.setPositiveButton("Aceptar", null);
        builder.show();
    }
    public void mostrarConteoCategoria(String categoria){
        int contador = 0;

        for (int i = 0; i < listaDetalles.size(); i++) {
            if (listaDetalles.get(i).contains(categoria)) {
                contador++;
            }
        }

        Toast.makeText(
                this,
                "Tareas en " + categoria + ": " + contador,
                Toast.LENGTH_SHORT
        ).show();
    }

}
