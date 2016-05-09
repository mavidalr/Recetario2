package com.example.mache.recetario2;

public class Data_Model{

    // Getter and Setter model for recycler view items
    private String title;
    private int image;

    //nuevo
    private String categoria;

    public Data_Model(String title,  int image, String categoria) {

        this.title = title;

        this.image = image;
        //nuevo
        this.categoria = categoria;
    }

    public String getTitle() {
        return title;
    }
    public int getImage() {
        return image;
    }
    //nuevo
    public String getCategoria() {
        return categoria;
    }
}