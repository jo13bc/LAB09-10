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


public class EstudianteActivity extends AppCompatActivity {

    private FloatingActionButton btn_insert_update;
    private TextView edad;
    private TextView nombre;
    private TextView apell1;
    private TextView apell2;
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
        nombre = findViewById(R.id.nombre);
        apell1 = findViewById(R.id.apell1);
        apell2 = findViewById(R.id.apell2);
        edad = findViewById(R.id.edad);
        user = findViewById(R.id.user);
        pass = findViewById(R.id.pass);
        //button
        btn_insert_update = findViewById(R.id.btn_insert_update);
        btn_insert_update.show();
    }

    private void load_estudiantes(Estudiante estudiante) {
        this.estudiante.setId(estudiante.getId());
        nombre.setText(estudiante.getNombre());
        apell1.setText(estudiante.getApell1());
        apell2.setText(estudiante.getApell2());
        edad.setText(estudiante.getEdad());
        user.setText(estudiante.getUser());
        pass.setText(estudiante.getPassword());
    }

    private void clean() {
        nombre.setText("");

    }

    private void prepare_datas() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            load_estudiantes((Estudiante) getIntent().getSerializableExtra("object"));
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
        if (TextUtils.isEmpty(this.nombre.getText())) {
            nombre.setError("Nombre requerido");
            error++;
        }
         if (error > 0) {
            Toast.makeText(getApplicationContext(), "Algunos errores", Toast.LENGTH_LONG).show();
            return false;
        } else {
             estudiante.setNombre(nombre.getText().toString());
             estudiante.setApell1(apell1.getText().toString());
        }
        return true;
    }
}
