package com.overcome.tickets.dialogFragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.overcome.tickets.MainActivity;
import com.overcome.tickets.R;
import com.overcome.tickets.Utilidades.Funciones;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NuevoTicket extends DialogFragment {

    Button btnCrearTicket;
    EditText txtNombreResponsable,txtVersionSoftware,txtVDescripcion,txtTitulo;
    Spinner spEquipoResponsable,spTipoIncidencia,spGravedadIncidencia;
    Funciones funciones;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.df_nuevoticket,container,false);
        btnCrearTicket = view.findViewById(R.id.btnCrearTicket);

        txtTitulo = view.findViewById(R.id.txtTitulo);
        txtNombreResponsable = view.findViewById(R.id.txtNombreResponsable);
        txtVersionSoftware = view.findViewById(R.id.txtVersionSoftware);
        txtVDescripcion = view.findViewById(R.id.txtVDescripcion);

        spEquipoResponsable = view.findViewById(R.id.spEquipoResponsable);
        spTipoIncidencia = view.findViewById(R.id.spTipoIncidencia);
        spGravedadIncidencia = view.findViewById(R.id.spGravedadIncidencia);
        funciones = new Funciones();

        btnCrearTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String Titulo = txtTitulo.getText().toString();
                String NombreResponsable = txtNombreResponsable.getText().toString();
                String Id_EquipoResponsable = Integer.toString(spEquipoResponsable.getSelectedItemPosition()+1);
                String Id_TipoIncidencia = Integer.toString(spTipoIncidencia.getSelectedItemPosition()+1);
                String Id_GravedadIncidencia = Integer.toString(spGravedadIncidencia.getSelectedItemPosition()+1);
                String VersionSoftware = txtVersionSoftware.getText().toString();
                String Descripcion = txtVDescripcion.getText().toString();
                SharedPreferences preferences = getActivity().getSharedPreferences("datos", Context.MODE_PRIVATE);
                String Usuario = preferences.getString("Usuario", "");


                if(validarCampos(Titulo,NombreResponsable,Descripcion))
                {

                    //Comprueba si hay Conexión a internet
                    if (funciones.isNetDisponible(getContext())) {
                        GeneraTicket(Titulo,NombreResponsable,Id_EquipoResponsable,Id_TipoIncidencia,Id_GravedadIncidencia,VersionSoftware,Descripcion,Usuario);
                    } else {
                        Toast.makeText(getContext(), "Verifica tu conexión e intenta de nuevo", Toast.LENGTH_LONG).show();

                    }
                }
            }
        });

        return view;
    }

    private boolean validarCampos(String Titulo,String NombreResponsable,String Descripcion) {
        if(Titulo.equals("")){
            Toast.makeText(getContext(),"El Título del Ticket no puede ser vacio",Toast.LENGTH_SHORT).show(); return false;
        }
        if(NombreResponsable.equals("")){
            Toast.makeText(getContext(),"El Nombre del responsable del Ticket no puede ser vacío",Toast.LENGTH_SHORT).show(); return false;
        }
        if(Descripcion.equals("")){
            Toast.makeText(getContext(),"La Descripcion del Ticket no puede ser vacio",Toast.LENGTH_SHORT).show(); return false;
        }
        return true;
    }
    private void GeneraTicket(String Titulo,String NombreResponsable, String Id_EquipoResponsable,String Id_TipoIncidencia,String Id_GravedadIncidencia,String VersionSoftware,String Descripcion,String Usuario) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("Titulo",Titulo)
                .add("NombreResponsable",NombreResponsable)
                .add("Id_EquipoResponsable",Id_EquipoResponsable)
                .add("Id_TipoIncidencia",Id_TipoIncidencia)
                .add("Id_GravedadIncidencia",Id_GravedadIncidencia)
                .add("VersionSoftware",VersionSoftware)
                .add("Descripcion",Descripcion)
                .add("Usuario",Usuario)
                .build();

        Request request = new Request.Builder()
                .url(getResources().getString(R.string.URL_insertTickets))
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Log.i("SERVICIOWEB","onFailure");
                        Toast.makeText(getContext(), "ERROR VERIFIQUE SU CONEXIÓN A INTERNET E INTÉNTELO NUEVAMENTE", Toast.LENGTH_LONG).show();
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
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String resp = myResponse;
                                //***************** RESPUESTA DEL WEBSERVICE **************************//
                                Toast.makeText(getContext(), "TICKET GENERADO:", Toast.LENGTH_SHORT).show();
                                        Intent intent1 = new Intent(getContext(), MainActivity.class);
                                        startActivity(intent1);
                            }
                        });
                    }
                    catch (Exception e){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(), "CATCH: ERROR ENVIAR SU ALERTA, POR FAVOR VERIFIQUE SU CONEXIÓN A INTERNET E INTÉNTELO NUEVAMENTE", Toast.LENGTH_LONG).show();
                            }

                        });
                    }

                }
                else
                {

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //  Log.i("SERVICIOWEB","Respuesta SW isnt Successful");
                            Toast.makeText(getContext(), " - ERROR ENVIAR SU ALERTA, POR FAVOR VERIFIQUE SU CONEXIÓN A INTERNET E INTÉNTELO NUEVAMENTE", Toast.LENGTH_LONG).show();

                        }

                    });

                }
            }
        });
    }
}
