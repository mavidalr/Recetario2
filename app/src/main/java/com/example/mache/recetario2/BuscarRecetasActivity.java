package com.example.mache.recetario2;


import com.android.volley.toolbox.NetworkImageView;
import com.example.mache.recetario2.adater.CustomListAdapter;
import com.example.mache.recetario2.app.AppController;
//Contiene get set de Recetas
import com.example.mache.recetario2.model.Receta;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import junit.framework.Test;

public class BuscarRecetasActivity extends Activity {
    // Log tag
    private static final String TAG = BuscarRecetasActivity.class.getSimpleName();

    // Recetas json url
   // private static final String url = "http://api.androidhive.info/json/movies.json";

    private static final String url = "https://dl.dropboxusercontent.com/u/3194177/Taller/BDDOnline.json";
    private ProgressDialog pDialog;
    private List<Receta> RecetaList = new ArrayList<Receta>();
    private ListView listView;
    private CustomListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getActionBar().setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_buscar_recetas);

        listView = (ListView) findViewById(R.id.list);
        adapter = new CustomListAdapter(this, RecetaList);
        listView.setAdapter(adapter);

        /*
        TextView textViewNombreUsuario =
                (TextView) findViewById(R.id.Ver);

        textViewNombreUsuario.setText("Holiwi");
        */

        //awesome font
        Typeface fontAwesomeFont = Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf");
        TextView fontAwesomeAndroidIcon = (TextView) findViewById(R.id.Ver);
        fontAwesomeAndroidIcon.setTypeface(fontAwesomeFont);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long id) {

                //Intent intent;

                //TextView textViewNombreUsuario2 =
                //        (TextView) findViewById(R.id.Ver);

                TextView twIdReceta = (TextView) view.findViewById(R.id.IdReceta);
                TextView twURL = (TextView) view.findViewById(R.id.URL);
                TextView twNombreReceta = (TextView) view.findViewById(R.id.NombreReceta);
                TextView twTiempoPreparacion = (TextView) view.findViewById(R.id.TiempoPreparacion);
                TextView twCategoria = (TextView) view.findViewById(R.id.Categoria);

                String SIdReceta = twIdReceta.getText().toString();
                String SURL = twURL.getText().toString();
                String SNombreReceta = twNombreReceta.getText().toString();
                String STiempoPreparacion = twTiempoPreparacion.getText().toString();
                String SCategoria = twCategoria.getText().toString();


                //System.out.println("IdReceta = : " + IdReceta);
                //System.out.println("la URL = : " + URL);
                //System.out.println("Url = : " + imgUrl);


                /*
                BitmapDrawable bd = (BitmapDrawable) ((NetworkImageView) view.findViewById(R.id.FotoReceta))
                        .getDrawable();
                Bitmap bitmap=bd.getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bd.getBitmap().compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] imgByte = baos.toByteArray();

                Intent intent = new Intent(BuscarRecetasActivity.this, MostrarRecetaActivity.class);
                intent.putExtra("image", imgByte);
                // any other extra you need to pass
                //startActivity(intent);
                */


                //Creamos el Intent
                Intent intent =
                        new Intent(BuscarRecetasActivity.this, MostrarRecetaActivity.class);

                //Creamos la información a pasar entre actividades
                Bundle b = new Bundle();
                b.putString("SIdReceta", SIdReceta);
                b.putString("SURL", SURL);
                b.putString("SNombreReceta", SNombreReceta);
                b.putString("STiempoPreparacion", STiempoPreparacion);
                b.putString("SCategoria", SCategoria);
                //b.putString("FotoReceta", imgUrl);

                //Añadimos la información al intent
                intent.putExtras(b);

                //Iniciamos la nueva actividad
                startActivity(intent);


/*
                switch (position){
                    case 0:
                        intent = new Intent(BuscarRecetasActivity.this,
                                MostrarRecetaActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        textViewNombreUsuario2.setText("asddf");
                        break;
                }
                */
            }
        });


        pDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Cargando...");
        pDialog.show();



        // changing action bar color
        //getActionBar().setBackgroundDrawable(
        //      new ColorDrawable(Color.parseColor("#1b1b1b")));

        // Creating volley request obj
        JsonArrayRequest RecetaReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        hidePDialog();

                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                JSONObject obj = response.getJSONObject(i);
                                Receta Receta = new Receta();
                                Receta.setNombreReceta(obj.getString("NombreReceta"));
                                Receta.setFotoReceta(obj.getString("FotoReceta"));
                                Receta.setCategoria(obj.getString("Categoria"));
                                //Receta.setRating(((Number) obj.get("rating"))
                                //        .doubleValue());
                                Receta.setIdReceta(obj.getInt("IdReceta"));
                                Receta.setTiempoPraparacion(obj.getInt("TiempoPreparacion"));

                                Receta.setURL(obj.getString("FotoReceta"));

                                /*
                                // Genre is json array
                                JSONArray genreArry = obj.getJSONArray("genre");
                                ArrayList<String> genre = new ArrayList<String>();
                                for (int j = 0; j < genreArry.length(); j++) {
                                    genre.add((String) genreArry.get(j));
                                }
                                Receta.setGenre(genre);
                                */
                                // adding Receta to Recetas array
                                RecetaList.add(Receta);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                hidePDialog();

            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(RecetaReq);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
*/
}
