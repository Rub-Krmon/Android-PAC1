package com.uoc.pac2.utils;

/**
 * @author Ruben Carmona
 * @project TFM - PAC2
 * @date 11/2016
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;



//    Tarea asíncrona que permite bajar una imagen y realizar un ejecto
//    fade para mostrarla.
//    Esta tarea asíncrona se utiliza junto con las URL de las imágenes
//    para descargarlas en segundo plano y de mientras mostrar al usuario
//    una imagen genérica de la portada de un libro

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    private ImageView bmImage;

    public DownloadImageTask(ImageView bmImage) {
        this.bmImage = bmImage;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        // create the transition layers
        Drawable[] layers = new Drawable[2];
        layers[0] = new BitmapDrawable(bmImage.getResources(), bmImage.getDrawingCache());
        layers[1] = new BitmapDrawable(bmImage.getResources(), result);

        TransitionDrawable transitionDrawable = new TransitionDrawable(layers);
        transitionDrawable.setCrossFadeEnabled(true);

        bmImage.setImageDrawable(transitionDrawable);
        transitionDrawable.startTransition(1000);
    }
}