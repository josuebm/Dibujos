package com.example.josu.dibujos;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SeekBar;

/**
 * Created by Josué on 05/02/2015.
 */
public class Vista extends View implements SeekBar.OnSeekBarChangeListener, ColorPickerDialog.OnColorSelectedListener{

    private Paint pincel;
    private Bitmap mapaDeBits;
    private float x0=-1, y0=-1, xi=-1, yi=-1;
    private Canvas lienzoFondo;
    private String figura;
    private Path rectaPoligonal = new Path();
    private double radio =0;
    private int grosor = 1;

    public Vista(Context context) {
        super(context);
        pincel = new Paint();
        pincel.setColor(Color.BLACK);
        pincel.setAntiAlias(true);
        figura = "lapiz";
    }

    @Override
    protected void onDraw(Canvas lienzo) {
        super.onDraw(lienzo);
        lienzo.drawColor(Color.WHITE);
        lienzo.drawBitmap(mapaDeBits, 0, 0, null);
        if(figura.equals("linea"))
            lienzo.drawLine(x0, y0, xi, yi, pincel);
        else if(figura.equals("lapiz")){
            lienzo.drawPath(rectaPoligonal, pincel);
            pincel.setStyle(Paint.Style.STROKE);
        }else if (figura.equals("cuadrado")){
            float xorigen = Math.min(x0, xi);
            float xdestino = Math.max(x0, xi);
            float yorigen = Math.min(y0, yi);
            float ydestino = Math.max(y0, yi);
            pincel.setStyle(Paint.Style.STROKE);
            lienzo.drawRect(xorigen, yorigen, xdestino, ydestino, pincel);
        }else if(figura.equals("circulo")){
            lienzo.drawCircle(x0, y0, (float)radio, pincel);
            pincel.setStyle(Paint.Style.STROKE);
        }else if (figura.equals("cuadrado relleno")) {
            float xorigen = Math.min(x0, xi);
            float xdestino = Math.max(x0, xi);
            float yorigen = Math.min(y0, yi);
            float ydestino = Math.max(y0, yi);
            pincel.setStyle(Paint.Style.FILL);
            lienzo.drawRect(xorigen, yorigen, xdestino, ydestino, pincel);
        }else if(figura.equals("circulo relleno")) {
            lienzo.drawCircle(x0, y0, (float) radio, pincel);
            pincel.setStyle(Paint.Style.FILL);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:{
                    x0 = x;
                    y0 = y;
                if(figura.equals("lapiz")){
                    rectaPoligonal.reset();
                    rectaPoligonal.moveTo(x0, y0);
                }
            }
            break;
            case MotionEvent.ACTION_MOVE:{
                xi = x;
                yi = y;
                if(figura.equals("lapiz"))
                    rectaPoligonal.quadTo(xi, yi, (x + xi) / 2, (y + yi)/2);
                else if(figura.startsWith("circulo"))
                    radio = Math.sqrt(Math.pow(xi - x0, 2) + Math.pow(yi - y0, 2));
                invalidate();
            }
            break;
            case MotionEvent.ACTION_UP:{
                xi = x;
                yi = y;
                if(figura.equals("linea"))
                    lienzoFondo.drawLine(x0, y0, xi, yi, pincel);
                else if(figura.equals("lapiz"))
                    lienzoFondo.drawPath(rectaPoligonal, pincel);
                else if (figura.startsWith("cuadrado")){
                    if(xi < x0 || yi < y0)
                        lienzoFondo.drawRect(xi, yi, x0, y0, pincel);
                    else
                        lienzoFondo.drawRect(x0, y0, xi, yi, pincel);
                }else if (figura.startsWith("circulo")){
                    radio = Math.sqrt(Math.pow(xi - x0, 2) + Math.pow(yi - y0, 2));
                    lienzoFondo.drawCircle(x0, y0, (float)radio, pincel);
                }
                invalidate();
                radio = 0;
                x0=y0=xi=yi=-1;
            }
            break;
        }
        return true;

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mapaDeBits = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mapaDeBits.eraseColor(Color.WHITE);
        lienzoFondo = new Canvas(mapaDeBits);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        grosor = progress;
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onColorSelected(int color) {
        pincel.setColor(color);
    }


    public void cambioColor(Context contexto, ColorPickerDialog.OnColorSelectedListener listener){
        ColorPickerDialog colorPickerDialog = new ColorPickerDialog(contexto, pincel.getColor(), listener);
        colorPickerDialog.show();
    }

    public void cambioGrosor(Context contexto, SeekBar.OnSeekBarChangeListener listener){
        SeekBar barra = new SeekBar(contexto);
        barra.setOnSeekBarChangeListener(listener);
        barra.setProgress((int)pincel.getStrokeWidth());
        barra.setMax(20);
        new AlertDialog.Builder(contexto)
                .setTitle(getResources().getString(R.string.grosor_pincel))
                .setView(barra)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        pincel.setStrokeWidth(grosor);
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .show();
    }

    public void setFigura(String figura){
        this.figura = figura;
    }

    public Bitmap getDibujo(){
        return mapaDeBits;
    }
}
