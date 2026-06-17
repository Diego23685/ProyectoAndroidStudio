package com.example.miaplicacion;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private static final String NOMBRE_DB = "vg.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLA_VIDEOJUEGOS = "videojuegos";

    private static final String VALOR_TITULO = "titulo";
    private static final String VALOR_PLATAFORMA = "plataforma";
    private static final String VALOR_GENERO = "genero";
    private static final String VALOR_ANIO = "anio";
    private static final String VALOR_PRECIO = "precio";

    public DBHelper(Context context){
        super(context, NOMBRE_DB, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE ="CREATE TABLE " + TABLA_VIDEOJUEGOS + "(" +
                VALOR_TITULO + " TEXT PRIMARY KEY, " +
                VALOR_PLATAFORMA + " TEXT, " +
                VALOR_GENERO + " TEXT, " +
                VALOR_ANIO + " INT, " +
                VALOR_PRECIO + " DOUBLE)";
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLA_VIDEOJUEGOS);
        onCreate(sqLiteDatabase);
    }

    public long agregarVideojeugo(Videojuego vg) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(VALOR_TITULO, vg.getTitulo());
        contentValues.put(VALOR_PLATAFORMA, vg.getPlataforma());
        contentValues.put(VALOR_GENERO, vg.getGenero());
        contentValues.put(VALOR_ANIO, vg.getAnio());
        contentValues.put(VALOR_PRECIO, vg.getPrecio());

        long id = db.insert(TABLA_VIDEOJUEGOS, null, contentValues);
        return id;
    }

    public ArrayList<Videojuego> leerTodosLosVideojuego() {
        ArrayList<Videojuego> list = new ArrayList<>();
        String query = "SELECT * FROM " + TABLA_VIDEOJUEGOS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Videojuego vg = new Videojuego();
                vg.setTitulo(cursor.getString(0));
                vg.setPlataforma(cursor.getString(1));
                vg.setGenero(cursor.getString(2));
                vg.setAnio(cursor.getInt(3));
                vg.setPrecio(cursor.getDouble(4));
                list.add(vg);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return list;
    }

    public int actualizarVideojuego(Videojuego vg) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(VALOR_TITULO, vg.getTitulo());
        contentValues.put(VALOR_PLATAFORMA, vg.getPlataforma());
        contentValues.put(VALOR_GENERO, vg.getGenero());
        contentValues.put(VALOR_ANIO, vg.getAnio());
        contentValues.put(VALOR_PRECIO, vg.getPrecio());

        return db.update(TABLA_VIDEOJUEGOS, contentValues, VALOR_TITULO + " = ?", new String[]{vg.getTitulo()});
    }

    public void eliminarVideojuego(Videojuego vg) {
        SQLiteDatabase db =this.getWritableDatabase();
        db.delete(TABLA_VIDEOJUEGOS, VALOR_TITULO + " = ?", new String[]{vg.getTitulo()});
    }
}
