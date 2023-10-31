package com.overcome.tickets.ui.Activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.gson.Gson;
import com.overcome.tickets.MainActivity;
import com.overcome.tickets.R;
import com.overcome.tickets.Utilidades.Funciones;
import com.overcome.tickets.dialogFragment.NuevoTicket;
import com.overcome.tickets.dialogFragment.ResponderTicket;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TicketDetalles extends AppCompatActivity {

    Button btnResponderTicket,btnArchivarTicket,btnAbrirTicket;
    Funciones funciones;
    String Id_Ticket = "";
    String TAG="Pruebas_TicketDetalles";

    TextView lblTitulo, lblTipoIncidencia, lblGravedadIncidencia, lblEstadoTicket, lblNombreResponsable, lblEquipoResponsable, lblVersionSoftware, lblDescripcion,
            lblId_Archivado, lblFechaHoraCreacion;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_detalles);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        funciones = new Funciones();

        btnResponderTicket = findViewById(R.id.btnResponderTicket);
        btnArchivarTicket = findViewById(R.id.btnArchivarTicket);
        btnAbrirTicket = findViewById(R.id.btnAbrirTicket);

        lblTitulo  = findViewById(R.id.lblTitulo);
        lblTipoIncidencia  = findViewById(R.id.lblTipoIncidencia);
        lblGravedadIncidencia  = findViewById(R.id.lblGravedadIncidencia);
        lblEstadoTicket  = findViewById(R.id.lblEstadoTicket);
        lblNombreResponsable  = findViewById(R.id.lblNombreResponsable);
        lblEquipoResponsable  = findViewById(R.id.lblEquipoResponsable);
        lblVersionSoftware  = findViewById(R.id.lblVersionSoftware);
        lblDescripcion  = findViewById(R.id.lblDescripcion);
        lblId_Archivado  = findViewById(R.id.lblId_Archivado);
        lblFechaHoraCreacion  = findViewById(R.id.lblFechaHoraCreacion);

        SharedPreferences preferences = getSharedPreferences("datos", Context.MODE_PRIVATE);
        String ticketSelected = preferences.getString("ticketSelected","");

        Log.i(TAG,""+ticketSelected);

        if(ticketSelected.equals(""))
            onBackPressed();

        //Uso de la librería Gson
        final Gson gson = new Gson();
        Map<?,?> TicketMap = gson.fromJson(ticketSelected,Map.class);

        Id_Ticket = ""+TicketMap.get("Id_Ticket");
        String Id_Ticket = "" +TicketMap.get("Id_Ticket");
        String Titulo = "" +TicketMap.get("Titulo");
        String TipoIncidencia = "" +TicketMap.get("TipoIncidencia");
        String GravedadIncidencia = "" +TicketMap.get("GravedadIncidencia");
        String EstadoTicket = "" +TicketMap.get("EstadoTicket");
        String FechaHoraActualizacion = "" +TicketMap.get("FechaHoraActualizacion");
        String NombreResponsable = "" +TicketMap.get("NombreResponsable");
        String EquipoResponsable = "" +TicketMap.get("EquipoResponsable");
        String VersionSoftware = "" +TicketMap.get("VersionSoftware");
        String Descripcion = "" +TicketMap.get("Descripcion");
        String Id_Archivado = "" +TicketMap.get("Id_Archivado");
        String FechaHoraCreacion = "" +TicketMap.get("FechaHoraCreacion");

        this.setTitle("No. de Ticket: "+Id_Ticket);
        lblTitulo.setText(Titulo);
        lblTipoIncidencia.setText(TipoIncidencia);
        lblGravedadIncidencia.setText(GravedadIncidencia);
        lblEstadoTicket.setText(EstadoTicket);
        lblNombreResponsable.setText(NombreResponsable);
        lblEquipoResponsable.setText(EquipoResponsable);
        lblVersionSoftware.setText(VersionSoftware);
        lblDescripcion.setText(Descripcion);
        lblId_Archivado.setText(Id_Archivado);
        lblFechaHoraCreacion.setText(FechaHoraCreacion);


        //Identifica si esta archivado o no, para poder visualizar el botón correspondiente a la acción contraria
        if(Id_Archivado.equals("2")){
            btnArchivarTicket.setVisibility(View.GONE);
            btnAbrirTicket.setVisibility(View.VISIBLE);
        }



        btnResponderTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ResponderTicket responderTicket = new ResponderTicket();
                responderTicket.show(getSupportFragmentManager(), "Responder Ticket");
            }
        });

        btnArchivarTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Comprueba si hay Conexión a internet
                if (funciones.isNetDisponible(getApplicationContext())) {
                    ArchivarTicket();
                } else {
                    funciones.sadSucces(getApplicationContext(),"Verifica tu conexión e intenta de nuevo");
                }
            }
        });

        btnAbrirTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Comprueba si hay Conexión a internet
                if (funciones.isNetDisponible(getApplicationContext())) {
                    AbrirTicket();
                } else {
                    funciones.sadSucces(getApplicationContext(),"Verifica tu conexión e intenta de nuevo");
                }
            }
        });


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }

    OkHttpClient client = new OkHttpClient();

    private void ArchivarTicket() {
        RequestBody body = new FormBody.Builder()
                .add("Archivo","2")
                .add("Ticket",Id_Ticket)
                .build();

        Request request = new Request.Builder()
                .url(getResources().getString(R.string.URL_archivarTicket))
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Log.i("SERVICIOWEB","onFailure");
                        Toast.makeText(getApplicationContext() , "ERROR VERIFIQUE SU CONEXIÓN A INTERNET E INTÉNTELO NUEVAMENTE", Toast.LENGTH_LONG).show();
                    }
                });

                Looper.prepare(); // to be able to make toast
                Looper.loop();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //Log.i("beny","OnResponse");
                if (response.isSuccessful()) {
                    final String myResponse = response.body().string();

                    try {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String resp = myResponse;
                                //***************** RESPUESTA DEL WEBSERVICE **************************//
                                Toast.makeText(getApplicationContext(), "TICKET MODIFICADO  ", Toast.LENGTH_SHORT).show();
                                Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent1);
                                finish();
                            }
                        });
                    }
                    catch (Exception e){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "CATCH: ERROR, POR FAVOR VERIFIQUE SU CONEXIÓN A INTERNET E INTÉNTELO NUEVAMENTE", Toast.LENGTH_LONG).show();
                            }

                        });
                    }

                }
                else
                {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.i("SERVICIOWEB","Respuesta SW isnt Successful");
                            Toast.makeText(getApplicationContext(), " - ERROR, POR FAVOR VERIFIQUE SU CONEXIÓN A INTERNET E INTÉNTELO NUEVAMENTE", Toast.LENGTH_LONG).show();
                        }

                    });

                }
            }
        });
    }


    private void AbrirTicket() {
        RequestBody body = new FormBody.Builder()
                .add("Archivo","1")
                .add("Ticket",Id_Ticket)
                .build();

        Request request = new Request.Builder()
                .url(getResources().getString(R.string.URL_archivarTicket))
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Log.i("SERVICIOWEB","onFailure");
                        Toast.makeText(getApplicationContext() , "ERROR VERIFIQUE SU CONEXIÓN A INTERNET E INTÉNTELO NUEVAMENTE", Toast.LENGTH_LONG).show();
                    }
                });

                Looper.prepare(); // to be able to make toast
                Looper.loop();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //Log.i("beny","OnResponse");
                if (response.isSuccessful()) {
                    final String myResponse = response.body().string();

                    try {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String resp = myResponse;
                                //***************** RESPUESTA DEL WEBSERVICE **************************//
                                Toast.makeText(getApplicationContext(), "TICKET ARCHIVADO:", Toast.LENGTH_SHORT).show();
                                Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent1);
                            }
                        });
                    }
                    catch (Exception e){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "CATCH: ERROR, POR FAVOR VERIFIQUE SU CONEXIÓN A INTERNET E INTÉNTELO NUEVAMENTE", Toast.LENGTH_LONG).show();
                            }

                        });
                    }

                }
                else
                {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //  Log.i("SERVICIOWEB","Respuesta SW isnt Successful");
                            Toast.makeText(getApplicationContext(), " - ERROR, POR FAVOR VERIFIQUE SU CONEXIÓN A INTERNET E INTÉNTELO NUEVAMENTE", Toast.LENGTH_LONG).show();
                        }

                    });

                }
            }
        });
    }


}