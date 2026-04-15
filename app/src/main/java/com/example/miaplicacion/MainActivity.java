package com.example.miaplicacion;

import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Videojuego> listaJuegos;
    private JuegoAdapter adaptador;
    private ListView lvJuegos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Mi Colección");
        }

        // 1. Inicializar vistas y listas
        lvJuegos = findViewById(R.id.listViewJuegos);
        listaJuegos = new ArrayList<>();

        // Datos iniciales
        listaJuegos.add(new Videojuego("Zelda: TotK", "Switch", "Aventura", 2023, 59.99));

        adaptador = new JuegoAdapter(this, listaJuegos);
        lvJuegos.setAdapter(adaptador);

        // 2. Configurar el botón flotante (Asegúrate de tener el ID en tu XML)
        View btnAdd = findViewById(R.id.btnFlotanteAgregar);
        if (btnAdd != null) {
            btnAdd.setOnClickListener(v -> mostrarDialogoAgregar());
        }

        // 3. Configurar la SELECCIÓN MÚLTIPLE
        lvJuegos.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        lvJuegos.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                mode.setTitle(lvJuegos.getCheckedItemCount() + " seleccionados");
                adaptador.notifyDataSetChanged(); // Para que el adaptador pinte el fondo gris
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                getMenuInflater().inflate(R.menu.menu_contextual, menu);
                return true;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                if (item.getItemId() == R.id.itemEliminar) {
                    borrarSeleccionados();
                    mode.finish();
                    return true;
                }
                return false;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) { return false; }

            @Override
            public void onDestroyActionMode(ActionMode mode) { }
        });

        // Configurar ELIMINAR con clic largo (opcional si ya tienes el multi-select)
        lvJuegos.setOnItemLongClickListener((adapterView, view, i, l) -> {
            lvJuegos.setItemChecked(i, true);
            return true;
        });
    }
    private void borrarSeleccionados() {
        SparseBooleanArray seleccionados = lvJuegos.getCheckedItemPositions();
        for (int i = seleccionados.size() - 1; i >= 0; i--) {
            if (seleccionados.valueAt(i)) {
                int posicion = seleccionados.keyAt(i);
                listaJuegos.remove(posicion);
            }
        }
        adaptador.notifyDataSetChanged();
        Toast.makeText(this, "Registros eliminados", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.itemAgregar) {
            mostrarDialogoAgregar();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void mostrarDialogoAgregar() {
        View view = getLayoutInflater().inflate(R.layout.dialog_juego, null);
        EditText etTitulo = view.findViewById(R.id.etTitulo);
        EditText etPlataforma = view.findViewById(R.id.etPlataforma);
        EditText etGenero = view.findViewById(R.id.etGenero);
        EditText etAnio = view.findViewById(R.id.etAnio);
        EditText etPrecio = view.findViewById(R.id.etPrecio);

        new AlertDialog.Builder(this)
                .setTitle("Nuevo Videojuego")
                .setView(view)
                .setPositiveButton("Guardar", (dialogInterface, i) -> {
                    try {
                        String t = etTitulo.getText().toString();
                        String p = etPlataforma.getText().toString();
                        String g = etGenero.getText().toString();
                        int a = Integer.parseInt(etAnio.getText().toString().isEmpty() ? "0" : etAnio.getText().toString());
                        double pre = Double.parseDouble(etPrecio.getText().toString().isEmpty() ? "0.0" : etPrecio.getText().toString());

                        listaJuegos.add(new Videojuego(t, p, g, a, pre));
                        adaptador.notifyDataSetChanged();
                    } catch (Exception e) {
                        Toast.makeText(this, "Error en los datos", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
}