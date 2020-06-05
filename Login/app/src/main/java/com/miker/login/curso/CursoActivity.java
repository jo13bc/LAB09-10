package com.miker.login.curso;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.miker.login.R;
import com.miker.login.carrera.Carrera;

public class CursoActivity extends AppCompatActivity {

    private FloatingActionButton btn_insert_update;
    private TextView codigo;
    private TextView nombre;
    private TextView creditos;
    private TextView horas_semana;
    private TextView anno;
    private TextView ciclo;
    private TextView carrera;
    private Curso curso = new Curso();

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
        setContentView(R.layout.activity_curso);
        //textview
        codigo = findViewById(R.id.codigo);
        nombre = findViewById(R.id.nombre);
        creditos = findViewById(R.id.creditos);
        horas_semana = findViewById(R.id.hora_semana);
        ciclo = findViewById(R.id.ciclo);
        carrera = findViewById(R.id.carrera);
        anno = findViewById(R.id.anno);
        //button
        btn_insert_update = findViewById(R.id.btn_insert_update);
        btn_insert_update.show();
    }

    private void load_curso(Curso curso) {
        this.curso.setId(curso.getId());
        codigo.setText(curso.getCodigo());
        nombre.setText(curso.getNombre());
        creditos.setText(String.valueOf(curso.getCreditos()));
        horas_semana.setText(String.valueOf(curso.getHora_semana()));
        anno.setText(String.valueOf(curso.getAnno()));
        ciclo.setText(String.valueOf(curso.getCiclo().getId()));
        carrera.setText(String.valueOf(curso.getCarrera().getId()));
    }

    private void clean() {
        codigo.setText("");
        nombre.setText("");
        creditos.setText("");
        horas_semana.setText("");
        anno.setText("");
        ciclo.setText("");
        carrera.setText("");
        curso = new Curso();
    }

    private void prepare_datas() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            load_curso((Curso) getIntent().getSerializableExtra("object"));
            btn_insert_update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    insert_update("update");
                }
            });
        } else {
            clean();
            btn_insert_update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    insert_update("insert");
                }
            });
        }
    }

    private void insert_update(String type) {
        if (validateForm()) {
            Intent intent = new Intent(getBaseContext(), CursosActivity.class);
            intent.putExtra(type, curso);
            startActivity(intent);
            finish(); //prevent go back
        }
    }

    public boolean validateForm() {
        int error = 0;
        if (TextUtils.isEmpty(this.codigo.getText())) {
            codigo.setError("Código requerido");
            error++;
        }
        if (TextUtils.isEmpty(this.nombre.getText())) {
            nombre.setError("Nombre requerido");
            error++;
        }
        if (TextUtils.isEmpty(this.creditos.getText())) {
            creditos.setError("Cantidad de creditos requerida");
            error++;
        }
        if (TextUtils.isEmpty(this.horas_semana.getText())) {
            horas_semana.setError("Horas a la semana requeridas");
            error++;
        }
        if (TextUtils.isEmpty(this.anno.getText())) {
            anno.setError("Año requerido");
            error++;
        }
        if (TextUtils.isEmpty(this.ciclo.getText())) {
            ciclo.setError("Ciclo requerido");
            error++;
        }
        if (TextUtils.isEmpty(this.carrera.getText())) {
            carrera.setError("Carrera requerida");
            error++;
        }
        if (error > 0) {
            Toast.makeText(getApplicationContext(), "Algunos errores", Toast.LENGTH_LONG).show();
            return false;
        } else {
            curso.setCodigo(codigo.getText().toString());
            curso.setNombre(nombre.getText().toString());
            curso.setCreditos(Integer.parseInt(creditos.getText().toString()));
            curso.setHora_semana(Integer.parseInt(horas_semana.getText().toString()));
            curso.setAnno(Integer.parseInt(anno.getText().toString()));
            curso.setCiclo(new Ciclo(Integer.parseInt(ciclo.getText().toString())));
            curso.setCarrera(new Carrera(Integer.parseInt(carrera.getText().toString())));
        }
        return true;
    }
}
