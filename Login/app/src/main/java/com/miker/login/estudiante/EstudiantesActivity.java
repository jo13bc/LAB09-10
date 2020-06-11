package com.miker.login.estudiante;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.miker.login.Helper.RecyclerItemTouchHelper;
import com.miker.login.Model;
import com.miker.login.NavDrawerActivity;
import com.miker.login.R;
import com.miker.login.Servicio;
import com.miker.login.ServicioCurso;
import com.miker.login.ServicioEstudiante;
import com.miker.login.curso.Curso;
import com.miker.login.curso.CursoActivity;
import com.miker.login.curso.CursosActivity;
import com.miker.login.curso.CursosAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static com.miker.login.EncodingUtil.encodeURIComponent;
public class EstudiantesActivity extends AppCompatActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener, EstudiantesAdapter.EstudianteAdapterListener{

    private RecyclerView recyclerView;
    private EstudiantesAdapter adapter;
    private List<Estudiante> estudiantesList;
    private Estudiante deleteEstudiante;
    private CoordinatorLayout coordinatorLayout;
    private SearchView searchView;
    private FloatingActionButton btn_insert;
    private Model model;
    private ProgressDialog progressDialog;
    private ServicioEstudiante servicio;
    private String message;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cursos);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        servicio = new ServicioEstudiante(getApplicationContext());

        //toolbar fancy stuff
        getSupportActionBar().setTitle(getString(R.string.item_cursos));

        recyclerView = findViewById(R.id.recycler_view);
        estudiantesList = new ArrayList<>();
        model= (Model) getIntent().getSerializableExtra("model");
        coordinatorLayout = findViewById(R.id.main_content);
        estudiantesList = model.getEstudiantes(servicio);
        adapter = new EstudiantesAdapter(estudiantesList, this);

        // go to update or add career
        btn_insert = findViewById(R.id.btn_insert);
        btn_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insert_estudiante();
            }
        });

        //delete swiping left and right
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

        // white background notification bar
        whiteNotificationBar(recyclerView);

        // Receive the Carrera sent by AddUpdCarreraActivity
        EstudiantesActivity.checkIntentInformation checkIntentInformation = new EstudiantesActivity.checkIntentInformation();
        checkIntentInformation.execute();
    }


    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (direction == ItemTouchHelper.START) {
            if (viewHolder instanceof EstudiantesAdapter.MyViewHolder) {
                // get the removed item name to display it in snack bar
                deleteEstudiante = estudiantesList.get(viewHolder.getAdapterPosition());
                EstudiantesActivity.delete delete = new EstudiantesActivity.delete();
                delete.execute();
            }
        } else {
            //If is editing a row object
            Estudiante aux = adapter.getSwipedItem(viewHolder.getAdapterPosition());
            //send data to Edit Activity
            Intent intent = new Intent(this, EstudianteActivity.class);
            intent.putExtra("object", aux);
            adapter.notifyDataSetChanged(); //restart left swipe view
            startActivity(intent);
        }
    }

    @Override
    public void onItemMove(int source, int target) {
        adapter.onItemMove(source, target);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds cursoList to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);

        // Associate searchable configuration with the SearchView   !IMPORTANT
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change, every type on input
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                adapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        Intent a = new Intent(this, NavDrawerActivity.class);
        a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(a);
        super.onBackPressed();
    }

    private void whiteNotificationBar(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
            getWindow().setStatusBarColor(Color.WHITE);
        }
    }
    @Override
    public void onSelected(Estudiante estudiante) {
        Toast.makeText(getApplicationContext(), "Selected: " + estudiante.getId(), Toast.LENGTH_LONG).show();
    }

    public class checkIntentInformation extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // display a progress dialog for good user experiance
            progressDialog = new ProgressDialog(EstudiantesActivity.this);
            progressDialog.setMessage("¡Buscando Registros!");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            message = "";
            String result = "";
            try {
                Bundle extras = getIntent().getExtras();
                if (extras != null) {
                    Estudiante aux;
                    aux = (Estudiante) getIntent().getSerializableExtra("insert");
                    if (aux == null) {
                        aux = (Estudiante) getIntent().getSerializableExtra("update");
                        if (aux != null) {
                            //found an item that can be updated
                            servicio.update(aux);
                            //check if exist
                            message = aux.getId() + " actualizado correctamente";

                        }
                    } else {
                        //found a new Curso Object
                        servicio.insert(aux);
                        message = aux.getId() + " agregado correctamente";
                    }
                }
            } catch (Exception ex) {
                message = ex.getMessage();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            // dismiss the progress dialog after receiving data from API
            progressDialog.dismiss();
            //Json
            try {
                EstudiantesActivity.list task = new EstudiantesActivity.list();
                task.execute();
            } catch (Exception ex) {
                Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
            } finally {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }
        }

    }

    public class list extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // display a progress dialog for good user experiance
            progressDialog = new ProgressDialog(EstudiantesActivity.this);
            progressDialog.setMessage("¡Cargando la Lista!");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            message = "";
            String result = "";
            Estudiante est = new Estudiante();
            try {
                Cursor cursor = servicio.list();
                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        estudiantesList = new ArrayList<>();
                        do {
                            est.setId(cursor.getInt(cursor.getColumnIndex(ServicioEstudiante.estudianteEntry.ID)));
                            est.setNombre(cursor.getString(cursor.getColumnIndex(ServicioEstudiante.estudianteEntry.NOMBRE)));
                            est.setApellido1(cursor.getString(cursor.getColumnIndex(ServicioEstudiante.estudianteEntry.APELLIDO1)));
                            est.setApellido2(cursor.getString(cursor.getColumnIndex(ServicioEstudiante.estudianteEntry.APELLIDO2)));
                            est.setEdad(cursor.getInt(cursor.getColumnIndex(ServicioEstudiante.estudianteEntry.EDAD)));
                            est.setUser(cursor.getString(cursor.getColumnIndex(ServicioEstudiante.estudianteEntry.USER)));
                            est.setPassword(cursor.getString(cursor.getColumnIndex(ServicioEstudiante.estudianteEntry.PASSWORD)));
                            //
                            estudiantesList.add(est);
                        } while (cursor.moveToNext());
                    }
                    cursor.close();
                }
            } catch (Exception ex) {
                message = ex.getMessage();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            // dismiss the progress dialog after receiving data from API
            progressDialog.dismiss();
            //Json
            try {
                showEstudiantes();
            } catch (Exception ex) {
                Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
            } finally {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }
        }

    }

    public void showEstudiantes() {
        adapter = new EstudiantesAdapter(estudiantesList, this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);

        //refresh view
        adapter.notifyDataSetChanged();
    }

    public class delete extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // display a progress dialog for good user experiance
            progressDialog = new ProgressDialog(EstudiantesActivity.this);
            progressDialog.setMessage("¡Eliminando " + deleteEstudiante.getId() + "!");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            message = "";
            try {
                servicio.delete(deleteEstudiante);
                message = deleteEstudiante.getId() + " eliminado correctamente";
            } catch (Exception ex) {
                message = ex.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            // dismiss the progress dialog after receiving data from API
            progressDialog.dismiss();
            //Json
            try {
                EstudiantesActivity.list task = new EstudiantesActivity.list();
                task.execute();
            } catch (Exception ex) {
                Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
            } finally {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }
        }

    }

    private void insert_estudiante() {
        Intent intent = new Intent(this, EstudianteActivity.class);
        startActivity(intent);
    }
}
