package com.overcome.tickets.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.overcome.tickets.R;
import com.overcome.tickets.Utilidades.Funciones;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Tickets extends Fragment {
    ListView lvTickets;
    String TAG = "GetTickets";
    ArrayList<String> Tickets = new ArrayList<>();
    Funciones funciones;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tickets, container, false);
        lvTickets = view.findViewById(R.id.lvTickets);
        funciones = new Funciones();

        try {
            //Comprueba si hay Conexión a internet
            if (funciones.isOnlineNet()) {
                Log.d(TAG, "Estás online");
                SharedPreferences preferences = getActivity().getSharedPreferences("datos", Context.MODE_PRIVATE);
                String Usuario = preferences.getString("Usuario", "");
                GetTickets(Usuario);
            } else {
                Log.d(TAG, "NO Estás online");
                //funciones.SweetAlertDialogSucces(MainActivity.this,"Verifica tu conexión a Internet");
                Toast.makeText(getContext(), "Verifica tu conexión", Toast.LENGTH_SHORT);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return view;
    }


    OkHttpClient client = new OkHttpClient();
    public void GetTickets(String User) throws IOException {
        Tickets.clear();
        Request request = new Request.Builder()
                .url(getResources().getString(R.string.URL_GetTickets)+ User)
                .build();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try (Response response = client.newCall(request).execute()) {
                    String Respuesta = response.body().string();
                    Log.i(TAG, "response" + Respuesta);

                    if (Respuesta.equals("{\"Tickets\":[]}")) {
                        Toast.makeText(getContext(),"No se encontraron Tickets",Toast.LENGTH_LONG).show();
                    }else {
                        getActivity().runOnUiThread(new Runnable() {@Override public void run() {

                            //Mostrar los valores en el Listview

                            JSONObject jobject = null;
                            try {
                                jobject = new JSONObject(Respuesta);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            JSONArray arr = null;
                            try {
                                arr = jobject.getJSONArray("Tickets");
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            JSONObject jo;

                            for (int i = 0; i < arr.length(); i++) {
                                Log.i(TAG, "iteracion: " + i);

                                try {
                                    jo = arr.getJSONObject(i);
                                    Log.i(TAG, "json: " + jo.toString());
                                    Tickets.add(jo.toString());

                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }});

                        getActivity().runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                MyAdapter adapterRadio = new MyAdapter(getContext(),Tickets);
                                lvTickets.setAdapter(adapterRadio);
                                // Stuff that updates the UI

                            }
                        });
                    }
                } catch (Exception e) {
                    getActivity().runOnUiThread(new Runnable() {@Override public void run() {
                        Toast.makeText(getContext(),"Error al consultar al servidor"+e.toString(),Toast.LENGTH_LONG).show();
                        Log.i(TAG, "json: " + e.toString());

                    }});

                }
            }
        });

        thread.start();
    }

    class MyAdapter extends ArrayAdapter<String> {
        Context context;
        ArrayList<String> listTicket;

        MyAdapter (Context c, ArrayList<String> listTicket) {

            super(c, R.layout.row_ticket, listTicket);
            this.context = c;
            this.listTicket = listTicket;
            Log.i(TAG, "adapter:"+listTicket);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.row_ticket, parent, false);

            LinearLayout ly_indicadorGravedad = row.findViewById(R.id.ly_indicadorGravedad);
            TextView row_NoTicket = row.findViewById(R.id.row_NoTicket);
            TextView row_Titulo = row.findViewById(R.id.row_Titulo);
            TextView row_Gravedad = row.findViewById(R.id.row_Gravedad);
            TextView row_TipoIncidencia = row.findViewById(R.id.row_TipoIncidencia);
            TextView row_Estado = row.findViewById(R.id.row_Estado);
            TextView row_Fecha = row.findViewById(R.id.row_Fecha);

            try {
                JSONObject jsonObject = new JSONObject(listTicket.get(position));
                System.out.println("OBJECT : "+jsonObject.toString());

                int color= Color.RED;
                String Gravedad = (String)jsonObject.get("GravedadIncidencia");

                if(Gravedad.equals("Low"))
                    color = Color.GREEN;
                else if(Gravedad.equals("Medium"))
                    color = Color.YELLOW;



                ly_indicadorGravedad.setBackgroundColor(color);
                row_NoTicket.setText("No. Ticket: "+(String)jsonObject.get("Id_Ticket"));
                row_Titulo.setText((String)jsonObject.get("Titulo"));
                row_Gravedad.setText("Gravedad "+Gravedad);
                row_TipoIncidencia.setText("Tipo de Incidencia: "+(String)jsonObject.get("TipoIncidencia"));
                row_Estado.setText("Estado "+(String)jsonObject.get("EstadoTicket"));
                row_Fecha.setText((String)jsonObject.get("FechaHoraActualizacion"));

            } catch (JSONException err) {
                Log.i(TAG, "jsonadapter Error: "+err.toString());
            }


            return row;
        }


    }
}
