package com.overcome.tickets.Utilidades;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Funciones {

    public void sadSucces(Context context, String Mensaje){
        new SweetAlertDialog(context)
                .setTitleText(Mensaje)
                .show();
    }

    public void sadError(Context context, String Mensaje){
        new SweetAlertDialog(context)
                .setTitleText(Mensaje)
                .show();
    }

    public boolean isNetDisponible(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo actNetInfo = connectivityManager.getActiveNetworkInfo();
        return (actNetInfo != null && actNetInfo.isConnected());
    }

    public Boolean isOnlineNet() {
        try {
            Process p = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.es");
            int val           = p.waitFor();
            boolean reachable = (val == 0);
            return reachable;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }
}
