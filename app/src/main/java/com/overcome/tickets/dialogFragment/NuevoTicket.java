package com.overcome.tickets.dialogFragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.overcome.tickets.MainActivity;
import com.overcome.tickets.R;
import com.overcome.tickets.Utilidades.Funciones;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NuevoTicket extends DialogFragment {

    Button btnCrearTicket;
    EditText txtNombreResponsable;
    Funciones funciones;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.df_nuevoticket,container,false);
        btnCrearTicket = view.findViewById(R.id.btnCrearTicket);
        txtNombreResponsable = view.findViewById(R.id.txtNombreResponsable);
        funciones = new Funciones();

        btnCrearTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validarCampos())
                {

                }
                Toast.makeText(getContext(),"Mensaje"+ txtNombreResponsable.getText().toString(),Toast.LENGTH_SHORT).show();
            }
        });


        return view;
    }

    private boolean validarCampos() {

        return true;
    }

}
