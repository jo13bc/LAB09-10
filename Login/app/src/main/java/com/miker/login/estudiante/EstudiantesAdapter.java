package com.miker.login.estudiante;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.miker.login.R;
import com.miker.login.curso.Curso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


public class EstudiantesAdapter extends RecyclerView.Adapter<EstudiantesAdapter.MyViewHolder> implements Filterable {

    private List<Estudiante> estudianteList;
    private List<Estudiante> estudianteListFiltered;
    private EstudianteAdapterListener listener;
    private Estudiante object;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView id, nombre, apell1, apell2, edad, user, pass;
        public RelativeLayout viewForeground, viewBackgroundDelete, viewBackgroundEdit;

        public MyViewHolder(View view) {
            super(view);
            id = (TextView) view.findViewById(R.id.id);
            nombre = (TextView) view.findViewById(R.id.nombre);
            apell1 = (TextView) view.findViewById(R.id.apell1);
            apell2 = (TextView) view.findViewById(R.id.apell2);
            edad = (TextView) view.findViewById(R.id.edad);
            user = (TextView) view.findViewById(R.id.user);
            pass = (TextView) view.findViewById(R.id.pass);

            viewBackgroundDelete = view.findViewById(R.id.view_background_delete);
            viewBackgroundEdit = view.findViewById(R.id.view_background_edit);
            viewForeground = view.findViewById(R.id.view_foreground);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onSelected(estudianteListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }

    public EstudiantesAdapter(List<Estudiante> estudianteList, EstudianteAdapterListener listener) {
        this.estudianteList = estudianteList;
        this.listener = listener;
        this.estudianteListFiltered = estudianteList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.curso_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Estudiante estudiante = estudianteListFiltered.get(position);
        holder.id.setText(String.valueOf(estudiante.getId()));
        holder.nombre.setText(estudiante.getNombre());
        holder.apell1.setText(estudiante.getApellido1());
        holder.apell2.setText(estudiante.getApellido2());
        holder.edad.setText(String.valueOf(estudiante.getEdad()));
        holder.user.setText(estudiante.getUser());
        holder.pass.setText(estudiante.getPassword());
    }

    @Override
    public int getItemCount() {
        return estudianteListFiltered.size();
    }

    public void removeItem(int position) {
        object = estudianteListFiltered.remove(position);
        Iterator<Estudiante> iter = estudianteList.iterator();
        while (iter.hasNext()) {
            Estudiante aux = iter.next();
            if (object.equals(aux))
                iter.remove();
        }
        // notify item removed
        notifyItemRemoved(position);
    }

    public void restoreItem(int position) {

        if (estudianteListFiltered.size() == estudianteList.size()) {
            estudianteListFiltered.add(position, object);
        } else {
            estudianteListFiltered.add(position, object);
            estudianteList.add(object);
        }
        notifyDataSetChanged();
        // notify item added by position
        notifyItemInserted(position);
    }

    public Estudiante getSwipedItem(int index) {
        if (this.estudianteList.size() == this.estudianteListFiltered.size()) { //not filtered yet
            return estudianteList.get(index);
        } else {
            return estudianteListFiltered.get(index);
        }
    }

    public void onItemMove(int fromPosition, int toPosition) {
        if (estudianteList.size() == estudianteListFiltered.size()) { // without filter
            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(estudianteList, i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(estudianteList, i, i - 1);
                }
            }
        } else {
            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(estudianteListFiltered, i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(estudianteListFiltered, i, i - 1);
                }
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    estudianteListFiltered = estudianteList;
                } else {
                    List<Estudiante> filteredList = new ArrayList<>();
                    for (Estudiante row : estudianteList) {
                        // filter use two parameters
                        if (row.getNombre().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    estudianteListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = estudianteListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                estudianteListFiltered = (ArrayList<Estudiante>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface EstudianteAdapterListener {
        void onSelected(Estudiante estudiante);
    }
}

