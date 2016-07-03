package com.example.mache.recetario2;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.File;
import java.io.FileInputStream;
import android.widget.Toast;
import android.widget.LinearLayout;
import android.widget.CheckBox;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import android.view.View;
import java.util.ArrayList;

import com.example.mache.recetario2.database.AppDatabaseHelper;


public class MostrarRecetaDeviceActivity extends AppCompatActivity {

    private static String TAG = MostrarRecetaDeviceActivity.class.getSimpleName();
    // Progress dialog
    private ProgressDialog pDialog;
    private int IdReceta;

    private int mYear, mMonth, mDay;
    private int TipoCesta=1;
    private String TextoCesta="Default";
    private String TipoMenu="Default";

    //--Para el checkbox
    LinearLayout linearMain;
    CheckBox checkBox;
    //Fin para leer ingredientes

    //para el boton savve
    private int G_IdReceta;
    private String G_NombreReceta;
    private String G_FotoReceta;
    private int G_NumPersonas;
    private int G_TiempoPreparacion;
    private String G_Categoria;
    private int G_Dificulad;

    private String G_Path;

    ArrayList<Integer> G_IdIngrediente = new ArrayList<Integer>();
    ArrayList<String> G_NombreIngrediente = new ArrayList<String>();
    ArrayList<String> G_Medicion = new ArrayList<String>();
    ArrayList<Double> G_Cantidad = new ArrayList<Double>();

    ArrayList<Integer> G_IdIngredienteCesta = new ArrayList<Integer>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_receta_device);

        //--
        //Recuperamos la información pasada en el intent
        Bundle bundle = this.getIntent().getExtras();

        String ID_RECETA = bundle.getString("SIdReceta");
        IdReceta = Integer.parseInt(ID_RECETA);
        String TIEMPO = bundle.getString("STiempoPreparacion");
        String CATEGORIA = bundle.getString("SCategoria");



        String delims = "[ ]";
        String[] tokens = TIEMPO.split(delims);

        int Tiempo = Integer.parseInt(tokens[1]);
        tokens = CATEGORIA.split(delims);

        G_IdReceta = IdReceta;
        G_NombreReceta = bundle.getString("SNombreReceta");
        G_FotoReceta = bundle.getString("SURL");
        G_NumPersonas = 0;
        G_TiempoPreparacion = Tiempo;
        G_Categoria = tokens[1];
        G_Dificulad = 0;

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.Dcollapse_toolbar);

        //Nombre receta
        collapsingToolbar.setTitle(bundle.getString("SNombreReceta"));

        //Si IdReceta es par, es img guardada en internal so¿torage Si es impar, es del sdcard
        if(IdReceta%2==0)
        {
            //Cargar img de fondo
            ImageView targetImage = (ImageView)findViewById(R.id.DImagenFondo);
            targetImage.setImageBitmap(loadImageBitmap(getApplicationContext(), ID_RECETA + ".png"));
        }
        else
        {
            ImageView imgPreview = (ImageView)findViewById(R.id.DImagenFondo);
            AppDatabaseHelper dbHelper = new AppDatabaseHelper(MostrarRecetaDeviceActivity.this);
            String PathImagen = dbHelper.PathImagenSDCard(ID_RECETA);

            Uri Path = Uri.fromFile(new File(PathImagen));

            try {
                imgPreview.setVisibility(View.VISIBLE);

                // bimatp factory
                BitmapFactory.Options options = new BitmapFactory.Options();

                options.inSampleSize = 8;
                final Bitmap bitmap = BitmapFactory.decodeFile(Path.getPath(),options);

                imgPreview.setImageBitmap(bitmap);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

        }
        TextView PrimerCard = (TextView) findViewById(R.id.DcardUno);
        PrimerCard.setText("Categoría: "+tokens[1] + "\n\n" +"Tiempo de preparación: "+ Tiempo + " min.");

        collapsingToolbar.setContentScrimColor(getResources().getColor(R.color.colorPrimary));
        collapsingToolbar.setExpandedTitleTextAppearance(R.style.barraExpandida);
        collapsingToolbar.setCollapsedTitleTextAppearance(R.style.barraColapse);


        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Cargando ingredientes e instrucciones");
        pDialog.setCancelable(false);
        linearMain = (LinearLayout) findViewById(R.id.DlinearMain);

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
        //Estructura de query()
        //query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit)

        //Se recorre el arreglo de ingredientes:
        //Nos aseguramos de que existe al menos un registro
        if (resultados.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
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
            checkBox = new CheckBox(MostrarRecetaDeviceActivity.this);
            //checkBox = new CheckBox(this);
            checkBox.setId(Integer.parseInt(me.getKey().toString()));
            checkBox.setText(me.getValue().toString());


            //checkBox.setTextSize(TypedValue.COMPLEX_UNIT_PT, 10);


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
        TextView txtResponse = (TextView) findViewById(R.id.DcardInstrucciones);
        txtResponse.setText(TextoInstrucciones);

        hidepDialog();


        final Button btnAgregarMenuPersonal = (Button)findViewById(R.id.DevicebtnMenuPersonal);

        btnAgregarMenuPersonal.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View arg0) {

                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(MostrarRecetaDeviceActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {


                                //Recuperar IDingredientes que se seleccionan del checkbox

                                AppDatabaseHelper dbHelper = new AppDatabaseHelper(MostrarRecetaDeviceActivity.this);
                                int IdMenuAInsertar = dbHelper.IdMenuAInsertar();
                                int IdCestaAInsertar = dbHelper.IdCestaAInsertar();

                                for(int y=0;y<G_IdIngredienteCesta.size();y++)
                                {
                                    int posicionEnG_IdIngrediente = G_IdIngrediente.indexOf(G_IdIngredienteCesta.get(y));
                                    dbHelper.InsertarCesta(IdCestaAInsertar+y,G_NombreIngrediente.get(posicionEnG_IdIngrediente),G_Medicion.get(posicionEnG_IdIngrediente),G_Cantidad.get(posicionEnG_IdIngrediente),TipoCesta,TextoCesta);

                                }

                                dbHelper.InsertarMenu(IdMenuAInsertar,G_IdReceta,dayOfMonth,monthOfYear,year,TipoMenu);

                                Toast.makeText(MostrarRecetaDeviceActivity.this,
                                        "Receta agregada al menú personal",
                                        Toast.LENGTH_SHORT).show();


                            }
                        }, mYear, mMonth, mDay);


                datePickerDialog.show();






            }});

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
    //-- Fin para leer ingredientes


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


