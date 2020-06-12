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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Luis Carrillo Rodriguez on 18/4/2018.
 */
public class EstudiantesAdapter extends RecyclerView.Adapter<EstudiantesAdapter.MyViewHolder> implements Filterable {

    private List<Estudiante> carreraList;
    private List<Estudiante> carreraListFiltered;
    private EstudiantesAdapter.EstudianteAdapterListener listener;
    private Estudiante object;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nombre,apell1, apell2, edad, user, pass;
        public RelativeLayout viewForeground, viewBackgroundDelete, viewBackgroundEdit;

        public MyViewHolder(View view) {
            super(view);
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
                    listener.onSelected(carreraListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }

    public EstudiantesAdapter(List<Estudiante> carreraList, EstudiantesAdapter.EstudianteAdapterListener listener) {
        this.carreraList = carreraList;
        this.listener = listener;
        this.carreraListFiltered = carreraList;
    }

    @Override
    public EstudiantesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.carrera_card, parent, false);

        return new EstudiantesAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final EstudiantesAdapter.MyViewHolder holder, final int position) {
        final Estudiante estudiante = carreraListFiltered.get(position);
        holder.nombre.setText(estudiante.getNombre());
        holder.apell1.setText(estudiante.getApell1());
        holder.apell2.setText(estudiante.getApell2());
        holder.edad.setText(estudiante.getEdad());
        holder.user.setText(estudiante.getUser());
        holder.pass.setText(estudiante.getPassword());
    }

    @Override
    public int getItemCount() {
        return carreraListFiltered.size();
    }

    public void removeItem(int position) {
        object = carreraListFiltered.remove(position);
        Iterator<Estudiante> iter = carreraList.iterator();
        while (iter.hasNext()) {
            Estudiante aux = iter.next();
            if (object.equals(aux))
                iter.remove();
        }
        // notify item removed
        notifyItemRemoved(position);
    }

    public void restoreItem(int position) {

        if (carreraListFiltered.size() == carreraList.size()) {
            carreraListFiltered.add(position, object);
        } else {
            carreraListFiltered.add(position, object);
            carreraList.add(object);
        }
        notifyDataSetChanged();
        // notify item added by position
        notifyItemInserted(position);
    }

    public Estudiante getSwipedItem(int index) {
        if (this.carreraList.size() == this.carreraListFiltered.size()) { //not filtered yet
            return carreraList.get(index);
        } else {
            return carreraListFiltered.get(index);
        }
    }

    public void onItemMove(int fromPosition, int toPosition) {
        if (carreraList.size() == carreraListFiltered.size()) { // without filter
            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(carreraList, i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(carreraList, i, i - 1);
                }
            }
        } else {
            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(carreraListFiltered, i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(carreraListFiltered, i, i - 1);
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
                    carreraListFiltered = carreraList;
                } else {
                    List<Estudiante> filteredList = new ArrayList<>();
                    for (Estudiante row : carreraList) {
                        // filter use two parameters
                        if (row.getNombre().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    carreraListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = carreraListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                carreraListFiltered = (ArrayList<Estudiante>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface EstudianteAdapterListener {
        void onSelected(Estudiante estudiante);
    }
}

