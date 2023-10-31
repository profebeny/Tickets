package com.overcome.tickets.ui.Activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
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

    Button btnResponderTicket,btnArchivarTicket;
    Funciones funciones;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_detalles);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        funciones = new Funciones();

        btnResponderTicket = findViewById(R.id.btnResponderTicket);
        btnArchivarTicket = findViewById(R.id.btnArchivarTicket);

        SharedPreferences preferences = getSharedPreferences("datos", Context.MODE_PRIVATE);
        String ticketSelected = preferences.getString("ticketSelected","");

        if(ticketSelected.equals(""))
            onBackPressed();

        //Uso de la librería Gson
        final Gson gson = new Gson();
        Map<?,?> TicketMap = gson.fromJson(ticketSelected,Map.class);

        this.setTitle("No. de Ticket: "+TicketMap.get("Id_Ticket"));


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


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }

    private void ArchivarTicket() {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("Archivo","2")
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