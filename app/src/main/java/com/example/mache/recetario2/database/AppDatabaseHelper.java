package com.example.mache.recetario2.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


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
    public static final String TABLE_RECETAS= "Recetas";
    public static final String COL_IdReceta = "IdReceta";
    public static final String COL_FotoReceta = "FotoReceta";
    public static final String COL_NumPersonas = "NumPersonas";
    public static final String COL_TiempoPreparacion = "TiempoPreparacion";
    public static final String COL_Categoria = "Categoria";
    public static final String COL_Dificultad = "Dificultad";
    public static final String COL_NombreReceta = "NombreReceta";
    //public static final String COL_ = "";

/*
    public static final String TABLE_RAMOS= "Recetas";
    public static final String COLUMN_CODIGO = "codigo";
    public static final String COLUMN_NOMBRE = "nombre";
    public static final String COLUMN_DESCRIPCION = "descripcion";
    public static final String COLUMN_PROFESOR = "profesor";
*/
    public AppDatabaseHelper(Context context) {

        super(context, DATABASE_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {

        database.execSQL(CREATE_TABLE_RECETAS);
        crearDatosIniciales(database);
    }

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

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldDBVersion, int newDBVersion) {

    }
}
