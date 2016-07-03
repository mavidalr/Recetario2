package com.example.mache.recetario2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;

import android.widget.Toast;

import android.widget.LinearLayout;
import android.widget.CheckBox;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import android.view.View;

import com.example.mache.recetario2.database.AppDatabaseHelper;


public class MostrarRecetaMenuPersonalActivity extends AppCompatActivity {

    // Progress dialog
    private ProgressDialog pDialog;
    private int IdReceta;
    private int IdMenu;

    //--Para el checkbox
    LinearLayout linearMain;
    CheckBox checkBox;
    //Fin para leer ingredientes


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_receta_menu_personal);

        //--
        //Recuperamos la información pasada en el intent
        Bundle bundle = this.getIntent().getExtras();

        String ID_RECETA = bundle.getString("SIdReceta");
        IdReceta = Integer.parseInt(ID_RECETA);
        String FECHA = bundle.getString("SFecha");
        IdMenu = Integer.parseInt(bundle.getString("SIdMenu"));

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.MPcollapse_toolbar);

        //Nombre receta
        collapsingToolbar.setTitle(bundle.getString("SNombreReceta"));


        //Si IdReceta es par, es img guardada en internal so¿torage Si es impar, es del sdcard
        if(IdReceta%2==0)
        {


            //Cargar img de fondo
            ImageView targetImage = (ImageView)findViewById(R.id.MPImagenFondo);
            targetImage.setImageBitmap(loadImageBitmap(getApplicationContext(), ID_RECETA + ".png"));
        }
        else
        {
            ImageView imgPreview = (ImageView)findViewById(R.id.MPImagenFondo);
            AppDatabaseHelper dbHelper = new AppDatabaseHelper(MostrarRecetaMenuPersonalActivity.this);
            String PathImagen = dbHelper.PathImagenSDCard(ID_RECETA);

            Uri Path = Uri.fromFile(new File(PathImagen));

            try {
                // hide video preview
                //videoPreview.setVisibility(View.GONE);

                imgPreview.setVisibility(View.VISIBLE);

                BitmapFactory.Options options = new BitmapFactory.Options();

                options.inSampleSize = 8;

                //final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(), options);
                final Bitmap bitmap = BitmapFactory.decodeFile(Path.getPath(),options);

                imgPreview.setImageBitmap(bitmap);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

        }
        TextView PrimerCard = (TextView) findViewById(R.id.MPcardUno);
        PrimerCard.setText(FECHA);



        //Editar dinamicamente collapsing toolbar
        collapsingToolbar.setContentScrimColor(getResources().getColor(R.color.colorPrimary));
        collapsingToolbar.setExpandedTitleTextAppearance(R.style.barraExpandida);
        collapsingToolbar.setCollapsedTitleTextAppearance(R.style.barraColapse);


        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Cargando ingredientes e instrucciones");
        pDialog.setCancelable(false);
        linearMain = (LinearLayout) findViewById(R.id.MPlinearMain);

        //para el checkbox
        LinkedHashMap<String, String> alphabet = new LinkedHashMap<String, String>();
        String TextoColumna;

        showpDialog();

        //se hace la consulta:
        //Leyendo datos
        AppDatabaseHelper appDatabaseHelper = new AppDatabaseHelper(getApplicationContext());
        SQLiteDatabase database = appDatabaseHelper.getReadableDatabase();

        String[] args = new String[] {ID_RECETA};

        Cursor resultados = database.query(AppDatabaseHelper.TABLE_INGREDIENTES,
                null,"IdReceta=?",args,null,null,AppDatabaseHelper.COL_IdIngrediente);
        if (resultados.moveToFirst()) {

            String NombreIngrediente;
            String Medicion;
            Double Cantidad;
            int IdIngrediente;
            do {
                //Se obtienen los campos de cada columna
                NombreIngrediente = resultados.getString(resultados.getColumnIndex(AppDatabaseHelper.COL_NombreIngrediente));
                Medicion = resultados.getString(resultados.getColumnIndex(AppDatabaseHelper.COL_Medicion));
                Cantidad = resultados.getDouble(resultados.getColumnIndex(AppDatabaseHelper.COL_Cantidad));
                IdIngrediente = resultados.getInt(resultados.getColumnIndex(AppDatabaseHelper.COL_IdIngrediente));

                if (Medicion.equals("Unidad"))
                {
                    TextoColumna = Cantidad +" "+ NombreIngrediente;
                }
                else
                    TextoColumna = Cantidad +" "+ Medicion +" de "+ NombreIngrediente;

                alphabet.put(String.valueOf(IdIngrediente), TextoColumna);


            } while(resultados.moveToNext());
        }

        Set<?> set = alphabet.entrySet();
        // Get an iterator
        Iterator<?> i = set.iterator();
        // Display elements
        while (i.hasNext()) {
            @SuppressWarnings("rawtypes")
            Map.Entry me = (Map.Entry) i.next();
            checkBox = new CheckBox(MostrarRecetaMenuPersonalActivity.this);
            //checkBox = new CheckBox(this);
            checkBox.setId(Integer.parseInt(me.getKey().toString()));
            checkBox.setText(me.getValue().toString());

            checkBox.setOnClickListener(getOnClickDoSomething(checkBox));
            linearMain.addView(checkBox);
        }

        String TextoInstrucciones = "";
        String TextoInstruccion;
        String[] campos = new String[] {"TextoInstruccion"};

        resultados = database.query(AppDatabaseHelper.TABLE_INSTRUCCIONES,
                campos,"IdReceta=?",args,null,null,AppDatabaseHelper.COL_OrdenInstruccion);


        if (resultados.moveToFirst()) {

            do {
                TextoInstruccion = resultados.getString(resultados.getColumnIndex(AppDatabaseHelper.COL_TextoInstruccion));

                TextoInstrucciones += TextoInstruccion + "\n\n";

            } while(resultados.moveToNext());
        }
        TextoInstrucciones += "\n\n";
        TextView txtResponse = (TextView) findViewById(R.id.MPcardInstrucciones);
        txtResponse.setText(TextoInstrucciones);

        hidepDialog();




        //makeJsonArrayRequestInstrucciones();


        //-- Fin para leer ingredientes


        final Button btnBorrarMenuPersonal = (Button)findViewById(R.id.MPbtnBorrarMenuPersonal);
        btnBorrarMenuPersonal.setOnClickListener(new Button.OnClickListener(){


            @Override
            public void onClick(View arg0) {
                AppDatabaseHelper dbHelper = new AppDatabaseHelper(MostrarRecetaMenuPersonalActivity.this);
                dbHelper.EliminarRecetaMenuPersonal(IdMenu);

                View b = findViewById(R.id.MPbtnBorrarMenuPersonal);
                b.setVisibility(View.GONE);



                Toast.makeText(MostrarRecetaMenuPersonalActivity.this,
                        "Receta eliminada del menú personal",
                        Toast.LENGTH_SHORT).show();


            }});

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, BuscarRecetasMenuPersonalActivity.class));
    }


    View.OnClickListener getOnClickDoSomething(final Button button) {
        return new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("*************Id******" + button.getId());
                System.out.println("Text***" + button.getText().toString());
            }
        };
    }


    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


    public Bitmap loadImageBitmap(Context context, String imageName) {
        Bitmap bitmap = null;
        FileInputStream fiStream;
        try {
            fiStream    = context.openFileInput(imageName);
            bitmap      = BitmapFactory.decodeStream(fiStream);
            fiStream.close();
        } catch (Exception e) {
            System.out.println("saveImage"+ "Exception 3, Something went wrong!");
            e.printStackTrace();
        }
        return bitmap;
    }



}


