package com.example.mache.recetario2;


import com.android.volley.toolbox.NetworkImageView;
import com.example.mache.recetario2.adater.CustomListAdapter;
import com.example.mache.recetario2.adater.CustomListAdapterDevice;
import com.example.mache.recetario2.app.AppController;
//Contiene get set de Recetas
import com.example.mache.recetario2.database.AppDatabaseHelper;
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
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

public class BuscarRecetasDeviceActivity extends Activity {

    private static final String TAG = BuscarRecetasDeviceActivity.class.getSimpleName();

    private ProgressDialog pDialog;
    private List<Receta> RecetaList = new ArrayList<Receta>();
    private ListView listView;
    private CustomListAdapterDevice adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_buscar_recetas_device);

        listView = (ListView) findViewById(R.id.Devicelist);
        adapter = new CustomListAdapterDevice(this, RecetaList);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long id) {


                TextView twIdReceta = (TextView) view.findViewById(R.id.DIdReceta);
                TextView twURL = (TextView) view.findViewById(R.id.DURL);
                TextView twNombreReceta = (TextView) view.findViewById(R.id.DNombreReceta);
                TextView twTiempoPreparacion = (TextView) view.findViewById(R.id.DTiempoPreparacion);
                TextView twCategoria = (TextView) view.findViewById(R.id.DCategoria);

                String SIdReceta = twIdReceta.getText().toString();
                String SURL = twURL.getText().toString();
                String SNombreReceta = twNombreReceta.getText().toString();
                String STiempoPreparacion = twTiempoPreparacion.getText().toString();
                String SCategoria = twCategoria.getText().toString();

                //Creamos el Intent
                Intent intent =
                        new Intent(BuscarRecetasDeviceActivity.this, MostrarRecetaDeviceActivity.class);

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

            }
        });


        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Cargando valores desde el dispositivo...");
        pDialog.show();


        //Leyendo datos
        AppDatabaseHelper appDatabaseHelper = new AppDatabaseHelper(getApplicationContext());
        SQLiteDatabase database = appDatabaseHelper.getReadableDatabase();

        Cursor resultados = database.query(AppDatabaseHelper.TABLE_RECETAS,
                null,null,null,null,null,AppDatabaseHelper.COL_IdReceta);
        //Estructura de query()
        //query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit)

        Integer IdReceta;
        String NombreReceta;
        String Categoria;
        Integer TiempoPreparacion;
        String PathReceta;
        //String URL;
        while(resultados.moveToNext()){
            Receta Receta = new Receta();

            IdReceta = resultados.getInt(resultados.getColumnIndex(AppDatabaseHelper.COL_IdReceta));
            NombreReceta = resultados.getString(resultados.getColumnIndex(AppDatabaseHelper.COL_NombreReceta));
            Categoria = resultados.getString(resultados.getColumnIndex(AppDatabaseHelper.COL_Categoria));
            TiempoPreparacion = resultados.getInt(resultados.getColumnIndex(AppDatabaseHelper.COL_TiempoPreparacion));
            PathReceta = resultados.getString(resultados.getColumnIndex(AppDatabaseHelper.COL_FotoReceta));

            Receta.setNombreReceta(NombreReceta);

            //LA FOTO
            Receta.setFotoReceta(PathReceta);
            Receta.setCategoria(Categoria);
            Receta.setIdReceta(IdReceta);
            Receta.setTiempoPraparacion(TiempoPreparacion);

            RecetaList.add(Receta);
        }


        hidePDialog();
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
