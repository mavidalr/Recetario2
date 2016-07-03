package com.example.mache.recetario2;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mache.recetario2.adater.CustomListAdapterMenuPersonal;
import com.example.mache.recetario2.database.AppDatabaseHelper;
import com.example.mache.recetario2.model.Receta;

import java.util.ArrayList;
import java.util.List;

public class BuscarRecetasMenuPersonalActivity extends Activity {
    // Log tag
    private static final String TAG = BuscarRecetasMenuPersonalActivity.class.getSimpleName();

    private ProgressDialog pDialog;
    private List<Receta> RecetaList = new ArrayList<Receta>();
    private ListView listView;
    private CustomListAdapterMenuPersonal adapter;
    private String IdMenuIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_recetas_menu_personal);

        listView = (ListView) findViewById(R.id.MenuPlist);
        adapter = new CustomListAdapterMenuPersonal(this, RecetaList);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long id) {

                TextView twIdReceta = (TextView) view.findViewById(R.id.MPIdReceta);
                TextView twURL = (TextView) view.findViewById(R.id.MPURL);
                TextView twNombreReceta = (TextView) view.findViewById(R.id.MPNombreReceta);
                TextView twFecha = (TextView) view.findViewById(R.id.MPFecha);
                TextView twCategoria = (TextView) view.findViewById(R.id.MPCategoria);

                String SIdReceta = twIdReceta.getText().toString();
                String SURL = twURL.getText().toString();
                String SNombreReceta = twNombreReceta.getText().toString();
                String SFecha = twFecha.getText().toString();
                String SCategoria = twCategoria.getText().toString();

                //Creamos el Intent
                Intent intent =
                        new Intent(BuscarRecetasMenuPersonalActivity.this, MostrarRecetaMenuPersonalActivity.class);

                //Creamos la información a pasar entre actividades
                Bundle b = new Bundle();
                b.putString("SIdReceta", SIdReceta);
                b.putString("SURL", SURL);
                b.putString("SNombreReceta", SNombreReceta);
                b.putString("SFecha", SFecha);
                b.putString("SCategoria", SCategoria);
                b.putString("SIdMenu", IdMenuIntent);
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


        //Estructura de query()
        //query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit)

        Integer IdReceta;
        String NombreReceta;
        String Categoria;
        //Integer TiempoPreparacion;
        String PathReceta;
        ArrayList<String> G_IdRecetas = new ArrayList<String>();
        ArrayList<String> G_IdMenus = new ArrayList<String>();
        ArrayList<String> G_Anios = new ArrayList<String>();
        ArrayList<String> G_Meses = new ArrayList<String>();
        ArrayList<String> G_Dias = new ArrayList<String>();
        ArrayList<String> G_Fechas = new ArrayList<String>();
        String Fecha, SetearFecha;
        int Posicion;

        G_Fechas = appDatabaseHelper.listaMenuPersonal();

        for (int x = 0; x < G_Fechas.size(); x++) {
            Fecha = G_Fechas.get(x);
            String[] Split = Fecha.split("-");
            //Fechas.add(IDMENU+"-"+IDRECETA+"-"+Dia+"-"+Mes+"-"+Anio);
            G_IdMenus.add(Split[0]);
            G_IdRecetas.add(Split[1]);
            G_Dias.add(Split[2]);
            G_Meses.add(Split[3]);
            G_Anios.add(Split[4]);
        }

        for(int y=0; y<G_IdRecetas.size();y++)
        {
            Cursor resultados = database.rawQuery("SELECT * from Recetas where IdReceta = " +G_IdRecetas.get(y), null);
            if (resultados.moveToFirst()) {
                Receta Receta = new Receta();

                IdReceta = resultados.getInt(resultados.getColumnIndex(AppDatabaseHelper.COL_IdReceta));

                //Si la receta del dispositivo esta en el menu
                if (G_IdRecetas.contains(Integer.toString(IdReceta))) {


                    //Obtener la posicion de la receta en la listaMenu
                    Posicion = G_IdRecetas.indexOf(Integer.toString(IdReceta));
                    //Agregar datos al objeto Receta

                    NombreReceta = resultados.getString(resultados.getColumnIndex(AppDatabaseHelper.COL_NombreReceta));
                    Categoria = resultados.getString(resultados.getColumnIndex(AppDatabaseHelper.COL_Categoria));
                    //SetearFecha = resultados.getInt(resultados.getColumnIndex(AppDatabaseHelper.COL_TiempoPreparacion));
                    SetearFecha = G_Dias.get(Posicion) + "/" + G_Meses.get(Posicion) + "/" + G_Anios.get(Posicion);
                    PathReceta = resultados.getString(resultados.getColumnIndex(AppDatabaseHelper.COL_FotoReceta));

                    //Setear Valores
                    Receta.setNombreReceta(NombreReceta);
                    //LA FOTO
                    Receta.setFotoReceta(PathReceta);
                    Receta.setCategoria(Categoria);
                    Receta.setIdReceta(IdReceta);
                    Receta.setFecha(SetearFecha);
                    Receta.setURL(G_IdMenus.get(Posicion));
                    IdMenuIntent = G_IdMenus.get(Posicion);

                    RecetaList.add(Receta);


                }
                //Si no
                //No entra al array
            }
            hidePDialog();

        }
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

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
    }

}
