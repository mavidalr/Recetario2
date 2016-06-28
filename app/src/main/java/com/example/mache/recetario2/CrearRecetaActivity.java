package com.example.mache.recetario2;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;

import java.util.ArrayList;
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
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import android.view.ViewGroup.LayoutParams;

import com.example.mache.recetario2.database.AppDatabaseHelper;

public class CrearRecetaActivity extends AppCompatActivity {

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
        Button btnAgregarImagen = (Button) findViewById(R.id.btnAgregarImagen);
        //Button btnAgregarIngrediente = (Button) findViewById(R.id.CRAgregarIngrediente);
        //Button btnAgregarInstruccion = (Button) findViewById(R.id.CRAgregarInstruccion);
        //Button btnVerInstrucciones = (Button) findViewById(R.id.CRVerInstrucciones);
        //Button bntVerIngredientes = (Button) findViewById(R.id.CRVerIngredientes);

        btnCrearReceta.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Toast.makeText(CrearRecetaActivity.this,
                        "HOLI",
                        Toast.LENGTH_SHORT).show();

            }
        });

    }


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
