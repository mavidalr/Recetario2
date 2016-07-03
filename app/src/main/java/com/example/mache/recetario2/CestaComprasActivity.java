package com.example.mache.recetario2;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.mache.recetario2.database.AppDatabaseHelper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class CestaComprasActivity extends AppCompatActivity {

    LinearLayout CClinearLayout;
    CheckBox checkBox;
    ArrayList<Integer> G_IdElementosSeleccionados = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cesta_compras);

        LinkedHashMap<String, String> alphabet = new LinkedHashMap<String, String>();
        Set<?> set = alphabet.entrySet();

        CClinearLayout = (LinearLayout) findViewById(R.id.CCCheckBox);

        //se hace la consulta:
        //Leyendo datos
        AppDatabaseHelper appDatabaseHelper = new AppDatabaseHelper(getApplicationContext());
        SQLiteDatabase database = appDatabaseHelper.getReadableDatabase();
        String TextoCheckbox="";

        Cursor resultados = database.query(AppDatabaseHelper.TABLE_CESTA, null, null, null, null, null, appDatabaseHelper.COL_NombreIngredienteCesta, null);

        //Se recorre el arreglo de ingredientes:
        //Nos aseguramos de que existe al menos un registro
        if (resultados.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            String NombreIngrediente;
            String Medicion;
            Double Cantidad;
            int IdElementoCesta;
            do {
                //Se obtienen los campos de cada columna
                NombreIngrediente = resultados.getString(resultados.getColumnIndex(AppDatabaseHelper.COL_NombreIngredienteCesta));
                Medicion = resultados.getString(resultados.getColumnIndex(AppDatabaseHelper.COL_MedicionCesta));
                Cantidad = resultados.getDouble(resultados.getColumnIndex(AppDatabaseHelper.COL_CantidadCesta));
                IdElementoCesta = resultados.getInt(resultados.getColumnIndex(AppDatabaseHelper.COL_IdElemento));

                if (Medicion.equals("Unidad"))
                {
                    TextoCheckbox = Cantidad +" "+ NombreIngrediente;
                }
                else
                    TextoCheckbox = Cantidad +" "+ Medicion +" de "+ NombreIngrediente;

                alphabet.put(String.valueOf(IdElementoCesta), TextoCheckbox);


            } while(resultados.moveToNext());
        }

        Iterator<?> i = set.iterator();
        // Display elements
        while (i.hasNext()) {
            @SuppressWarnings("rawtypes")
            Map.Entry me = (Map.Entry) i.next();

            checkBox = new CheckBox(CestaComprasActivity.this);
            //checkBox = new CheckBox(this);
            checkBox.setId(Integer.parseInt(me.getKey().toString()));
            checkBox.setText(me.getValue().toString());

            checkBox.setOnClickListener(getOnClickDoSomething(checkBox));
            CClinearLayout.addView(checkBox);
        }

        Button btnEliminarSeleccionados = (Button) findViewById(R.id.btnCCEliminarSelect);
        Button btnEliminarTodos = (Button) findViewById(R.id.btnEliminarTodos);

        btnEliminarSeleccionados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Eliminar de BDD
                AppDatabaseHelper appDatabaseHelper = new AppDatabaseHelper(getApplicationContext());
                SQLiteDatabase database = appDatabaseHelper.getReadableDatabase();
                String Consulta = "";
                for(int x=0; x<G_IdElementosSeleccionados.size();x++) {
                    Consulta = "DELETE FROM Cesta WHERE IdElemento = " +G_IdElementosSeleccionados.get(x);
                    database.execSQL(Consulta);

                }
                //Vaciar arraylist G_IdElementosSeleccionados
                G_IdElementosSeleccionados.clear();

                Toast.makeText(CestaComprasActivity.this,
                        "Productos eliminados",
                        Toast.LENGTH_SHORT).show();

                Intent intent = getIntent();
                finish();
                startActivity(intent);

            }
        });


        btnEliminarTodos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Eliminar de BDD

                AppDatabaseHelper appDatabaseHelper = new AppDatabaseHelper(getApplicationContext());
                SQLiteDatabase database = appDatabaseHelper.getReadableDatabase();
                database.delete(appDatabaseHelper.TABLE_CESTA, null, null);

                //Vaciar arraylist G_IdElementosSeleccionados
                G_IdElementosSeleccionados.clear();

                Toast.makeText(CestaComprasActivity.this,
                        "Productos eliminados",
                        Toast.LENGTH_SHORT).show();

                Intent intent = getIntent();
                finish();
                startActivity(intent);

            }
        });
    }

    //Funcion que obtiene los valores seleccionados del checkbox
    View.OnClickListener getOnClickDoSomething(final Button button) {
        return new View.OnClickListener() {
            public void onClick(View v) {

                //Almacenar IdIngrediente a la cesta

                if(G_IdElementosSeleccionados.contains(button.getId()))

                {
                    G_IdElementosSeleccionados.remove(G_IdElementosSeleccionados.indexOf(button.getId()));


                }
                else
                {
                    G_IdElementosSeleccionados.add(button.getId());
                }


            }
        };
    }
}
