package com.example.mache.recetario2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
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


public class MostrarRecetaActivity extends AppCompatActivity {

    //--Para leer Ingredientes
    // json array response url
    private String urlJsonArry = "https://dl.dropboxusercontent.com/u/3194177/Taller/BDDOnlineIngredientes.json";
    private String urlJsonArryInstrucciones = "https://dl.dropboxusercontent.com/u/3194177/Taller/BDDOnlineInstrucciones.json";
    private String urlJsonObj = "http://api.androidhive.info/volley/person_object.json";
    private static String TAG = MostrarRecetaActivity.class.getSimpleName();
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

    //para el boton savve
    private int G_IdReceta;
    private String G_NombreReceta;
    private String G_FotoReceta;
    private int G_NumPersonas;
    private int G_TiempoPreparacion;
    private String G_Categoria;
    private int G_Dificulad;

    private String G_Path;

    /*private int G_IdIngrediente[];
    private String G_NombreIngrediente[];
    private String G_Medicion[];
    private int G_Cantidad[];
    */
    ArrayList<Integer> G_IdIngrediente = new ArrayList<Integer>();
    ArrayList<String> G_NombreIngrediente = new ArrayList<String>();
    ArrayList<String> G_Medicion = new ArrayList<String>();
    ArrayList<Double> G_Cantidad = new ArrayList<Double>();

    /*private int G_IdInstruccion[];
    private int G_OrdenInstruccion[];
    private String G_TextoInstruccion[];
    */
    ArrayList<Integer> G_IdInstruccion = new ArrayList<Integer>();
    ArrayList<Integer> G_OrdenInstruccion = new ArrayList<Integer>();
    ArrayList<String> G_TextoInstruccion = new ArrayList<String >();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_receta);

        //--
        //Recuperamos la información pasada en el intent
        Bundle bundle = this.getIntent().getExtras();

        String ID_RECETA = bundle.getString("SIdReceta");
        IdReceta = Integer.parseInt(ID_RECETA);
        String ID_TIEMPO = bundle.getString("SIdReceta");
        int IdTiempo = Integer.parseInt(ID_TIEMPO);

        //Construimos el mensaje a mostrar
        //txtSaludo.setText("Hola " + bundle.getString("NOMBRE"));
        System.out.println("Se recibe Id = : " + bundle.getString("SIdReceta"));
        System.out.println("Se recibe Id = : " + bundle.getString("SURL"));
        System.out.println("Se recibe Id = : " + bundle.getString("SNombreReceta"));
        System.out.println("Se recibe Id = : " + bundle.getString("STiempoPreparacion"));
        System.out.println("Se recibe Id = : " + bundle.getString("SCategoria"));
        //--

        G_IdReceta = IdReceta;
        G_NombreReceta = bundle.getString("SNombreReceta");
        G_FotoReceta = bundle.getString("SURL");
        G_NumPersonas = 0;
        G_TiempoPreparacion = IdTiempo;
        G_Categoria = bundle.getString("SCategoria");
        G_Dificulad = 0;




        final Toolbar toolbar = (Toolbar) findViewById(R.id.MyToolbar);
        //setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapse_toolbar);
        //collapsingToolbar.setTitle("My Toolbar Tittle");

        //Nombre receta
        collapsingToolbar.setTitle(bundle.getString("SNombreReceta"));


        //para agregar el fondo
        ImageView targetImage = (ImageView)findViewById(R.id.ImagenFondo);

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

        makeJsonArrayRequest();
        makeJsonArrayRequestInstrucciones();


        //-- Fin para leer ingredientes


        FloatingActionButton Guardar = (FloatingActionButton) findViewById(R.id.guardar);
        Guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
                //Intent intent = new Intent(MainActivity.this, NewMessageActivity.class);
                //startActivity(intent);
                System.out.println("Empezar a guadar en la bdd");

                new DownloadImage().execute(G_FotoReceta);



                ImageView cargaImg = (ImageView) findViewById(R.id.pathIMG);

                //Bitmap bMap = BitmapFactory.decodeFile(G_Path);
                //cargaImg.setImageBitmap(bMap);
                //cargaImg.setbMap(BitmapFactory.decodeFile(G_Path));
                cargaImg.setImageBitmap(loadImageBitmap(getApplicationContext(), String.valueOf(G_IdReceta)+".png"));



                AppDatabaseHelper dbHelper = new AppDatabaseHelper(MostrarRecetaActivity.this);
                dbHelper.InsertarReceta(G_IdReceta, G_NombreReceta, G_FotoReceta, G_NumPersonas, G_TiempoPreparacion, G_Categoria, G_Dificulad);

                for(int x=0;x<G_IdIngrediente.size();x++) {
                    //System.out.println(al.get(x));
                    dbHelper.InsertarIngrediente(G_IdReceta, G_IdIngrediente.get(x),G_NombreIngrediente.get(x),G_Medicion.get(x),G_Cantidad.get(x));
                }

                for(int y=0;y<G_IdInstruccion.size();y++) {
                    dbHelper.InsertarInstruccion(G_IdReceta, G_IdInstruccion.get(y), G_OrdenInstruccion.get(y), G_TextoInstruccion.get(y));
                }

                System.out.println("BDD yep");

            }
        });

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

                                    G_IdIngrediente.add(IdIngrediente);
                                    G_NombreIngrediente.add(NombreIngrediente);
                                    G_Medicion.add(Medicion);
                                    G_Cantidad.add(Cantidad);

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
                                checkBox = new CheckBox(MostrarRecetaActivity.this);
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

                                    G_TextoInstruccion.add(TextoInstruccion);
                                    G_OrdenInstruccion.add(OrdenInstruccion);
                                    G_IdInstruccion.add(IdInstruccion);

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
            saveImage(getApplicationContext(), result, String.valueOf(G_IdReceta)+".png");
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


