package com.amigos.tadjir;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

public  class ColorChanger {
    public static void setNewColor(String colorCode, View... views) {
    int newColor = Color.parseColor(colorCode);
    for (View view : views) {
        view.setBackgroundColor(newColor);
    }
}
    public static void setNewTextColor(String colorCode, TextView... textViews) {
        int newColor = Color.parseColor(colorCode);
        for (TextView textView : textViews) {
            textView.setTextColor(newColor);
        }
    }

}