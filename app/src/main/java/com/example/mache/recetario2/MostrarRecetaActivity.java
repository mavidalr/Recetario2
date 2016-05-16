package com.example.mache.recetario2;

import android.app.ProgressDialog;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.example.mache.recetario2.app.AppController;

import org.json.JSONArray;

import java.io.IOException;
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


 

public class MostrarRecetaActivity extends AppCompatActivity {

    //--Para leer Ingredientes
    // json array response url
    private String urlJsonArry = "https://dl.dropboxusercontent.com/u/3194177/Taller/BDDOnlineIngredientes.json";
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
    //LinearLayout linearMain;
    //CheckBox checkBox;

    //Fin para leer ingredientes

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_receta);

        //--
        //Recuperamos la información pasada en el intent
        Bundle bundle = this.getIntent().getExtras();

        String ID_RECETA = bundle.getString("SIdReceta");
        IdReceta = Integer.parseInt(ID_RECETA);

        //Construimos el mensaje a mostrar
        //txtSaludo.setText("Hola " + bundle.getString("NOMBRE"));
        System.out.println("Se recibe Id = : " + bundle.getString("SIdReceta"));
        System.out.println("Se recibe Id = : " + bundle.getString("SURL"));
        System.out.println("Se recibe Id = : " + bundle.getString("SNombreReceta"));
        System.out.println("Se recibe Id = : " + bundle.getString("STiempoPreparacion"));
        System.out.println("Se recibe Id = : " + bundle.getString("SCategoria"));
        //--




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
        txtResponse = (TextView) findViewById(R.id.cardDos);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Cargando");
        pDialog.setCancelable(false);

        makeJsonArrayRequest();


        //-- Fin para leer ingredientes


    }
    //-- Para leer ingredientes


    /**
     * Method to make json array request where response starts with [
     * */
    private void makeJsonArrayRequest() {
        showpDialog();

        JsonArrayRequest req = new JsonArrayRequest(urlJsonArry,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                        try {
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
                                    int IdIngrediente = ingredientes.getInt("Cantidad");



                                    if (Medicion.equals("Unidad"))
                                    {
                                        //System.out.println("Holi, soy una unidad de ingrediente");
                                        jsonResponse += Cantidad +" "+ NombreIngrediente+ "\n\n";
                                    }
                                    else
                                        jsonResponse += Cantidad +" "+ Medicion +" de "+ NombreIngrediente+ "\n\n";





                                    //jsonResponse += "NombreIngrediente: " + NombreIngrediente + "\n\n";
                                    //jsonResponse += "Medicion: " + Medicion + "\n\n";
                                    //jsonResponse += "Cantidad: " + Cantidad + "\n\n";


                                    //JSONObject phone = person
                                    //        .getJSONObject("phone");
                                    //String home = phone.getString("home");
                                    //String mobile = phone.getString("mobile");

                                    //jsonResponse += "Name: " + name + "\n\n";
                                    //jsonResponse += "Email: " + email + "\n\n";
                                    //jsonResponse += "Home: " + home + "\n\n";
                                    //jsonResponse += "Mobile: " + mobile + "\n\n\n";
                                }

                            }

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



    //Para cargar ingredientes



}


