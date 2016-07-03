package com.example.mache.recetario2;



import com.example.mache.recetario2.adater.CustomListAdapter;
import com.example.mache.recetario2.app.AppController;
//Contiene get set de Recetas
import com.example.mache.recetario2.model.Receta;


import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;

import android.os.Bundle;
import android.util.Log;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

public class BuscarRecetasActivity extends Activity {
    // Log tag
    private static final String TAG = BuscarRecetasActivity.class.getSimpleName();

    // Recetas json url
    private static final String url = "https://dl.dropboxusercontent.com/u/3194177/Taller/BDDOnline.json";
    private ProgressDialog pDialog;
    private List<Receta> RecetaList = new ArrayList<Receta>();
    private ListView listView;
    private CustomListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_buscar_recetas);

        listView = (ListView) findViewById(R.id.list);
        adapter = new CustomListAdapter(this, RecetaList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long id) {

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

                //Añadimos la información al intent
                intent.putExtras(b);

                //Iniciamos la nueva actividad
                startActivity(intent);
            }
        });


        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Cargando...");
        pDialog.show();

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

                                RecetaList.add(Receta);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                hidePDialog();

            }
        });

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
}
