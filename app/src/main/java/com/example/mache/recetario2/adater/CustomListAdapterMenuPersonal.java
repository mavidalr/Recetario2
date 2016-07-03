package com.example.mache.recetario2.adater;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mache.recetario2.R;
import com.example.mache.recetario2.model.Receta;

import java.io.File;
import java.util.List;

public class CustomListAdapterMenuPersonal extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Receta> RecetaItems;
    //ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public CustomListAdapterMenuPersonal(Activity activity, List<Receta> RecetaItems) {
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
            convertView = inflater.inflate(R.layout.list_row_menu_personal, null);

        //if (imageLoader == null)
        //    imageLoader = AppController.getInstance().getImageLoader();
        ImageView thumbNail = (ImageView) convertView.findViewById(R.id.MPFotoReceta);

        //ImageView thumbNail = (ImageView) convertView.findViewById(R.id.FotoReceta);
        TextView NombreReceta = (TextView) convertView.findViewById(R.id.MPNombreReceta);
        TextView Categoria = (TextView) convertView.findViewById(R.id.MPCategoria);
        TextView Fecha = (TextView) convertView.findViewById(R.id.MPFecha);
        //TextView year = (TextView) convertView.findViewById(R.id.releaseYear);
        TextView IdReceta = (TextView) convertView.findViewById(R.id.MPIdReceta);

        TextView URL = (TextView) convertView.findViewById(R.id.MPURL);

        // getting Receta data for the row
        Receta m = RecetaItems.get(position);

        String IDRECETA = String.valueOf(m.getIdReceta());
        if(Integer.parseInt(IDRECETA)%2==0)
        {Bitmap myBitmap = BitmapFactory.decodeFile(m.getFotoReceta());
            thumbNail.setImageBitmap(myBitmap);
        }
        else
        {

            Uri Path = Uri.fromFile(new File(m.getFotoReceta()));

            try {
                // hide video preview
                //videoPreview.setVisibility(View.GONE);

                thumbNail.setVisibility(View.VISIBLE);

                // bimatp factory
                BitmapFactory.Options options = new BitmapFactory.Options();

                // downsizing image as it throws OutOfMemory Exception for larger
                // images
                options.inSampleSize = 8;

                //final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(), options);
                final Bitmap bitmap = BitmapFactory.decodeFile(Path.getPath(),options);

                thumbNail.setImageBitmap(bitmap);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }


        // thumbnail image
        //thumbNail.setImageUrl(m.getFotoReceta(), imageLoader);


        // NombreReceta
        NombreReceta.setText(m.getNombreReceta());

        // rating
        Categoria.setText("Categoría: " + String.valueOf(m.getCategoria()));
        // genre
        Fecha.setText("Fecha: " + String.valueOf(m.getFecha()));
        IdReceta.setText(String.valueOf(m.getIdReceta()));


        URL.setText(String.valueOf(m.getURL()));

        //Cargar img de fondo



        return convertView;
    }


}