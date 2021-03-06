package com.example.mache.recetario2;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.MenuItem;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    //--para el slide
    private static RecyclerView recyclerView;

    //String and Integer array for Recycler View Items
    public static final String[] TITLES= {"Ver +","Ver +","Ver +","Ver +","Ver +"};
    public static final Integer[] IMAGES= {R.drawable.uno,R.drawable.dos,R.drawable.tres,R.drawable.uno,R.drawable.dos};

    //nuevo
    public static final String[] CATEGORIAS= {"Sandwiches","Postres","Fondos","Sandwiches","Postres"};

    private static String navigateFrom;//String to get Intent Value
    //--

    String[] opciones = {
            "Buscar recetas",
            "Ver recetas guardadas",
            "Cesta de compras",
            "Menú personal",
            "Crear receta"};

    private static final int OPCION_BUSCAR_RECETAS = 0;
    private static final int OPCION_VER_RECETAS_GUARDADAS = 1;
    private static final int OPCION_CESTA_DE_COMPRAS = 2;
    private static final int OPCION_MENU_PERSONAL = 3;
    private static final int OPCION_CREAR_RECETA = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        initViews();
        populatRecyclerView();
        setListViewOpciones();
    }

    private void setListViewOpciones() {

        ListView listViewOpciones = (ListView) findViewById(R.id.listViewOpciones);

        ArrayAdapter<String> adapterOpciones =
                new ArrayAdapter<String>(this,
                        R.layout.fila_menu,
                        opciones);

        listViewOpciones.setAdapter(adapterOpciones);


        listViewOpciones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long id) {

                Intent intent;

                switch (position){
                    case OPCION_BUSCAR_RECETAS:
                        intent = new Intent(MainActivity.this,
                                BuscarRecetasActivity.class);
                        startActivity(intent);
                        break;

                    case OPCION_VER_RECETAS_GUARDADAS:

                        intent = new Intent(MainActivity.this,
                                BuscarRecetasDeviceActivity.class);
                        startActivity(intent);
                        break;

                    case OPCION_CREAR_RECETA:
                        intent = new Intent(MainActivity.this,
                                CrearRecetaActivity.class);
                        startActivity(intent);
                        break;
                    case OPCION_CESTA_DE_COMPRAS:
                        intent = new Intent(MainActivity.this,
                                CestaComprasActivity.class);
                        startActivity(intent);
                        break;
                    case OPCION_MENU_PERSONAL:
                        intent = new Intent(MainActivity.this,
                                BuscarRecetasMenuPersonalActivity.class);
                        startActivity(intent);
                        break;

                }
            }
        });

    }

    private void initViews() {

        navigateFrom = "horizontal";
        recyclerView = (RecyclerView)
                findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        if (navigateFrom.equals("horizontal")) {
            getSupportActionBar().setTitle("Recetario || Menú principal");
            recyclerView
                    .setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
        } else {
            getSupportActionBar().setTitle("Staggered GridLayout Manager");
            recyclerView
                    .setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));// Here 2 is no. of columns to be displayed

        }
    }

    private void populatRecyclerView() {
        ArrayList<Data_Model> arrayList = new ArrayList<>();
        for (int i = 0; i < TITLES.length; i++) {
            arrayList.add(new Data_Model(TITLES[i],IMAGES[i], CATEGORIAS[i]));
        }
        RecyclerView_Adapter  adapter = new RecyclerView_Adapter(MainActivity.this, arrayList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
