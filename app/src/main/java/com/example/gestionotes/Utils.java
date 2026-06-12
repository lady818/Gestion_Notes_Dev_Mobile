package com.example.gestionotes;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;

public class Utils {

    // convertit les dp en pixels (pour les tailles à l'écran)
    public static int dp(Activity activity, int valeur) {
        float density = activity.getResources().getDisplayMetrics().density;
        return (int) (valeur * density);
    }

    // fond coloré avec des coins arrondis
    public static GradientDrawable fond(String couleur, int rayon) {
        GradientDrawable fond = new GradientDrawable();
        fond.setColor(Color.parseColor(couleur));
        fond.setCornerRadius(rayon);
        return fond;
    }
}
