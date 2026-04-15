package com.example.miaplicacion;

import android.content.Context;
import android.graphics.Color; // IMPORTANTE: Para usar Color.LTGRAY
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView; // IMPORTANTE: Para usar ListView
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class JuegoAdapter extends ArrayAdapter<Videojuego> {
    private Context context;
    private List<Videojuego> juegos;

    public JuegoAdapter(@NonNull Context context, @NonNull List<Videojuego> juegos) {
        super(context, R.layout.item_juego, juegos);
        this.context = context;
        this.juegos = juegos;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // 1. Obtener el objeto para esta posición
        Videojuego juego = juegos.get(position);

        // 2. "Inflar" el diseño de la fila si es necesario
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_juego, parent, false);
        }

        // 3. Vincular los datos (Los 5 atributos)
        TextView txtTitulo = convertView.findViewById(R.id.txtTitulo);
        TextView txtPlataforma = convertView.findViewById(R.id.txtPlataforma);
        TextView txtGeneroAnio = convertView.findViewById(R.id.txtGeneroAnio);
        TextView txtPrecio = convertView.findViewById(R.id.txtPrecio);

        if (juego != null) {
            txtTitulo.setText(juego.getTitulo());
            txtPlataforma.setText("Plataforma: " + juego.getPlataforma());
            txtGeneroAnio.setText(juego.getGenero() + " | Año: " + juego.getAnio());
            txtPrecio.setText("Precio: $" + juego.getPrecio());
        }

        // Esto cambia el fondo si el elemento está seleccionado en el ListView
        if (parent instanceof ListView) {
            ListView lv = (ListView) parent;
            if (lv.isItemChecked(position)) {
                convertView.setBackgroundColor(Color.LTGRAY); // Color gris cuando se selecciona
            } else {
                convertView.setBackgroundColor(Color.TRANSPARENT); // Normal
            }
        }

        return convertView;
    }
}