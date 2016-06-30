package com.example.mache.recetario2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.net.http.SslCertificate;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.example.mache.recetario2.app.AppController;

import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONArray;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

//para cargar Ingredientes
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import android.widget.LinearLayout;
import android.widget.CheckBox;


import java.net.URLConnection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import java.util.ArrayList;



import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;



import com.example.mache.recetario2.database.AppDatabaseHelper;


public class MostrarRecetaDeviceActivity extends AppCompatActivity {

    //--Para leer Ingredientes
    // json array response url
    private String urlJsonArry = "https://dl.dropboxusercontent.com/u/3194177/Taller/BDDOnlineIngredientes.json";
    private String urlJsonArryInstrucciones = "https://dl.dropboxusercontent.com/u/3194177/Taller/BDDOnlineInstrucciones.json";
    private String urlJsonObj = "http://api.androidhive.info/volley/person_object.json";
    private static String TAG = MostrarRecetaDeviceActivity.class.getSimpleName();
    private Button btnMakeObjectRequest, btnMakeArrayRequest;
    // Progress dialog
    private ProgressDialog pDialog;
    private TextView txtResponse;
    // temporary string to show the parsed response
    private String jsonResponse;
    private int IdReceta;

    //--Para el checkbox
    LinearLayout linearMain;
    CheckBox checkBox;
    //Fin para leer ingredientes


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
        //System.out.println(tokens[1]);
        int Tiempo = Integer.parseInt(tokens[1]);
        tokens = CATEGORIA.split(delims);

        //Construimos el mensaje a mostrar
        //txtSaludo.setText("Hola " + bundle.getString("NOMBRE"));
        System.out.println("Se recibe Id = : " + bundle.getString("SIdReceta"));
        System.out.println("Se recibe Id = : " + bundle.getString("SURL"));
        System.out.println("Se recibe Id = : " + bundle.getString("SNombreReceta"));
        System.out.println("Se recibe Id = : " + bundle.getString("STiempoPreparacion"));
        System.out.println("Se recibe Id = : " + bundle.getString("SCategoria"));
        //--


        //final Toolbar toolbar = (Toolbar) findViewById(R.id.DMyToolbar);
        //setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.Dcollapse_toolbar);
        //collapsingToolbar.setTitle("My Toolbar Tittle");

        //Nombre receta
        collapsingToolbar.setTitle(bundle.getString("SNombreReceta"));

        /*
        //para agregar el fondo
        ImageView targetImage = (ImageView)findViewById(R.id.DImagenFondo);

        //Load bitmap from internet
        //String onLineImage = "https://www.meals.com/ImagesRecipes/146232lrg.jpg";
        String onLineImage = bundle.getString("SURL");
        URL onLineURL;

        try {
            onLineURL = new URL(onLineImage);
            new MyNetworkTask(targetImage).execute(onLineURL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        */

        //Si IdReceta es par, es img guardada en internal so¿torage Si es impar, es del sdcard
        if(IdReceta%2==0)
        {


        //Cargar img de fondo
        ImageView targetImage = (ImageView)findViewById(R.id.DImagenFondo);
        System.out.println(loadImageBitmap(getApplicationContext(), ID_RECETA + ".png"));
        targetImage.setImageBitmap(loadImageBitmap(getApplicationContext(), ID_RECETA + ".png"));
        }
        else
        {
            ImageView imgPreview = (ImageView)findViewById(R.id.DImagenFondo);
            AppDatabaseHelper dbHelper = new AppDatabaseHelper(MostrarRecetaDeviceActivity.this);
            String PathImagen = dbHelper.PathImagenSDCard(ID_RECETA);
            System.out.println(PathImagen);

            Uri Path = Uri.fromFile(new File(PathImagen));

            try {
                // hide video preview
                //videoPreview.setVisibility(View.GONE);

                imgPreview.setVisibility(View.VISIBLE);

                // bimatp factory
                BitmapFactory.Options options = new BitmapFactory.Options();

                // downsizing image as it throws OutOfMemory Exception for larger
                // images
                options.inSampleSize = 8;

                //final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(), options);
                final Bitmap bitmap = BitmapFactory.decodeFile(Path.getPath(),options);

                imgPreview.setImageBitmap(bitmap);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

        }
        TextView PrimerCard = (TextView) findViewById(R.id.DcardUno);
        PrimerCard.setText("Categoría: "+tokens[1] + "\n\n" +"Tiempo de preparación: "+ Tiempo + " min.");



        //Editar dinamicamente collapsing toolbar
        collapsingToolbar.setContentScrimColor(getResources().getColor(R.color.colorPrimary));
        collapsingToolbar.setExpandedTitleTextAppearance(R.style.barraExpandida);
        collapsingToolbar.setCollapsedTitleTextAppearance(R.style.barraColapse);

        //LLenar la otra info

        //-- Para leer ingredientes
        //btnMakeObjectRequest = (Button) findViewById(R.id.btnObjRequest);
        //btnMakeArrayRequest = (Button) findViewById(R.id.btnArrayRequest);
        //txtResponse = (TextView) findViewById(R.id.cardIngredientes);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Cargando ingredientes e instrucciones");
        pDialog.setCancelable(false);

        //makeJsonArrayRequest();
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
            System.out.println("EL IF");
            //Recorremos el cursor hasta que no haya más registros
            String NombreIngrediente;
            String Medicion;
            Double Cantidad;
            int IdIngrediente;
            do {
                System.out.println("Hay ingredientes");
                //Se obtienen los campos de cada columna
                NombreIngrediente = resultados.getString(resultados.getColumnIndex(AppDatabaseHelper.COL_NombreIngrediente));
                Medicion = resultados.getString(resultados.getColumnIndex(AppDatabaseHelper.COL_Medicion));
                Cantidad = resultados.getDouble(resultados.getColumnIndex(AppDatabaseHelper.COL_Cantidad));
                IdIngrediente = resultados.getInt(resultados.getColumnIndex(AppDatabaseHelper.COL_IdIngrediente));

                if (Medicion.equals("Unidad"))
                {
                    //System.out.println("Holi, soy una unidad de ingrediente");
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
            System.out.print(me.getKey() + ": ");
            System.out.println(me.getValue());
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




        //makeJsonArrayRequestInstrucciones();


        //-- Fin para leer ingredientes

    }
    //-- Para leer ingredientes

    //funcion para guardar url imagen


    /**
     * Method to make json array request where response starts with [
     * */
    private void makeJsonArrayRequest() {
        linearMain = (LinearLayout) findViewById(R.id.linearMain);

        showpDialog();

        JsonArrayRequest req = new JsonArrayRequest(urlJsonArry,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                        try {
                            LinkedHashMap<String, String> alphabet = new LinkedHashMap<String, String>();
                            // Parsing json array response
                            // loop through each json object
                            jsonResponse = "";
                            for (int i = 0; i < response.length(); i++) {

                                JSONObject ingredientes = (JSONObject) response
                                        .get(i);

                                int IdRecetaJson = ingredientes.getInt("IdReceta");

                                System.out.println("json" + IdRecetaJson);
                                System.out.println("intent" + IdReceta);

                                if(IdReceta == IdRecetaJson) {
                                    String NombreIngrediente = ingredientes.getString("NombreIngrediente");
                                    String Medicion = ingredientes.getString("Medicion");
                                    Double Cantidad = ingredientes.getDouble("Cantidad");
                                    int IdIngrediente = ingredientes.getInt("IdIngrediente");


                                    if (Medicion.equals("Unidad"))
                                    {
                                        //System.out.println("Holi, soy una unidad de ingrediente");
                                        jsonResponse = Cantidad +" "+ NombreIngrediente;
                                    }
                                    else
                                        jsonResponse = Cantidad +" "+ Medicion +" de "+ NombreIngrediente;

                                    alphabet.put(String.valueOf(IdIngrediente), jsonResponse);


                                }

                            }

                            Set<?> set = alphabet.entrySet();
                            // Get an iterator
                            Iterator<?> i = set.iterator();
                            // Display elements
                            while (i.hasNext()) {
                                @SuppressWarnings("rawtypes")
                                Map.Entry me = (Map.Entry) i.next();
                                System.out.print(me.getKey() + ": ");
                                System.out.println(me.getValue());
                                checkBox = new CheckBox(MostrarRecetaDeviceActivity.this);
                                //checkBox = new CheckBox(this);
                                checkBox.setId(Integer.parseInt(me.getKey().toString()));
                                checkBox.setText(me.getValue().toString());


                                //checkBox.setTextSize(TypedValue.COMPLEX_UNIT_PT, 10);


                                checkBox.setOnClickListener(getOnClickDoSomething(checkBox));
                                linearMain.addView(checkBox);
                            }

                            //txtResponse.setText(jsonResponse);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }

                        hidepDialog();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                hidepDialog();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req);
    }

    View.OnClickListener getOnClickDoSomething(final Button button) {
        return new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("*************Id******" + button.getId());
                System.out.println("Text***" + button.getText().toString());
            }
        };
    }

    private void makeJsonArrayRequestInstrucciones(){
        //linearMain = (TextView) findViewById(R.id.cardInstrucciones);
        txtResponse = (TextView) findViewById(R.id.cardInstrucciones);

        showpDialog();

        JsonArrayRequest req = new JsonArrayRequest(urlJsonArryInstrucciones,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                        try {
                            LinkedHashMap<String, String> alphabet = new LinkedHashMap<String, String>();
                            // Parsing json array response
                            // loop through each json object
                            jsonResponse = "";
                            for (int i = 0; i < response.length(); i++) {

                                JSONObject ingredientes = (JSONObject) response
                                        .get(i);

                                int IdRecetaJson = ingredientes.getInt("IdReceta");

                                System.out.println("2json" + IdRecetaJson);
                                System.out.println("2intent" + IdReceta);

                                String TextoInstruccion;
                                int OrdenInstruccion;
                                int IdInstruccion;


                                if(IdReceta == IdRecetaJson) {
                                    TextoInstruccion = ingredientes.getString("TextoInstruccion");
                                    OrdenInstruccion = ingredientes.getInt("OrdenInstruccion");
                                    IdInstruccion = ingredientes.getInt("IdInstruccion");


                                    jsonResponse += TextoInstruccion + "\n\n";

                                    if(response.length()!=i)
                                    {//poner linea horizontal
                                    }



                                }

                            }

                            jsonResponse += "\n\n";

                            txtResponse.setText(jsonResponse);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }

                        hidepDialog();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                hidepDialog();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req);
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
    private class MyNetworkTask extends AsyncTask<URL, Void, Bitmap>{

        ImageView tIV;

        public MyNetworkTask(ImageView iv){
            tIV = iv;
        }

        @Override
        protected Bitmap doInBackground(URL... urls) {

            Bitmap networkBitmap = null;

            URL networkUrl = urls[0]; //Load the first element
            try {
                networkBitmap = BitmapFactory.decodeStream(
                        networkUrl.openConnection().getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }

            return networkBitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            tIV.setImageBitmap(result);
        }

    }



    public void saveImage(Context context, Bitmap b, String imageName) {
        FileOutputStream foStream;
        try {
            foStream = context.openFileOutput(imageName, Context.MODE_PRIVATE);
            b.compress(Bitmap.CompressFormat.PNG, 100, foStream);
            foStream.close();
        } catch (Exception e) {
            System.out.println("saveImage"+ "Exception 2, Something went wrong!");
            e.printStackTrace();
        }
    }
    private class DownloadImage extends AsyncTask<String, Void, Bitmap> {
        private String TAG = "DownloadImage";
        private Bitmap downloadImageBitmap(String sUrl) {
            Bitmap bitmap = null;
            try {
                InputStream inputStream = new URL(sUrl).openStream();   // Download Image from URL
                bitmap = BitmapFactory.decodeStream(inputStream);       // Decode Bitmap
                inputStream.close();
            } catch (Exception e) {
                System.out.println(TAG+ "Exception 1, Something went wrong!");
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            return downloadImageBitmap(params[0]);
        }

        protected void onPostExecute(Bitmap result) {
            //saveImage(getApplicationContext(), result, String.valueOf(G_IdReceta)+".png");
        }
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


