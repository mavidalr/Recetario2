package com.example.mache.recetario2.adater;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.io.FileInputStream;
import java.util.List;

import com.example.mache.recetario2.R;
import com.example.mache.recetario2.app.AppController;
import com.example.mache.recetario2.model.Receta;

public class CustomListAdapterDevice extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Receta> RecetaItems;
    //ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public CustomListAdapterDevice(Activity activity, List<Receta> RecetaItems) {
        this.activity = activity;
        this.RecetaItems = RecetaItems;

    }

    @Override
    public int getCount() {
        return RecetaItems.size();
    }

    @Override
    public Object getItem(int location) {
        return RecetaItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_row_device, null);

        //if (imageLoader == null)
        //    imageLoader = AppController.getInstance().getImageLoader();
        ImageView thumbNail = (ImageView) convertView.findViewById(R.id.DFotoReceta);

        //ImageView thumbNail = (ImageView) convertView.findViewById(R.id.FotoReceta);
        TextView NombreReceta = (TextView) convertView.findViewById(R.id.DNombreReceta);
        TextView Categoria = (TextView) convertView.findViewById(R.id.DCategoria);
        TextView TiempoPreparacion = (TextView) convertView.findViewById(R.id.DTiempoPreparacion);
        //TextView year = (TextView) convertView.findViewById(R.id.releaseYear);
        TextView IdReceta = (TextView) convertView.findViewById(R.id.DIdReceta);

        TextView URL = (TextView) convertView.findViewById(R.id.DURL);

        // getting Receta data for the row
        Receta m = RecetaItems.get(position);

        Bitmap myBitmap = BitmapFactory.decodeFile(m.getFotoReceta());
        thumbNail.setImageBitmap(myBitmap);

        // thumbnail image
        //thumbNail.setImageUrl(m.getFotoReceta(), imageLoader);


        // NombreReceta
        NombreReceta.setText(m.getNombreReceta());

        // rating
        Categoria.setText("Categoría: " + String.valueOf(m.getCategoria()));
        // genre
        TiempoPreparacion.setText("Praparación: " + String.valueOf(m.getTiempoPraparacion()) + " min.");
        IdReceta.setText(String.valueOf(m.getIdReceta()));
        String IDRECETA = String.valueOf(m.getIdReceta());

        URL.setText(String.valueOf(m.getURL()));

        //Cargar img de fondo



        return convertView;
    }


}