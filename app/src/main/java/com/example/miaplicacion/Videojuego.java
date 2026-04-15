package com.example.miaplicacion;

public class Videojuego {
    private String titulo;
    private String plataforma;
    private String genero;
    private int anio;
    private double precio;

    // Constructor
    public Videojuego(String titulo, String plataforma, String genero, int anio, double precio) {
        this.titulo = titulo;
        this.plataforma = plataforma;
        this.genero = genero;
        this.anio = anio;
        this.precio = precio;
    }

    // Getters (necesarios para que el adaptador lea los datos)
    public String getTitulo() { return titulo; }
    public String getPlataforma() { return plataforma; }
    public String getGenero() { return genero; }
    public int getAnio() { return anio; }
    public double getPrecio() { return precio; }
}