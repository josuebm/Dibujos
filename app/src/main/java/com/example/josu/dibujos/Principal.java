package com.example.josu.dibujos;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class Principal extends Activity{

    private MenuItem menuItem, menuItemGrosor;
    private Vista vista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vista = new Vista(this);
        setContentView(vista);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        menuItem = menu.findItem(R.id.action_figura);
        menuItemGrosor = menu.findItem(R.id.action_grosor);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_color) {
            vista.cambioColor(this, vista);
            return true;
        }else if(id == R.id.action_grosor){
            vista.cambioGrosor(this, vista);
            return true;
        }else if(id == R.id.action_linea){
            menuItem.setIcon(R.drawable.linea);
            vista.setFigura("linea");
            menuItemGrosor.setVisible(true);
            return true;
        }else if(id == R.id.action_cuadrado){
            menuItem.setIcon(R.drawable.square);
            vista.setFigura("cuadrado");
            menuItemGrosor.setVisible(true);
            return true;
        }else if(id == R.id.action_circulo){
            menuItem.setIcon(R.drawable.circle);
            vista.setFigura("circulo");
            menuItemGrosor.setVisible(true);
            return true;
        }else if(id == R.id.action_lapiz){
            menuItem.setIcon(R.drawable.lapiz);
            vista.setFigura("lapiz");
            menuItemGrosor.setVisible(true);
            return true;
        }else if(id == R.id.action_cuadrado_relleno) {
            menuItem.setIcon(R.drawable.square_relleno);
            vista.setFigura("cuadrado relleno");
            menuItemGrosor.setVisible(false);
            return true;
        }else if(id == R.id.action_circulo_relleno) {
            menuItem.setIcon(R.drawable.circle_relleno);
            vista.setFigura("circulo relleno");
            menuItemGrosor.setVisible(false);
            return true;
        }else if(id == R.id.action_goma) {
            vista = new Vista(this);
            setContentView(vista);
            menuItem.setIcon(R.drawable.lapiz);
            vista.setFigura("lapiz");
            menuItemGrosor.setVisible(true);
            return true;
        }else if(id == R.id.action_guardar) {
            try {
                guardarFoto(vista.getDibujo());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void tostada(String mensaje){
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }

    public void guardarFoto(final Bitmap foto) throws IOException {
        final EditText input = new EditText(this);
        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.nombre_guardar))
                .setView(input)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        FileOutputStream fos = null;
                        String nombre = input.getText().toString() + ".jpg";
                        File archivo = null;

                        if (espacioSuficiente(getExternalFilesDir(Environment.DIRECTORY_DCIM))) {
                            archivo = new File(getExternalFilesDir(Environment.DIRECTORY_DCIM), nombre);
                            if (!archivo.exists()) {
                                try {
                                    fos = new FileOutputStream(archivo);
                                    fos.flush();
                                    foto.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else
                                tostada(getResources().getString(R.string.nombre_usado));
                        }
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .show();

    }

    public boolean espacioSuficiente(File f) {
        double eTotal, eDisponible, porcentaje;
        eTotal = (double) f.getTotalSpace();
        eDisponible = (double) f.getFreeSpace();
        porcentaje = (eDisponible / eTotal) * 100;
        return porcentaje > 10;
    }
}
