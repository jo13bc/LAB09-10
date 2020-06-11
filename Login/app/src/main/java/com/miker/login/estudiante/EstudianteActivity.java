package com.miker.login.estudiante;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.miker.login.R;
import com.miker.login.curso.Curso;
import com.miker.login.curso.CursosActivity;

public class EstudianteActivity extends AppCompatActivity {

    private FloatingActionButton btn_insert_update;
    private TextView id;
    private TextView nombre;
    private TextView apell2;
    private TextView apell1;
    private TextView edad;
    private TextView user;
    private TextView pass;

   private Estudiante estudiante = new Estudiante();

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
        setContentView(R.layout.activity_estudiante);
        //textview

        id= findViewById(R.id.id);
        nombre= findViewById(R.id.nombre);
        apell2= findViewById(R.id.apell2);
        apell1= findViewById(R.id.apell1);
        edad= findViewById(R.id.edad);
        user= findViewById(R.id.user);
        pass= findViewById(R.id.pass);

        //button
        btn_insert_update = findViewById(R.id.btn_insert_update);
        btn_insert_update.show();
    }

    private void load_estudiante(Estudiante estudiante) {
        this.estudiante.setId(estudiante.getId());
        nombre.setText(estudiante.getNombre());
        apell1.setText(estudiante.getApellido1());
         apell2.setText(estudiante.getApellido2());
         edad.setText(estudiante.getEdad());
         user.setText(estudiante.getUser());
         pass.setText(estudiante.getPassword());
    }

    private void clean() {
        id.setText("");
        nombre.setText("");
        apell1.setText("");
        apell2.setText("");
        edad.setText("");
        user.setText("");
        pass.setText("");
        estudiante = new Estudiante();
    }

    private void prepare_datas() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            load_estudiante((Estudiante) getIntent().getSerializableExtra("object"));
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
            Intent intent = new Intent(getBaseContext(), EstudiantesActivity.class);
            intent.putExtra(type, estudiante);
            startActivity(intent);
            finish(); //prevent go back
        }
    }

    public boolean validateForm() {
        int error = 0;
        if (TextUtils.isEmpty(this.id.getText())) {
            id.setError("Id requerido");
            error++;
        }
        if (TextUtils.isEmpty(this.nombre.getText())) {
            nombre.setError("Nombre requerido");
            error++;
        }
        if (TextUtils.isEmpty(this.apell1.getText())) {
            apell1.setError("Apellido requerido");
            error++;
        }
        if (TextUtils.isEmpty(this.apell2.getText())) {
            apell2.setError("Apellido requerido");
            error++;
        }
        if (TextUtils.isEmpty(this.edad.getText())) {
            edad.setError("Edad requerida");
            error++;
        }
        if (TextUtils.isEmpty(this.user.getText())) {
            user.setError("Usuario requerido");
            error++;
        }
        if (TextUtils.isEmpty(this.pass.getText())) {
            pass.setError("Contraseña requerida");
            error++;
        }
        if (error > 0) {
            Toast.makeText(getApplicationContext(), "Algunos errores", Toast.LENGTH_LONG).show();
            return false;
        } else {
            estudiante.setId(Integer.parseInt(id.getText().toString()));
            estudiante.setNombre(nombre.getText().toString());
            estudiante.setApellido1(apell1.getText().toString());
            estudiante.setApellido2(apell2.getText().toString());
            estudiante.setEdad(Integer.parseInt(edad.getText().toString()));
            estudiante.setUser(user.getText().toString());
            estudiante.setPassword(pass.getText().toString());
        }
        return true;
    }
}