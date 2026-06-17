package com.example.miaplicacion;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Videojuego> listaJuegos;
    private JuegoAdapter adaptador;
    private ListView lvJuegos;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Mi Colección");
        }

        lvJuegos = findViewById(R.id.listViewJuegos);
        listaJuegos = new ArrayList<>();
        dbHelper = new DBHelper(this);

        cargarVideojuegos();

        View botonAgregar = findViewById(R.id.btnFlotanteAgregar);
        if (botonAgregar != null) {
            botonAgregar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mostrarDialogoAgregar();
                }
            });
        }

        // 1. Configuramos la lista para permitir selección múltiple
        lvJuegos.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

        // 2. Agregamos el "Listener" que maneja la selección y los botones superiores
        lvJuegos.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuItem editItem = menu.add(Menu.NONE, 1, Menu.NONE, "Editar");
                editItem.setIcon(android.R.drawable.ic_menu_edit);
                editItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

                MenuItem deleteItem = menu.add(Menu.NONE, 2, Menu.NONE, "Borrar");
                deleteItem.setIcon(android.R.drawable.ic_menu_delete);
                deleteItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

                return true;
            }

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                int seleccionados = lvJuegos.getCheckedItemCount();
                mode.setTitle(seleccionados + " seleccionados");

                MenuItem editItem = mode.getMenu().findItem(1);
                if (editItem != null) {
                    editItem.setVisible(seleccionados == 1);
                }
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case 1: // Editar
                        SparseBooleanArray seleccionadosEditar = lvJuegos.getCheckedItemPositions();
                        for (int i = 0; i < seleccionadosEditar.size(); i++) {
                            if (seleccionadosEditar.valueAt(i)) {
                                int posicionParaEditar = seleccionadosEditar.keyAt(i);
                                mostrarDialogoEdicion(posicionParaEditar, mode);
                                break;
                            }
                        }
                        return true;

                    case 2: // Borrar
                        SparseBooleanArray seleccionadosBorrar = lvJuegos.getCheckedItemPositions();
                        for (int i = lvJuegos.getCount() - 1; i >= 0; i--) {
                            if (seleccionadosBorrar.get(i)) {
                                dbHelper.eliminarVideojuego((Videojuego) listaJuegos.toArray()[i]);
                                listaJuegos.remove(i);
                            }
                        }
                        adaptador.notifyDataSetChanged();
                        mode.finish();
                        Toast.makeText(MainActivity.this, "Juegos eliminados", Toast.LENGTH_SHORT).show();
                        return true;

                    default:
                        return false;
                }
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) { return false; }

            @Override
            public void onDestroyActionMode(ActionMode mode) { }
        });
    }

    private void cargarVideojuegos() {
        listaJuegos = dbHelper.leerTodosLosVideojuego();

        adaptador = new JuegoAdapter(this, listaJuegos);
        lvJuegos.setAdapter(adaptador);
    }

    private void mostrarDialogoAgregar() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Nuevo Videojuego");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);

        final EditText inputTitulo = new EditText(this);
        inputTitulo.setHint("Título");
        layout.addView(inputTitulo);

        final EditText inputPlataforma = new EditText(this);
        inputPlataforma.setHint("Plataforma");
        layout.addView(inputPlataforma);

        final EditText inputGenero = new EditText(this);
        inputGenero.setHint("Género");
        layout.addView(inputGenero);

        final EditText inputAnio = new EditText(this);
        inputAnio.setHint("Año");
        inputAnio.setInputType(InputType.TYPE_CLASS_NUMBER);
        layout.addView(inputAnio);

        final EditText inputPrecio = new EditText(this);
        inputPrecio.setHint("Precio");
        inputPrecio.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        layout.addView(inputPrecio);

        builder.setView(layout);

        builder.setPositiveButton("Agregar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Videojuego nuevoJuego = new Videojuego();

                nuevoJuego.setTitulo(inputTitulo.getText().toString());
                nuevoJuego.setPlataforma(inputPlataforma.getText().toString());
                nuevoJuego.setGenero(inputGenero.getText().toString());

                try {
                    nuevoJuego.setAnio(Integer.parseInt(inputAnio.getText().toString()));
                    nuevoJuego.setPrecio(Double.parseDouble(inputPrecio.getText().toString()));
                } catch (NumberFormatException e) {
                    nuevoJuego.setAnio(0);
                    nuevoJuego.setPrecio(0.0);
                    Toast.makeText(MainActivity.this, "Números vacíos o incorrectos. Se guardó como 0.", Toast.LENGTH_SHORT).show();
                }

                listaJuegos.add(nuevoJuego);
                dbHelper.agregarVideojeugo(nuevoJuego);
                adaptador.notifyDataSetChanged();
                Toast.makeText(MainActivity.this, "¡Juego agregado!", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void mostrarDialogoEdicion(final int posicion, final ActionMode mode) {
        final Videojuego juegoSeleccionado = listaJuegos.get(posicion);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Editar Videojuego");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);

        final EditText inputPlataforma = new EditText(this);
        inputPlataforma.setHint("Plataforma");
        inputPlataforma.setText(juegoSeleccionado.getPlataforma());
        layout.addView(inputPlataforma);

        final EditText inputGenero = new EditText(this);
        inputGenero.setHint("Género");
        inputGenero.setText(juegoSeleccionado.getGenero());
        layout.addView(inputGenero);

        final EditText inputAnio = new EditText(this);
        inputAnio.setHint("Año");
        inputAnio.setInputType(InputType.TYPE_CLASS_NUMBER);
        inputAnio.setText(String.valueOf(juegoSeleccionado.getAnio()));
        layout.addView(inputAnio);

        final EditText inputPrecio = new EditText(this);
        inputPrecio.setHint("Precio");
        inputPrecio.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        inputPrecio.setText(String.valueOf(juegoSeleccionado.getPrecio()));
        layout.addView(inputPrecio);

        builder.setView(layout);

        builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                juegoSeleccionado.setPlataforma(inputPlataforma.getText().toString());
                juegoSeleccionado.setGenero(inputGenero.getText().toString());

                try {
                    juegoSeleccionado.setAnio(Integer.parseInt(inputAnio.getText().toString()));
                    juegoSeleccionado.setPrecio(Double.parseDouble(inputPrecio.getText().toString()));
                } catch (NumberFormatException e) {
                    Toast.makeText(MainActivity.this, "Revisa los números, el formato es incorrecto", Toast.LENGTH_SHORT).show();
                }

                dbHelper.actualizarVideojuego(juegoSeleccionado);

                adaptador.notifyDataSetChanged();
                mode.finish();
                Toast.makeText(MainActivity.this, "¡Juego actualizado!", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                mode.finish();
            }
        });

        builder.show();
    }
}