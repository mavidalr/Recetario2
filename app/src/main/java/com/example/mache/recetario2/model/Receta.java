package com.example.mache.recetario2.model;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class Receta {
	private String title, thumbnailUrl;
	private int year;
	private double rating;
	private ArrayList<String> genre;

    //nuevo
	private int IdReceta, NumPersonas,TiempoPraparacion,Dificultad;
    private String NombreReceta, FotoReceta, Categoria, Fecha;
    private String URL;

	public Receta() {
	}

	/*public Receta(String name, String thumbnailUrl, int year, double rating,
				 ArrayList<String> genre) {
		this.title = name;
		this.thumbnailUrl = thumbnailUrl;
		this.year = year;
		this.rating = rating;
		this.genre = genre;
	}*/


    public Receta(String NombreReceta, String FotoReceta, String Categoria, int IdReceta, int NumPersonas, int TiempoPraparacion, int Dificultad, String Fecha) {
        this.NombreReceta = NombreReceta;
        this.FotoReceta = FotoReceta;
        this.Categoria = Categoria;
        this.IdReceta = IdReceta;
        this.NumPersonas = NumPersonas;
        this.TiempoPraparacion = TiempoPraparacion;
        this.Dificultad = Dificultad;
        this.URL = FotoReceta;
        this.Fecha = Fecha;

    }
    //nuevos get
    public String getNombreReceta()
    {
        return NombreReceta;
    }
    public String getFotoReceta()
    {
        return FotoReceta;
    }
    public String getCategoria()
    {
        return Categoria;
    }
    public int getIdReceta() {
        return IdReceta;
    }
    public int getNumPersonas() {
        return NumPersonas;
    }
    public int getTiempoPraparacion() {
        return TiempoPraparacion;
    }
    public int getDificultad() {
        return Dificultad;
    }
    public String getURL()
    {
        return URL;
    }
    public String getFecha(){return Fecha;}
    //nuevos set
    public void setNombreReceta(String NombreReceta)
    {
        this.NombreReceta = NombreReceta;
    }
    public void setFotoReceta(String FotoReceta)
    {
        this.FotoReceta = FotoReceta;
    }
    public void setCategoria(String Categoria)
    {
        this.Categoria = Categoria;
    }
    public void setIdReceta(int IdReceta)
    {
        this.IdReceta = IdReceta;
    }
    public void setNumPersonas(int NumPersonas)
    {
        this.NumPersonas = NumPersonas;
    }
    public void setTiempoPraparacion(int TiempoPreparacion)
    {
        this.TiempoPraparacion = TiempoPreparacion;
    }
    public void setDificultad(int Dificultad)
    {
        this.Dificultad = Dificultad;
    }
    public void setURL(String URL)
    {
        this.URL = URL;
    }
    public void setFecha(String Fecha)
    {
        this.Fecha = Fecha;
    }




/*
    public String getTitle() {
		return title;
	}

	public void setTitle(String name) {
		this.title = name;
	}

	public String getThumbnailUrl() {
		return thumbnailUrl;
	}

	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	public ArrayList<String> getGenre() {
		return genre;
	}

	public void setGenre(ArrayList<String> genre) {
		this.genre = genre;
	}
	*/

    public Bitmap getThumb() {
        return thumb;
    }

    public void setThumb(Bitmap thumb) {
        this.thumb = thumb;
    }

    Bitmap thumb;

}
