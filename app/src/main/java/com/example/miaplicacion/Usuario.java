package com.example.miaplicacion;

public class Usuario {
    private int id;
    private String nombre;
    private String passwd;

    // Constructor
    public Usuario(int id, String nombre, String passwd) {
        this.id = id;
        this.nombre = nombre;
        this.passwd = passwd;
    }

    public Usuario() {}

    // Getters (necesarios para que el adaptador lea los datos)
    public int getId() { return id; }
    public void setId(int id) {this.id = id;}
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) {this.nombre = nombre;}
    public String getPasswd() { return passwd; }
    public void setPasswd(String passwd) {this.passwd = passwd;}
}
