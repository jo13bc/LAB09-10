package com.miker.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.miker.login.carrera.CarrerasActivity;
import com.miker.login.curso.CursosActivity;
import com.miker.login.curso_x_estudiante.MatriculaActivity;
import com.miker.login.estudiante.Estudiante;
import com.miker.login.estudiante.EstudiantesActivity;

public class NavDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private SharedPreferences mPrefs;
    private Model model;
    Administrador administrador;
    Estudiante estudiante;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mPrefs = this.getSharedPreferences(getString(R.string.preference_user_key), Context.MODE_PRIVATE);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        model = (Model) getIntent().getSerializableExtra("model");
        administrador = model.getLoggedAdministrador();
        estudiante = model.getLoggedEstudiante();
        Menu menu = navigationView.getMenu();

        LinearLayout layout = (LinearLayout) navigationView.getHeaderView(0);

        if(administrador!= null){
            menu.findItem(R.id.nav_matricula).setVisible(false);
        }
        if(estudiante != null){
            menu.findItem(R.id.nav_cursos).setVisible(false);
            menu.findItem(R.id.nav_estudiantes).setVisible(false);
        }


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nav_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Intent  intent = null;
        Gson gson = new Gson();
        String json = mPrefs.getString(getString(R.string.preference_user_key), "");

        if(model == null){
            model = new Model();
        }
        int id = item.getItemId();

        if(!model.esAdmin){
            if (id == R.id.nav_matricula) {
                intent = new Intent(NavDrawerActivity.this, MatriculaActivity.class);
                NavDrawerActivity.this.startActivity(intent);
                // Handle the camera action
            }else if (id == R.id.nav_logout) {
                finish();
                intent = new Intent(NavDrawerActivity.this, LoginActivity.class);
                NavDrawerActivity.this.startActivity(intent);
            }
        }else{
            if (id == R.id.nav_cursos) {
                intent = new Intent(NavDrawerActivity.this, CursosActivity.class);
                NavDrawerActivity.this.startActivity(intent);
            } else if (id == R.id.nav_estudiantes) {
                intent = new Intent(NavDrawerActivity.this, EstudiantesActivity.class);
                NavDrawerActivity.this.startActivity(intent);
            }  else if (id == R.id.nav_logout) {
                finish();
                intent = new Intent(NavDrawerActivity.this, LoginActivity.class);
                NavDrawerActivity.this.startActivity(intent);
            }
        }
        intent.putExtra("model", model);
        startActivityForResult(intent, 0);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
