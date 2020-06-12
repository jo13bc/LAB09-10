package com.miker.login.curso_x_estudiante;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.miker.login.NavDrawerActivity;
import com.miker.login.R;
import com.miker.login.ServicioCurso_X_Estudiante;
import com.miker.login.curso.Curso;
import com.miker.login.estudiante.Estudiante;

public class MatriculaActivity extends AppCompatActivity {

    private Button btn_accept;
    private Button btn_cancel;
    private ListView cursos;
    private Estudiante estudiante;
    ServicioCurso_X_Estudiante servicio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Components Matching
        matching();
        //Prepare view
        prepare_datas();
    }

    private void matching() {
        // view
        setContentView(R.layout.activity_matricula);
        //textview
        cursos = findViewById(R.id.list);
        //button
        btn_accept = findViewById(R.id.btn_accept);
        btn_cancel = findViewById(R.id.btn_cancel);
        //Service
        servicio = new ServicioCurso_X_Estudiante(getApplicationContext());
        //
//        estudiante = new Estudiante(1);
//        estudiante.setCursos(new ArrayList<Curso>());
//        estudiante.getCursos().add(new Curso(1, "Programación I", 3));
//        estudiante.getCursos().add(new Curso(2, "Calculo I", 3));
//        estudiante.getCursos().add(new Curso(3, "Ingles I", 3));
//        getIntent().putExtra("object",estudiante);

    }

    private void load_curso(Estudiante estudiante) {
        CustomAdapter adapter = new CustomAdapter(getApplicationContext(), estudiante);
        cursos.setAdapter(adapter);
    }

    private void prepare_datas() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            load_curso((Estudiante) getIntent().getSerializableExtra("object"));
            btn_accept.setOnClickListener(new View.OnClickListener() {
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
        startActivity(intent);
        finish(); //prevent go back
    }

    private void insert() {
        try {
            for (Curso curso : estudiante.getCursos()) {
                servicio.insert(estudiante, curso);
            }
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
