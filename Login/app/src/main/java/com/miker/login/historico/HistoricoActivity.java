package com.miker.login.historico;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.miker.login.R;
import com.miker.login.ServicioMatricula;
import com.miker.login.Usuario;
import com.miker.login.curso.Curso;
import com.miker.login.estudiante.Estudiante;

import java.util.List;

public class HistoricoActivity extends AppCompatActivity implements CustomAdapter.CustomAdapterListener {

    private ListView cursosList;
    private Usuario usuario;
    private ServicioMatricula servicio;
    private List<Curso> cursos;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Components Matching
        matching();
        //Prepare view
        prepare_datas();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void matching() {
        // view
        setContentView(R.layout.activity_historico);
        //textview
        cursosList = findViewById(R.id.list);
        //Service
        try {
            servicio = ServicioMatricula.getServicio(getApplicationContext());
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void load_curso(Usuario usuario) {
        try {
            cursos = servicio.list((Estudiante) usuario);
            CustomAdapter adapter = new CustomAdapter(getApplicationContext(), cursos, this);
            cursosList.setAdapter(adapter);
            if (cursos.isEmpty()) {
                Toast.makeText(
                        getApplicationContext(),
                        "El estudiante no tiene ningún curso matriculado.",
                        Toast.LENGTH_LONG
                ).show();
            }
        } catch (Exception ex) {
            Toast.makeText(
                    getApplicationContext(),
                    "Hubo un problema al leer los cursos del estudiante: " + ex.getMessage(),
                    Toast.LENGTH_LONG
            ).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void prepare_datas() {
        Bundle extras = getIntent().getExtras();
        if (extras.getSerializable("usuario") != null) {
            usuario = (Usuario) getIntent().getExtras().getSerializable("usuario");
            load_curso(usuario);
        }
    }

    @Override
    public void onSelected(Curso curso) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("¿Esta seguro que desea desmatricular este curso?")
                .setTitle("Confirmación de Acción");
        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            public void onClick(DialogInterface dialog, int id) {
                servicio.delete((Estudiante) usuario, curso);
                Toast.makeText(getApplicationContext(), "Se desmatriculó exitosamente el curso " + curso.getDescripcion(), Toast.LENGTH_LONG).show();
                prepare_datas();
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(getApplicationContext(), "Se canceló la desmatriculación de " + curso.getDescripcion(), Toast.LENGTH_LONG).show();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
