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

import com.google.android.material.floatingactionbutton.FloatingActionButton;

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

import com.miker.login.Helper.RecyclerItemTouchHelper;
import com.miker.login.Model;
import com.miker.login.NavDrawerActivity;
import com.miker.login.R;
import com.miker.login.ServicioCurso;
import com.miker.login.ServicioEstudiante;
import com.miker.login.curso.Curso;
import com.miker.login.curso.CursoActivity;
import com.miker.login.curso.CursosActivity;
import com.miker.login.curso.CursosAdapter;

import java.util.ArrayList;
import java.util.List;

public class EstudiantesActivity extends AppCompatActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener, EstudiantesAdapter.EstudiantesAdapterListener {

        private RecyclerView recyclerView;
        private EstudiantesAdapter adapter;
        private List<Estudiante> cursoList;
        private Estudiante deleteCurso;
        private CoordinatorLayout coordinatorLayout;
        private SearchView searchView;
        private FloatingActionButton btn_insert;
        private Model model;
        private ProgressDialog progressDialog;
        private ServicioEstudiante servicio;
        private String message;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_cursos);
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            servicio = new ServicioEstudiante(getApplicationContext());

            //toolbar fancy stuff
            getSupportActionBar().setTitle(getString(R.string.item_cursos));

            recyclerView = findViewById(R.id.recycler_view);
            cursoList = new ArrayList<>();
            model = new Model();
            coordinatorLayout = findViewById(R.id.main_content);

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
            com.miker.login.estudiante.EstudiantesActivity.checkIntentInformation checkIntentInformation = new com.miker.login.estudiante.EstudiantesActivity.checkIntentInformation();
            checkIntentInformation.execute();
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
            if (direction == ItemTouchHelper.START) {
                if (viewHolder instanceof CursosAdapter.MyViewHolder) {
                    // get the removed item name to display it in snack bar
                    deleteCurso = cursoList.get(viewHolder.getAdapterPosition());
                    com.miker.login.estudiante.EstudiantesActivity.delete delete = new com.miker.login.estudiante.EstudiantesActivity.delete();
                    delete.execute();
                }
            } else {
                //If is editing a row object
                Estudiante aux = adapter.getSwipedItem(viewHolder.getAdapterPosition());
                //send data to Edit Activity
                Intent intent = new Intent(this, CursoActivity.class);
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
        public void onSelected(Estudiante curso) { //TODO get the select item of recycleView
            Toast.makeText(getApplicationContext(), "Selected: " + curso.getNombre(), Toast.LENGTH_LONG).show();
        }



    public class checkIntentInformation extends AsyncTask<String, String, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                // display a progress dialog for good user experiance
                progressDialog = new ProgressDialog(com.miker.login.estudiante.EstudiantesActivity.this);
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
                                message = aux.getNombre() + " actualizado correctamente";

                            }
                        } else {
                            //found a new Curso Object
                            servicio.insert(aux);
                            message = aux.getNombre() + " agregado correctamente";
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
                    com.miker.login.estudiante.EstudiantesActivity.list task = new com.miker.login.estudiante.EstudiantesActivity.list();
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
                progressDialog = new ProgressDialog(com.miker.login.estudiante.EstudiantesActivity.this);
                progressDialog.setMessage("¡Cargando la Lista!");
                progressDialog.setCancelable(false);
                progressDialog.show();
            }

            @Override
            protected String doInBackground(String... params) {
                message = "";
                String result = "";
                try {
                    Cursor cursor = servicio.list();
                    if (cursor != null) {
                        if (cursor.moveToFirst()) {
                            cursoList = new ArrayList<>();
                            do {
                                Estudiante est = new Estudiante();
                                est.setId(cursor.getInt(cursor.getColumnIndex(ServicioEstudiante.estudianteEntry.ID)));
                                est.setNombre(cursor.getString(cursor.getColumnIndex(ServicioEstudiante.estudianteEntry.NOMBRE)));
                                est.setApellido1(cursor.getString(cursor.getColumnIndex(ServicioEstudiante.estudianteEntry.APELLIDO1)));
                                est.setApellido2(cursor.getString(cursor.getColumnIndex(ServicioEstudiante.estudianteEntry.APELLIDO2)));
                                est.setEdad(cursor.getInt(cursor.getColumnIndex(ServicioEstudiante.estudianteEntry.EDAD)));
                                est.setUser(cursor.getString(cursor.getColumnIndex(ServicioEstudiante.estudianteEntry.USER)));
                                est.setPassword(cursor.getString(cursor.getColumnIndex(ServicioEstudiante.estudianteEntry.PASSWORD)));
                                //
                                cursoList.add(est);
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
            adapter = new EstudiantesAdapter(cursoList, this);

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
                progressDialog = new ProgressDialog(com.miker.login.estudiante.EstudiantesActivity.this);
                progressDialog.setMessage("¡Eliminando " + deleteCurso.getNombre() + "!");
                progressDialog.setCancelable(false);
                progressDialog.show();
            }

            @Override
            protected String doInBackground(String... args) {
                message = "";
                try {
                    servicio.delete(deleteCurso);
                    message = deleteCurso.getNombre()+ " eliminado correctamente";
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
                    com.miker.login.estudiante.EstudiantesActivity.list task = new com.miker.login.estudiante.EstudiantesActivity.list();
                    task.execute();
                } catch (Exception ex) {
                    Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
                } finally {
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                }
            }

        }

        private void insert_estudiante() {
            //  Intent intent = new Intent(this, EstudianteActivity.class);
         //   startActivity(intent);
        }
    }
