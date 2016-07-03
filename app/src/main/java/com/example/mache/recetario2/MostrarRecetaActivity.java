package com.example.mache.recetario2;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.mache.recetario2.app.AppController;
import org.json.JSONArray;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

//para cargar Ingredientes
import org.json.JSONException;
import org.json.JSONObject;
import android.widget.Toast;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import android.widget.LinearLayout;
import android.widget.CheckBox;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import android.view.View;
import java.util.ArrayList;

import com.example.mache.recetario2.database.AppDatabaseHelper;


public class MostrarRecetaActivity extends AppCompatActivity {

    //--Para leer Ingredientes
    // json array response url
    private String urlJsonArry = "https://dl.dropboxusercontent.com/u/3194177/Taller/BDDOnlineIngredientes.json";
    private String urlJsonArryInstrucciones = "https://dl.dropboxusercontent.com/u/3194177/Taller/BDDOnlineInstrucciones.json";
    private static String TAG = MostrarRecetaActivity.class.getSimpleName();
    // Progress dialog
    private ProgressDialog pDialog;
    private TextView txtResponse;
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

    ArrayList<Integer> G_IdIngrediente = new ArrayList<Integer>();
    ArrayList<String> G_NombreIngrediente = new ArrayList<String>();
    ArrayList<String> G_Medicion = new ArrayList<String>();
    ArrayList<Double> G_Cantidad = new ArrayList<Double>();

    ArrayList<Integer> G_IdIngredienteCesta = new ArrayList<Integer>();

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
                (CollapsingToolbarLayout) findViewById(R.id.collapse_toolbar);

        //Nombre receta
        collapsingToolbar.setTitle(bundle.getString("SNombreReceta"));

        TextView PrimerCard = (TextView) findViewById(R.id.cardUno);
        PrimerCard.setText("Categoría: "+G_Categoria + "\n\n" +"Tiempo de preparación: "+ G_TiempoPreparacion+" min.");


        //para agregar el fondo
        ImageView targetImage = (ImageView)findViewById(R.id.ImagenFondo);

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

                new DownloadImage().execute(G_FotoReceta);

                AppDatabaseHelper dbHelper = new AppDatabaseHelper(MostrarRecetaActivity.this);
                //dbHelper.InsertarReceta(G_IdReceta, G_NombreReceta, G_FotoReceta, G_NumPersonas, G_TiempoPreparacion, G_Categoria, G_Dificulad);

                for(int x=0;x<G_IdIngrediente.size();x++) {
                    dbHelper.InsertarIngrediente(G_IdReceta, G_IdIngrediente.get(x),G_NombreIngrediente.get(x),G_Medicion.get(x),G_Cantidad.get(x));
                }

                for(int y=0;y<G_IdInstruccion.size();y++) {
                    dbHelper.InsertarInstruccion(G_IdReceta, G_IdInstruccion.get(y), G_OrdenInstruccion.get(y), G_TextoInstruccion.get(y));
                }

                File file            = getApplicationContext().getFileStreamPath(String.valueOf(G_IdReceta) + ".png");
                String imageFullPath = file.getAbsolutePath();
                G_Path = imageFullPath;

                dbHelper.InsertarReceta(G_IdReceta, G_NombreReceta, G_Path, G_NumPersonas, G_TiempoPreparacion, G_Categoria, G_Dificulad);

                Toast.makeText(MostrarRecetaActivity.this,
                        "Receta guardada",
                        Toast.LENGTH_SHORT).show();

            }
        });



    }

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

                            jsonResponse = "";
                            for (int i = 0; i < response.length(); i++) {

                                JSONObject ingredientes = (JSONObject) response
                                        .get(i);

                                int IdRecetaJson = ingredientes.getInt("IdReceta");

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
                                        jsonResponse = Cantidad +" "+ NombreIngrediente;
                                    }
                                    else
                                        jsonResponse = Cantidad +" "+ Medicion +" de "+ NombreIngrediente;

                                    alphabet.put(String.valueOf(IdIngrediente), jsonResponse);


                                }

                            }

                            Set<?> set = alphabet.entrySet();

                            Iterator<?> i = set.iterator();

                            while (i.hasNext()) {
                                @SuppressWarnings("rawtypes")
                                Map.Entry me = (Map.Entry) i.next();

                                checkBox = new CheckBox(MostrarRecetaActivity.this);
                                //checkBox = new CheckBox(this);
                                checkBox.setId(Integer.parseInt(me.getKey().toString()));
                                checkBox.setText(me.getValue().toString());

                                checkBox.setOnClickListener(getOnClickDoSomething(checkBox));
                                linearMain.addView(checkBox);
                            }

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

        AppController.getInstance().addToRequestQueue(req);
    }

    //Funcion que obtiene los valores seleccionados del checkbox
    View.OnClickListener getOnClickDoSomething(final Button button) {
        return new View.OnClickListener() {
            public void onClick(View v) {

                //Almacenar IdIngrediente a la cesta

                if(G_IdIngredienteCesta.contains(button.getId()))
                {
                    G_IdIngredienteCesta.remove(G_IdIngredienteCesta.indexOf(button.getId()));


                }
                else
                {
                    G_IdIngredienteCesta.add(button.getId());
                }


            }
        };
    }

    private void makeJsonArrayRequestInstrucciones(){

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

}


