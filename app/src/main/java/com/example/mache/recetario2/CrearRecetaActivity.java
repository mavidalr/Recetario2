package com.example.mache.recetario2;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;

import java.io.File;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import android.view.ViewGroup.LayoutParams;
import android.widget.VideoView;

import com.example.mache.recetario2.database.AppDatabaseHelper;

public class CrearRecetaActivity extends AppCompatActivity {


    //Variables para trabajar con la API camara

    // Activity request codes
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    // directory name to store captured images and videos
    private static final String IMAGE_DIRECTORY_NAME = "Camara";

    private Uri fileUri; // file url to store image/video

    private ImageView imgPreview;
    private VideoView videoPreview;
    private Button btnCapturePicture, btnRecordVideo;

    //Variables para input del  formulario

    private Spinner spinner1;

    private Spinner spinnerIngrediente;
    private int IdRecetaAInsertar, IdIngredienteAInsertar, IdInstruccionAInsertar;
    private int NumUltimaReceta, NumUltimoIngrediente, NumUltimaInstruccion;
    private String CadenaIngredientes ="";

    private Button btnSubmit;

    //para ingredientes
    private EditText ValorNombreIngrediente;
    private EditText ValorCantidadIngrediente;
    private Double CantidadIngrediente = 0.0;

    //para instrucciones
    private EditText ValorTextoInstruccion;

    //ArrayList<Integer> G_IdIngrediente = new ArrayList<Integer>();
    ArrayList<String> G_NombreIngrediente = new ArrayList<String>();
    ArrayList<String> G_Medicion = new ArrayList<String>();
    ArrayList<Double> G_Cantidad = new ArrayList<Double>();

    //ArrayList<Integer> G_IdInstruccion = new ArrayList<Integer>();
    ArrayList<Integer> G_OrdenInstruccion = new ArrayList<Integer>();
    ArrayList<String> G_TextoInstruccion = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_receta);

        ValorCantidadIngrediente = (EditText)findViewById(R.id.CRCantidadIngrediente);
        ValorTextoInstruccion = (EditText)findViewById(R.id.CRTextoInstruccion);
        ValorNombreIngrediente = (EditText)findViewById(R.id.CRNombreIngrediente);

        //Id de los elementos del formulario para tabla Recetas
        final EditText InputNombreReceta = (EditText)findViewById(R.id.CRNombreReceta);
        final EditText InputPorciones = (EditText)findViewById(R.id.CRPorciones);
        final EditText InputTiempoPreparacion = (EditText)findViewById(R.id.CRTiempoPreparacion);
        final EditText InputDificultad = (EditText)findViewById(R.id.CRDificultad);



        //addListenerOnSpinnerItemSelection();
        //addListenerOnButton();

        AppDatabaseHelper dbHelper = new AppDatabaseHelper(CrearRecetaActivity.this);
        NumUltimaReceta = dbHelper.NumUltimaReceta();
        NumUltimoIngrediente = dbHelper.NumUltimoIngrediente();
        NumUltimaInstruccion = dbHelper.NumUltimaInstruccion();

        IdRecetaAInsertar = ObtenerIdRecetaAInsertar(NumUltimaReceta);
        IdIngredienteAInsertar = ObtenerIdIngredienteAInsertar(NumUltimoIngrediente);
        IdInstruccionAInsertar = ObtenerIdInstruccionAInsertar(NumUltimaInstruccion);


        //System.out.println("Prox id: "+Integer.toString(IdRecetaAInsertar)+"--"+Integer.toString(IdIngredienteAInsertar)+"--"+Integer.toString(IdInstruccionAInsertar));
        //Si el numero es par
        //if(NumUltimaReceta % 2 == 0)

        //System.out.println("Wazaaa");






        //iniciar pop up agregarIngrediente
        spinnerIngrediente = (Spinner) findViewById(R.id.CRCategoriaIngredientes);

        final Button btnAgregarIngrediente = (Button)findViewById(R.id.CRAgregarIngrediente);
        btnAgregarIngrediente.setOnClickListener(new Button.OnClickListener(){


            @Override
            public void onClick(View arg0) {


                //se obtienen los valores de los input y se guardan en los arreglos de string/double

                CantidadIngrediente = Double.parseDouble(ValorCantidadIngrediente.getText().toString());
                G_Cantidad.add(CantidadIngrediente);
                G_NombreIngrediente.add(ValorNombreIngrediente.getText().toString());
                G_Medicion.add(String.valueOf(spinnerIngrediente.getSelectedItem()));

                Toast.makeText(CrearRecetaActivity.this,
                        "Ingrediente agregado a la receta",
                        Toast.LENGTH_SHORT).show();
                ValorNombreIngrediente.setText("");
                ValorCantidadIngrediente.setText("");



            }});


        //iniciar pop up AgregarInstruccion
        final Button btnAgregarInstruccion = (Button)findViewById(R.id.CRAgregarInstruccion);
        btnAgregarInstruccion.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View arg0) {
                //se obtienen los valores de los input y se guardan en los arreglos de string
                G_TextoInstruccion.add(ValorTextoInstruccion.getText().toString());
                //El numero de orden de la isntruccion se obtiene desde la posicion del arralist de instrucciones
                Toast.makeText(CrearRecetaActivity.this,
                        "Instrucción agregada a la receta",
                        Toast.LENGTH_SHORT).show();
                ValorTextoInstruccion.setText("");

            }});

        //iniciar pop up verIngredientes
        final Button btnVerIngredientes = (Button)findViewById(R.id.CRVerIngredientes);
        btnVerIngredientes.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View arg0) {

                MostrarIngredientes();

            }});

        //iniciar pop up VerInstruccion
        final Button btnVerInstrucciones = (Button)findViewById(R.id.CRVerInstrucciones);
        btnVerInstrucciones.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View arg0) {

                MostrarInstrucciones();

            }});

        Button btnCrearReceta = (Button) findViewById(R.id.btnCrearReceta);
        spinner1 = (Spinner) findViewById(R.id.CRCategoria);


        btnCrearReceta.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                AppDatabaseHelper dbHelper = new AppDatabaseHelper(CrearRecetaActivity.this);
                //dbHelper.InsertarReceta(G_IdReceta, G_NombreReceta, G_FotoReceta, G_NumPersonas, G_TiempoPreparacion, G_Categoria, G_Dificulad);

                for(int x=0;x<G_NombreIngrediente.size();x++) {
                    System.out.println("Hay ingredientes");
                    dbHelper.InsertarIngrediente(IdRecetaAInsertar, IdIngredienteAInsertar+x ,G_NombreIngrediente.get(x),G_Medicion.get(x),G_Cantidad.get(x));
                }

                for(int y=0;y<G_TextoInstruccion.size();y++) {
                    System.out.println("Hay instrucciones");
                    //y representa el orden de la instruccion
                    dbHelper.InsertarInstruccion(IdRecetaAInsertar, IdInstruccionAInsertar+y, y, G_TextoInstruccion.get(y));
                }


                //File file = getApplicationContext().getFileStreamPath(String.valueOf(IdRecetaAInsertar) + ".png");
                //String G_Path = file.getAbsolutePath();

                String G_Path = fileUri.getPath();

                //_Path = imageFullPath;


                //File myFile = new File(fileUri.getPath());
                //String G_Path = myFile.getAbsolutePath();

                //String.valueOf(spinner1.getSelectedItem() obtiene valor de la Categoría de la receta

                //dbHelper.InsertarReceta(G_IdReceta, G_NombreReceta, G_Path, G_NumPersonas, G_TiempoPreparacion, G_Categoria, G_Dificulad);

                dbHelper.InsertarReceta(IdRecetaAInsertar, InputNombreReceta.getText().toString(), G_Path, Integer.parseInt(InputPorciones.getText().toString()), Integer.parseInt(InputTiempoPreparacion.getText().toString()), String.valueOf(spinner1.getSelectedItem()), Integer.parseInt(InputDificultad.getText().toString()));
                System.out.println("BDD yep");

                System.out.println(G_Path);
                /*System.out.println(Integer.toString(IdRecetaAInsertar));
                System.out.println(InputNombreReceta.getText().toString());

                System.out.println(InputPorciones.getText().toString());
                System.out.println(InputTiempoPreparacion.getText().toString());
                System.out.println(String.valueOf(spinner1.getSelectedItem()));
                System.out.println(InputDificultad.getText().toString());
*/

                Toast.makeText(CrearRecetaActivity.this,
                        "Receta guardada",
                        Toast.LENGTH_SHORT).show();

            }
        });


//Para trabajar con la API camara

        imgPreview = (ImageView) findViewById(R.id.imgPreview);
        //videoPreview = (VideoView) findViewById(R.id.videoPreview);
        btnCapturePicture = (Button) findViewById(R.id.btnCapturePicture);
        //btnRecordVideo = (Button) findViewById(R.id.btnRecordVideo);

        /**
         * Capture image button click event
         */
        btnCapturePicture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // capture picture
                captureImage();

                Toast.makeText(CrearRecetaActivity.this,
                        "HOLI, la img esta en"+fileUri,
                        Toast.LENGTH_SHORT).show();
            }

        });

        /**
         * Record video button click event

        btnRecordVideo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // record video
                recordVideo();
            }
        });
        */
        // Checking camera availability
        if (!isDeviceSupportCamera()) {
            Toast.makeText(getApplicationContext(),
                    "No es posible utilizar la cámara del dispositivo",
                    Toast.LENGTH_LONG).show();
            // will close the app if the device does't have camera
            finish();
        }
    }

    //Funciones para trabajar la API camara

    /**
     * Checking device has camera hardware or not
     * */
    private boolean isDeviceSupportCamera() {
        if (getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    /**
     * Capturing Camera Image will lauch camera app requrest image capture
     */
    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE, Integer.toString(IdRecetaAInsertar));

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    /**
     * Here we store the file url as it will be null after returning from camera
     * app
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on scren orientation
        // changes
        outState.putParcelable("file_uri", fileUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        fileUri = savedInstanceState.getParcelable("file_uri");
    }

    /**
     * Recording video

    private void recordVideo() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);

        // set video quality
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file
        // name

        // start the video capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_VIDEO_REQUEST_CODE);
    }
    */
    /**
     * Receiving activity result method will be called after closing the camera
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // successfully captured the image
                // display it in image view
                previewCapturedImage();
            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(getApplicationContext(),
                        "Captura de imagen cancelada", Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }
        } /*else if (requestCode == CAMERA_CAPTURE_VIDEO_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // video successfully recorded
                // preview the recorded video
                previewVideo();
            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled recording
                Toast.makeText(getApplicationContext(),
                        "User cancelled video recording", Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to record video
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to record video", Toast.LENGTH_SHORT)
                        .show();
            }
        }*/
    }

    /**
     * Display image from a path to ImageView
     */
    private void previewCapturedImage() {
        try {
            // hide video preview
            //videoPreview.setVisibility(View.GONE);

            imgPreview.setVisibility(View.VISIBLE);

            // bimatp factory
            BitmapFactory.Options options = new BitmapFactory.Options();

            // downsizing image as it throws OutOfMemory Exception for larger
            // images
            options.inSampleSize = 8;

            final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
                    options);

            imgPreview.setImageBitmap(bitmap);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        Toast.makeText(CrearRecetaActivity.this,
                "HOLI  umero 2, la img esta en"+fileUri,
                Toast.LENGTH_SHORT).show();
    }

    /**
     * Previewing recorded video

    private void previewVideo() {
        try {
            // hide image preview
            imgPreview.setVisibility(View.GONE);

            videoPreview.setVisibility(View.VISIBLE);
            videoPreview.setVideoPath(fileUri.getPath());
            // start playing
            videoPreview.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    */
    /**
     * ------------ Helper Methods ----------------------
     * */

    /**
     * Creating file uri to store image/video
     */
    public Uri getOutputMediaFileUri(int type, String ID) {
        return Uri.fromFile(getOutputMediaFile(type,ID));
    }

    /**
     * returning image / video
     */
    private static File getOutputMediaFile(int type, String ID) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Error al crear el directorio de almacenamiento"
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;

        //Cambiar el nombre de la imagen------------------------------------------------------------------------------------------------------------------------------------------------
        if (type == MEDIA_TYPE_IMAGE) {

            //String ID = String.valueOf(ObtenerId);


            mediaFile = new File(mediaStorageDir.getPath() + File.separator + ID + ".png");

            //mediaFile = new File(mediaStorageDir.getPath() + File.separator
            //        + "IMG_" + timeStamp + ".png");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    //Funciones para manejar el formulario
    public void MostrarIngredientes()
    {
        CadenaIngredientes ="";

        AlertDialog.Builder builderSingle = new AlertDialog.Builder(CrearRecetaActivity.this);
        //builderSingle.setIcon(R.drawable.ic_launcher);
        builderSingle.setTitle("Eliminar ingredientes:");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                CrearRecetaActivity.this,
                android.R.layout.select_dialog_singlechoice);


        for(int x=0;x<G_NombreIngrediente.size();x++) {
            CadenaIngredientes = G_Cantidad.get(x) +" "+ G_Medicion.get(x) +" "+ G_NombreIngrediente.get(x);
            arrayAdapter.add(CadenaIngredientes);
        }

        builderSingle.setNegativeButton(
                "Volver",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builderSingle.setAdapter(
                arrayAdapter,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String Cadena = arrayAdapter.getItem(which);
                        AlertDialog.Builder builderInner = new AlertDialog.Builder(
                                CrearRecetaActivity.this);
                        //builderInner.setMessage(Cadena);
                        String [] Nombre = Cadena.split("\\ ");
                        builderInner.setMessage(Nombre[2]);

                        G_NombreIngrediente.remove(Nombre[2]);

                        builderInner.setTitle("Se ha eliminado:");
                        builderInner.setPositiveButton(
                                "Ok",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(
                                            DialogInterface dialog,
                                            int which) {
                                        dialog.dismiss();
                                    }
                                });
                        builderInner.show();
                    }
                });
        builderSingle.show();
    }

    public void MostrarInstrucciones()
    {

        AlertDialog.Builder builderSingle = new AlertDialog.Builder(CrearRecetaActivity.this);
        //builderSingle.setIcon(R.drawable.ic_launcher);
        builderSingle.setTitle("Eliminar instrucciones:");

        final ArrayAdapter<String> arrayAdapterInstrucciones = new ArrayAdapter<String>(
                CrearRecetaActivity.this,
                android.R.layout.select_dialog_singlechoice);


        for(int x=0;x<G_TextoInstruccion.size();x++) {
            //CadenaIngredientes = G_Cantidad.get(x) +" "+ G_Medicion.get(x) +" "+ G_NombreIngrediente.get(x);
            arrayAdapterInstrucciones.add(G_TextoInstruccion.get(x));
        }

        builderSingle.setNegativeButton(
                "Volver",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builderSingle.setAdapter(
                arrayAdapterInstrucciones,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String Cadena = arrayAdapterInstrucciones.getItem(which);

                        AlertDialog.Builder builderInner = new AlertDialog.Builder(
                                CrearRecetaActivity.this);
                        //builderInner.setMessage(Cadena);
                        //String [] Nombre = Cadena.split("\\ ");
                        builderInner.setMessage(Cadena);

                        G_TextoInstruccion.remove(which);

                        builderInner.setTitle("Se ha eliminado:");
                        builderInner.setPositiveButton(
                                "Ok",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(
                                            DialogInterface dialog,
                                            int which) {
                                        dialog.dismiss();
                                    }
                                });
                        builderInner.show();
                    }
                });
        builderSingle.show();
    }

    public int ObtenerIdRecetaAInsertar(int NumUltimaReceta)
    {

        if(NumUltimaReceta == -1)
        {
            //No hay recetas guardadas
            return 1;
        }
        else if(NumUltimaReceta%2 == 0)
        {
            //El Id es par, por lo que pertenece a receta online
            return NumUltimaReceta + 1;
        }
        else
        {
            //El ultimo id es impar, por lo que pertenece a una receta creada en el dispositivo
            return NumUltimaReceta + 2;
        }

    }

    public int ObtenerIdIngredienteAInsertar(int NumUltimoIngrediente)
    {

        if(NumUltimoIngrediente == -1)
        {
            //No hay recetas guardadas
            return 1;
        }
        else if(NumUltimoIngrediente%2 == 0)
        {
            //El Id es par, por lo que pertenece a receta online
            return NumUltimoIngrediente + 1;
        }
        else
        {
            //El ultimo id es impar, por lo que pertenece a una receta creada en el dispositivo
            return NumUltimoIngrediente + 2;
        }

    }

    public int ObtenerIdInstruccionAInsertar(int NumUltimaInstruccion)
    {

        if(NumUltimaInstruccion == -1)
        {
            //No hay recetas guardadas
            return 1;
        }
        else if(NumUltimaInstruccion%2 == 0)
        {
            //El Id es par, por lo que pertenece a receta online
            return NumUltimaInstruccion + 1;
        }
        else
        {
            //El ultimo id es impar, por lo que pertenece a una receta creada en el dispositivo
            return NumUltimaInstruccion + 2;
        }

    }



    public void addListenerOnSpinnerItemSelection() {
        spinner1 = (Spinner) findViewById(R.id.CRCategoria);
        spinner1.setOnItemSelectedListener(new CustomOnItemSelectedListener());
        //spinnerIngrediente = (Spinner) findViewById(R.id.CRCategoriaIngredientes);
        //spinnerIngrediente.setOnItemSelectedListener(new CustomOnItemSelectedListener());
    }

    // get the selected dropdown list value
    public void addListenerOnButton() {

        spinner1 = (Spinner) findViewById(R.id.CRCategoria);
        spinnerIngrediente = (Spinner) findViewById(R.id.CRCategoriaIngredientes);
        //String.valueOf(spinner1.getSelectedItem())

        //spinner2 = (Spinner) findViewById(R.id.spinner2);
        btnSubmit = (Button) findViewById(R.id.btnCrearReceta);

        btnSubmit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                Toast.makeText(CrearRecetaActivity.this,
                        "OnClickListener : " +
                                "\nSpinner 1 : "+ String.valueOf(spinner1.getSelectedItem()) +
                                "\nSpinner 2 lalal ESTO NO : "+ String.valueOf(spinner1.getSelectedItem()),
                        Toast.LENGTH_SHORT).show();
            }

        });
    }
}
