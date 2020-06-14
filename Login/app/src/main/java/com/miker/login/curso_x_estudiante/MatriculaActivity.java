package com.miker.login.curso_x_estudiante;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.miker.login.NavDrawerActivity;
import com.miker.login.R;
import com.miker.login.ServicioMatricula;
import com.miker.login.Usuario;
import com.miker.login.curso.Curso;
import com.miker.login.estudiante.Estudiante;
import com.miker.login.oferta.curso.Oferta;
import com.miker.login.oferta.curso.OfertaAdapter;

import java.util.ArrayList;
import java.util.List;

public class MatriculaActivity extends AppCompatActivity implements CustomAdapter.CustomAdapterListener {

    private Button btn_accept;
    private Button btn_cancel;
    private ListView cursosList;
    private TextView titlename;
    private Usuario usuario;
    private ServicioMatricula servicio;
    private List<Curso> cursos;
    private boolean matricula = false;

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
        if (getIntent().getExtras().getSerializable("cursos") == null) {
            titlename.setText("Historico de Cursos Matriculados");
            btn_accept.setVisibility(View.INVISIBLE);
            btn_cancel.setVisibility(View.INVISIBLE);
        } else {
            matricula = true;
            titlename.setText("Lista de Cursos a Matricular");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void matching() {
        // view
        setContentView(R.layout.activity_matricula);
        //textview
        titlename = findViewById(R.id.titlename);
        cursosList = findViewById(R.id.list);
        //button
        btn_accept = findViewById(R.id.btn_accept);
        btn_cancel = findViewById(R.id.btn_cancel);
        //Service
        try {
            servicio = ServicioMatricula.getServicio(getApplicationContext());
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void load_curso(Usuario usuario, List<Curso> cursos) {
        try {
            if (cursos == null) {
                this.cursos = servicio.list((Estudiante) usuario);
                titlename.setText("Historico de Cursos Matriculados");
                btn_accept.setVisibility(View.INVISIBLE);
                btn_cancel.setVisibility(View.INVISIBLE);
            } else {
                matricula = true;
                this.cursos = cursos;
                titlename.setText("Lista de Cursos a Matricular");
            }
            CustomAdapter adapter = new CustomAdapter(getApplicationContext(), this.cursos, this);
            cursosList.setAdapter(adapter);
            if (this.cursos.isEmpty()) {
                if (cursos == null) {
                    Toast.makeText(
                            getApplicationContext(),
                            "El estudiante no tiene ningún curso matriculado.",
                            Toast.LENGTH_LONG
                    ).show();
                } else {
                    Toast.makeText(
                            getApplicationContext(),
                            "No se permite una matricula sin cursos.",
                            Toast.LENGTH_LONG
                    ).show();
                    btn_accept.setEnabled(false);
                }
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
            cursos = (ArrayList<Curso>) getIntent().getExtras().getSerializable("cursos");
            usuario = (Usuario) getIntent().getExtras().getSerializable("usuario");
            load_curso(usuario, cursos);
            btn_accept.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onClick(View view) {
                    insert();
                    exit();
                }
            });
            btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    exit();
                }
            });
        }
    }

    private void exit() {
        Intent intent = new Intent(getBaseContext(), NavDrawerActivity.class);
        intent.putExtra("usuario", getIntent().getExtras().getSerializable("usuario"));
        startActivity(intent);
        finish(); //prevent go back
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void insert() {
        try {
            Estudiante estudiante = ((Estudiante) usuario);
            for (Curso curso : cursos) {
                servicio.insert(estudiante, curso);
            }
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onSelected(Curso curso) {
        if (matricula) {
            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.activity_matricula);
            ListView lv = (ListView ) dialog.findViewById(R.id.list);
            lv.setAdapter(new CustomAdapter(getApplicationContext(), this.cursos, this));
            dialog.setCancelable(true);
            dialog.setTitle("Confirmación de Acción");
            dialog.setCancelable(true);
            dialog.show();

            setContentView(R.layout.activity_matricula);
        } else {
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
}
