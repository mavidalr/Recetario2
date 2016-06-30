package com.example.mache.recetario2.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;


public class AppDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "recetario.db";
    private static final int DB_VERSION = 1;
    //private static final String CREATE_TABLE_RAMOS ="CREATE TABLE " + "ramos (codigo TEXT, nombre TEXT,descripcion TEXT, profesor TEXT)";
    private static final String CREATE_TABLE_RECETAS ="" +
            "CREATE TABLE Recetas(" +
            "   IdReceta INTEGER," +
            "   NombreReceta TEXT," +
            "   FotoReceta TEXT," +
            "   NumPersonas INTEGER," +
            "   TiempoPreparacion INTEGER," +
            "   Categoria TEXT," +
            "   Dificultad INTEGER" +
            ");";

    private static final String CREATE_TABLE_INGREDIENTES ="" +
            "CREATE TABLE Ingredientes(" +
            "   IdIngrediente INTEGER," +
            "   IdReceta INTEGER," +
            "   NombreIngrediente TEXT," +
            "   Medicion TEXT," +
            "   Cantidad REAL" +
            ");";

    private static final String CREATE_TABLE_INSTRUCCIONES ="" +
            "CREATE TABLE Instrucciones(" +
            "   IdInstruccion INTEGER," +
            "   IdReceta INTEGER," +
            "   OrdenInstruccion INTEGER," +
            "   TextoInstruccion TEXT" +
            ");";

    private static final String CREATE_TABLE_MENUS ="" +
            "CREATE TABLE Menus(" +
            "   IdMenu INTEGER PRIMARY KEY," +
            "   IdReceta INTEGER," +
            "   DiaMenu INTEGER," +
            "   MesMenu INTEGER," +
            "   AnioMenu INTEGER," +
            "   TipoMenu TEXT" +
            ");";

    private static final String CREATE_TABLE_CESTA ="" +
            "CREATE TABLE Cesta(" +
            "   IdElemento INTEGER PRIMARY KEY," +
            "   NombreIngredienteCesta TEXT," +
            "   MedicionCesta TEXT," +
            "   CantidadCesta REAL," +
            "   TipoCesta INTEGER," +
            "   TextoCesta TEXT" +
            ");";

    public static final String TABLE_RECETAS= "Recetas";
    public static final String TABLE_INSTRUCCIONES= "Instrucciones";
    public static final String TABLE_INGREDIENTES= "Ingredientes";
    public static final String TABLE_MENUS= "Menus";
    public static final String TABLE_CESTA= "Cesta";



    public static final String COL_IdReceta = "IdReceta";
    public static final String COL_IdInstruccion = "IdInstruccion";
    public static final String COL_IdIngrediente = "IdIngrediente";
    public static final String COL_FotoReceta = "FotoReceta";
    public static final String COL_NumPersonas = "NumPersonas";
    public static final String COL_TiempoPreparacion = "TiempoPreparacion";
    public static final String COL_Categoria = "Categoria";
    public static final String COL_Dificultad = "Dificultad";
    public static final String COL_NombreReceta = "NombreReceta";
    //public static final String COL_ = "";

    public static final String COL_OrdenInstruccion = "OrdenInstruccion";
    public static final String COL_TextoInstruccion = "TextoInstruccion";

    public static final String COL_NombreIngrediente = "NombreIngrediente";
    public static final String COL_Medicion = "Medicion";
    public static final String COL_Cantidad = "Cantidad";

    public static final String COL_IdMenu = "IdMenu";
    public static final String COL_DiaMenu = "DiaMenu";
    public static final String COL_MesMenu = "MesMenu";
    public static final String COL_AnioMenu = "AnioMenu";
    public static final String COL_TipoMenu = "TipoMenu";

    public static final String COL_IdElemento = "IdElemento";
    public static final String COL_NombreIngredienteCesta = "NombreIngredienteCesta";
    public static final String COL_MedicionCesta = "MedicionCesta";
    public static final String COL_CantidadCesta = "CantidadCesta";
    public static final String COL_TipoCesta = "TipoCesta";
    public static final String COL_TextoCesta = "TextoCesta";


    public AppDatabaseHelper(Context context) {

        super(context, DATABASE_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {

        database.execSQL(CREATE_TABLE_RECETAS);
        database.execSQL(CREATE_TABLE_INSTRUCCIONES);
        database.execSQL(CREATE_TABLE_INGREDIENTES);
        database.execSQL(CREATE_TABLE_MENUS);
        database.execSQL(CREATE_TABLE_CESTA);
        //crearDatosIniciales(database);
    }

    public int InsertarReceta(int IdReceta, String NombreReceta, String FotoReceta, int NumPersonas, int TiempoPreparacion, String Categoria, int Dificultad){
        int IdInsert = 0;
        SQLiteDatabase db = getWritableDatabase();
        if(db!=null){
            ContentValues values = new ContentValues();
            //values.put("nombre", nombre);

            values.put("IdReceta", IdReceta);
            values.put("NombreReceta", NombreReceta);
            values.put("FotoReceta", FotoReceta);
            values.put("NumPersonas", NumPersonas);
            values.put("TiempoPreparacion", TiempoPreparacion);
            values.put("Categoria", Categoria);
            values.put("Dificultad", Dificultad);

            IdInsert = (int) db.insert(TABLE_RECETAS, null, values);
        }
        db.close();
        return IdInsert;
    }

    public int NumUltimaReceta()
    {
        //select count(*) as total from mitabla
        SQLiteDatabase db = getWritableDatabase();
        //Cursor c=db.rawQuery("select count(*) as total from "+TABLE_RECETAS, null);

        Cursor c=db.rawQuery("SELECT "+COL_IdReceta+" FROM "+TABLE_RECETAS+" ORDER BY "+COL_IdReceta+" DESC LIMIT 1", null);
        if(c.moveToFirst())
        {
            return c.getInt(0);
        }
        else
            return -1;
    }

    public int NumUltimoIngrediente()
    {
        SQLiteDatabase db = getWritableDatabase();
        Cursor c=db.rawQuery("SELECT "+COL_IdIngrediente+" FROM "+TABLE_INGREDIENTES+" ORDER BY "+COL_IdIngrediente+" DESC LIMIT 1", null);
        if(c.moveToFirst())
        {
            return c.getInt(0);
        }
        else
            return -1;
    }
    public int NumUltimaInstruccion()
    {
        SQLiteDatabase db = getWritableDatabase();
        Cursor c=db.rawQuery("SELECT "+COL_IdInstruccion+" FROM "+TABLE_INSTRUCCIONES+" ORDER BY "+COL_IdInstruccion+" DESC LIMIT 1", null);
        if(c.moveToFirst())
        {
            return c.getInt(0);
        }
        else
            return -1;
    }

    public String PathImagenSDCard(String IdReceta)
    {
        //select count(*) as total from mitabla
        SQLiteDatabase db = getWritableDatabase();
        //Cursor c=db.rawQuery("select count(*) as total from "+TABLE_RECETAS, null);

        //Cursor c=db.rawQuery("SELECT "+COL_FotoReceta+" FROM "+TABLE_RECETAS+" where "+COL_IdReceta+" = " + IdReceta, null);
        Cursor c=db.rawQuery("SELECT "+COL_FotoReceta+" FROM "+TABLE_RECETAS+" where "+COL_IdReceta+" = 3", null);
        if(c.moveToFirst())
        {
            return c.getString(0);
        }
        else
            return "No se encontro la imagen";
    }

    public int InsertarIngrediente(int IdReceta, int IdIngrediente, String NombreIngrediente, String Medicion, double Cantidad)
    {
        int IdInsert = 0;
        SQLiteDatabase db = getWritableDatabase();
        if(db!=null){
            ContentValues values = new ContentValues();
            //values.put("nombre", nombre);

            values.put("IdReceta", IdReceta);
            values.put("IdIngrediente", IdIngrediente);
            values.put("NombreIngrediente", NombreIngrediente);
            values.put("Medicion", Medicion);
            values.put("Cantidad", Cantidad);

            IdInsert = (int) db.insert(TABLE_INGREDIENTES, null, values);
        }
        db.close();
        return IdInsert;
    }

    public int InsertarInstruccion(int IdReceta, int IdInstruccion, int OrdenInstruccion, String TextoInstruccion)
    {
        int IdInsert = 0;
        SQLiteDatabase db = getWritableDatabase();
        if(db!=null){
            ContentValues values = new ContentValues();
            //values.put("nombre", nombre);

            values.put("IdReceta", IdReceta);
            values.put("IdInstruccion", IdInstruccion);
            values.put("OrdenInstruccion", OrdenInstruccion);
            values.put("TextoInstruccion", TextoInstruccion);

            IdInsert = (int) db.insert(TABLE_INSTRUCCIONES, null, values);
        }
        db.close();
        return IdInsert;
    }

    public int InsertarMenu(int IdMenu, int IdReceta, int DiaMenu, int MesMenu, int AnioMenu, String TipoMenu){
        int IdInsert = 0;
        SQLiteDatabase db = getWritableDatabase();
        if(db!=null){
            ContentValues values = new ContentValues();
            //values.put("nombre", nombre);

            values.put("IdMenu", IdMenu);
            values.put("IdReceta", IdReceta);
            values.put("DiaMenu", DiaMenu);
            values.put("MesMenu", MesMenu);
            values.put("AnioMenu", AnioMenu);
            values.put("TipoMenu", TipoMenu);

            IdInsert = (int) db.insert(TABLE_RECETAS, null, values);
        }
        db.close();
        return IdInsert;
    }

    public int InsertarCesta(int IdElemento, String NombreIngredienteCesta, String MedicionCesta, Double CantidadCesta, int TipoCesta, String TextoCesta){
        int IdInsert = 0;
        SQLiteDatabase db = getWritableDatabase();
        if(db!=null){
            ContentValues values = new ContentValues();
            //values.put("nombre", nombre);

            values.put("IdElemento", IdElemento);
            values.put("NombreIngredienteCesta", NombreIngredienteCesta);
            values.put("MedicionCesta", MedicionCesta);
            values.put("CantidadCesta", CantidadCesta);
            values.put("TipoCesta", TipoCesta);
            values.put("TextoCesta", TextoCesta);

            IdInsert = (int) db.insert(TABLE_RECETAS, null, values);
        }
        db.close();
        return IdInsert;
    }


    /*
    private void crearDatosIniciales(SQLiteDatabase database) {

        ContentValues cv = new ContentValues();

        //cv.put(COLUMN_CODIGO, "INF-001");

        cv.put(COL_IdReceta,"2");
        cv.put(COL_NombreReceta, "Torta de Huevo a la Mexicana");
        cv.put(COL_FotoReceta, "https://www.meals.com/ImagesRecipes/146232lrg.jpg");
        cv.put(COL_NumPersonas, "4");
        cv.put(COL_TiempoPreparacion, "28");
        cv.put(COL_Categoria, "Sandwiches");
        cv.put(COL_Dificultad, "2");

        database.insert("Recetas", null, cv);

        cv.clear();

        cv.put(COL_IdReceta,"4");
        cv.put(COL_NombreReceta, "Salsa de Canela y Vainilla Para Frutas Frescas");
        cv.put(COL_FotoReceta, "https://www.meals.com/ImagesRecipes/121609lrg.jpg");
        cv.put(COL_NumPersonas, "6");
        cv.put(COL_TiempoPreparacion, "10");
        cv.put(COL_Categoria, "Postres");
        cv.put(COL_Dificultad, "1");

        database.insert("Recetas", null, cv);

    }
    */

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldDBVersion, int newDBVersion) {

    }




}
