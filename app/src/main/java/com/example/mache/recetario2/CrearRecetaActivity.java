package com.example.mache.recetario2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
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

public class CrearRecetaActivity extends AppCompatActivity {

    private Spinner spinner1;
    private Button btnSubmit;

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


        //iniciar pop up agregarIngrediente
        final Button btnAgregarIngrediente = (Button)findViewById(R.id.CRAgregarIngrediente);
        btnAgregarIngrediente.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View arg0) {
                LayoutInflater layoutInflater
                        = (LayoutInflater)getBaseContext()
                        .getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = layoutInflater.inflate(R.layout.agregar_ingrediente_pop_up, null);
                final PopupWindow popupWindow = new PopupWindow(
                        popupView,
                        LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT);

                Button btnDismiss = (Button)popupView.findViewById(R.id.cancelAgregarIngrediente);
                btnDismiss.setOnClickListener(new Button.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        popupWindow.dismiss();
                    }});

                //popupWindow.showAsDropDown(btnOpenPopup, 50, -50);
                popupWindow.showAtLocation(btnAgregarIngrediente, Gravity.CENTER, 0, 0);

            }});

        //iniciar pop up AgregarInstruccion
        final Button btnAgregarInstruccion = (Button)findViewById(R.id.CRAgregarInstruccion);
        btnAgregarInstruccion.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View arg0) {
                LayoutInflater layoutInflater
                        = (LayoutInflater)getBaseContext()
                        .getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = layoutInflater.inflate(R.layout.agregar_instruccion_pop_up, null);
                final PopupWindow popupWindow = new PopupWindow(
                        popupView,
                        LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT);

                Button btnDismiss = (Button)popupView.findViewById(R.id.cancelAgregarInstruccion);
                btnDismiss.setOnClickListener(new Button.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        popupWindow.dismiss();
                    }});

                //popupWindow.showAsDropDown(btnOpenPopup, 50, -50);
                popupWindow.showAtLocation(btnAgregarInstruccion, Gravity.CENTER, 0, 0);

            }});

        //iniciar pop up verIngredientes
        final Button btnVerIngredientes = (Button)findViewById(R.id.CRVerIngredientes);
        btnVerIngredientes.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View arg0) {
                LayoutInflater layoutInflater
                        = (LayoutInflater)getBaseContext()
                        .getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = layoutInflater.inflate(R.layout.ver_ingredientes_pop_up, null);
                final PopupWindow popupWindow = new PopupWindow(
                        popupView,
                        LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT);

                Button btnDismiss = (Button)popupView.findViewById(R.id.cancelVerIngredientes);
                btnDismiss.setOnClickListener(new Button.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        popupWindow.dismiss();
                    }});

                //popupWindow.showAsDropDown(btnOpenPopup, 50, -50);
                popupWindow.showAtLocation(btnVerIngredientes, Gravity.CENTER, 0, 0);

            }});

        //iniciar pop up VerInstruccion
        final Button btnVerInstrucciones = (Button)findViewById(R.id.CRVerInstrucciones);
        btnVerInstrucciones.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View arg0) {
                LayoutInflater layoutInflater
                        = (LayoutInflater)getBaseContext()
                        .getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = layoutInflater.inflate(R.layout.ver_instrucciones_pop_up, null);
                final PopupWindow popupWindow = new PopupWindow(
                        popupView,
                        LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT);

                Button btnDismiss = (Button)popupView.findViewById(R.id.cancelVerInstrucciones);
                btnDismiss.setOnClickListener(new Button.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        popupWindow.dismiss();
                    }});

                //popupWindow.showAsDropDown(btnOpenPopup, 50, -50);
                popupWindow.showAtLocation(btnVerInstrucciones, Gravity.CENTER, 0, 0);

            }});


        //addListenerOnSpinnerItemSelection();
        addListenerOnButton();

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

       /* btnAgregarIngrediente.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Toast.makeText(CrearRecetaActivity.this,
                        "Se abre pop up con agregar ingrediente",
                        Toast.LENGTH_SHORT).show();

            }
        });
*/




    }








    public void addListenerOnSpinnerItemSelection() {
        spinner1 = (Spinner) findViewById(R.id.CRCategoria);
        spinner1.setOnItemSelectedListener(new CustomOnItemSelectedListener());
    }

    // get the selected dropdown list value
    public void addListenerOnButton() {

        spinner1 = (Spinner) findViewById(R.id.CRCategoria);
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
