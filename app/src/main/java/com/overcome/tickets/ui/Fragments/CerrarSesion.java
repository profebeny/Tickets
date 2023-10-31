package com.overcome.tickets.ui.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.overcome.tickets.ui.Activitys.Login;
import com.overcome.tickets.R;

public class CerrarSesion extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cerrar_sesion, container, false);

            Intent intent1 = new Intent(getContext(), Login.class);
            startActivity(intent1);

        return view;
    }
}
