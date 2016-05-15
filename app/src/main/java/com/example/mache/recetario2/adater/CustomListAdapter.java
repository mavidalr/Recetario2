package com.example.mache.recetario2.adater;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import com.example.mache.recetario2.R;
import com.example.mache.recetario2.app.AppController;
import com.example.mache.recetario2.model.Receta;

public class CustomListAdapter extends BaseAdapter {
	private Activity activity;
	private LayoutInflater inflater;
	private List<Receta> RecetaItems;
	ImageLoader imageLoader = AppController.getInstance().getImageLoader();

	public CustomListAdapter(Activity activity, List<Receta> RecetaItems) {
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
			convertView = inflater.inflate(R.layout.list_row, null);

		if (imageLoader == null)
			imageLoader = AppController.getInstance().getImageLoader();
		NetworkImageView thumbNail = (NetworkImageView) convertView
				.findViewById(R.id.FotoReceta);
		TextView NombreReceta = (TextView) convertView.findViewById(R.id.NombreReceta);
		TextView Categoria = (TextView) convertView.findViewById(R.id.Categoria);
		TextView TiempoPreparacion = (TextView) convertView.findViewById(R.id.TiempoPreparacion);
		//TextView year = (TextView) convertView.findViewById(R.id.releaseYear);
		TextView IdReceta = (TextView) convertView.findViewById(R.id.IdReceta);

        TextView URL = (TextView) convertView.findViewById(R.id.URL);

		// getting Receta data for the row
        Receta m = RecetaItems.get(position);

		// thumbnail image
		thumbNail.setImageUrl(m.getFotoReceta(), imageLoader);
		
		// NombreReceta
        NombreReceta.setText(m.getNombreReceta());
		
		// rating
        Categoria.setText("Categoría: " + String.valueOf(m.getCategoria()));
        // genre
        TiempoPreparacion.setText("Praparación: " + String.valueOf(m.getTiempoPraparacion()) + " min.");
        IdReceta.setText(String.valueOf(m.getIdReceta()));

        URL.setText(String.valueOf(m.getURL()));

		
		// genre
		/*String genreStr = "";
		for (String str : m.getGenre()) {
			genreStr += str + ", ";
		}
		genreStr = genreStr.length() > 0 ? genreStr.substring(0,
				genreStr.length() - 2) : genreStr;
		genre.setText(genreStr);
		*/
		// release year
		//year.setText(String.valueOf(m.getYear()));

		return convertView;
	}

}