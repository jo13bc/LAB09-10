package com.miker.login;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Toast;

import com.miker.login.estudiante.Estudiante;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class LoginActivity extends AppCompatActivity {

    private EditText user;
    private EditText password;
    private Usuario usuario = null;
    private ServicioEstudiante servicio;
    private List<Estudiante> cursoList;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // initiate a button
        ImageButton loginButton = (ImageButton) findViewById(R.id.btn);
        user = (EditText) findViewById(R.id.user);
        password = (EditText) findViewById(R.id.password);
        // perform click event on the button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    usuario = ServicioEstudiante.getServicio(getApplicationContext()).login(new Estudiante(user.getText().toString(), password.getText().toString()));
                    if (usuario == null) {
                        if (user.getText().toString().equals("admin") && password.getText().toString().equals("admin")) {
                            usuario = new Administrador("admin", "admin", "admin");
                        } else {
                            Toast.makeText(getApplicationContext(), "¡Datos no encontrados!", Toast.LENGTH_LONG).show();  // display a toast message
                        }
                    }
                    if(usuario != null) openActivity(usuario);
                } catch (Exception ex) {
                    Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        try {
            servicio = ServicioEstudiante.getServicio(getApplicationContext());
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();  // display a toast message
        }
    }

    private void openActivity(Usuario usuario) {
        Intent intent;
        String message;
        if (usuario.isSuperUser()) {
            intent = new Intent(getApplicationContext(), NavDrawerActivity.class);
            message = "Administrador";
        } else {
            intent = new Intent(getApplicationContext(), NavDrawerActivity.class);
            message = "Estudiante";
        }
        intent.putExtra("usuario", usuario);
        startActivityForResult(intent, 0);
        Toast.makeText(getApplicationContext(), "¡Bienvenido " + message + "!", Toast.LENGTH_LONG).show();  // display a toast message
    }
}
