package com.example.miaplicacion;

public class Videojuego {
    private int id;
    private String titulo;
    private String plataforma;
    private String genero;
    private int anio;
    private double precio;

    // Constructor
    public Videojuego(int id, String titulo, String plataforma, String genero, int anio, double precio) {
        this.id = id;
        this.titulo = titulo;
        this.plataforma = plataforma;
        this.genero = genero;
        this.anio = anio;
        this.precio = precio;
    }

    public Videojuego() {}

    // Getters (necesarios para que el adaptador lea los datos)
    public int getId() { return id; }
    public void setId(int id) {this.id = id;}
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) {this.titulo = titulo;}
    public String getPlataforma() { return plataforma; }
    public void setPlataforma(String plataforma) {this.plataforma = plataforma;}
    public String getGenero() { return genero; }
    public void setGenero(String genero) {this.genero = genero;}
    public int getAnio() { return anio; }
    public void setAnio(int anio) {this.anio = anio;}
    public double getPrecio() { return precio; }
    public void setPrecio(double precio) {this.precio = precio;}
}