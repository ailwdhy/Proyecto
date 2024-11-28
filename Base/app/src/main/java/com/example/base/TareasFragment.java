package com.example.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TareasFragment extends Fragment implements View.OnClickListener {

    private EditText etNombreTarea;
    private EditText etMateria;
    private EditText etDescripcion;
    private DatePicker fechaHoraLimite;
    private Button btnGuardarTarea;
    private RecyclerView recyclerView;
    private Spinner spinnerMateria;
    private TareaAdapter tareaAdapter;
    private List<Tarea> tareas;
    private TareaDao tareaDao;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tareas, container, false);

        etNombreTarea = view.findViewById(R.id.etNombreTarea);
        etMateria = view.findViewById(R.id.etMateria);
        etDescripcion = view.findViewById(R.id.etDescripcion);
        fechaHoraLimite = view.findViewById(R.id.fechaHoraLimite);
        btnGuardarTarea = view.findViewById(R.id.btnGuardarTarea);
        recyclerView = view.findViewById(R.id.recyclerViewTareas);
        spinnerMateria = view.findViewById(R.id.spinnerMateria);

        btnGuardarTarea.setOnClickListener(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        tareaDao = Room.databaseBuilder(getContext(), AppDatabase.class, "tareas-db").allowMainThreadQueries().build().tareaDao();
        tareas = obtenerTareas();

        tareaAdapter = new TareaAdapter(tareas, this);
        recyclerView.setAdapter(tareaAdapter);

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnGuardarTarea) {
            guardarTarea();
        }
    }

    private List<Tarea> obtenerTareas() {
        return tareaDao.getAllTareas();
    }

    private void guardarTarea() {
        String nombre = etNombreTarea.getText().toString();
        String materia = etMateria.getText().toString();
        String descripcion = etDescripcion.getText().toString();
        Calendar calendar = Calendar.getInstance();
        calendar.set(fechaHoraLimite.getYear(), fechaHoraLimite.getMonth(), fechaHoraLimite.getDayOfMonth());
        Date fechaHora = calendar.getTime();

        Tarea tarea = new Tarea();
        tarea.setNombre(nombre);
        tarea.setMateria(materia);
        tarea.setDescripcion(descripcion);
        tarea.setFechaHoraLimite(fechaHora);

        tareaDao.insert(tarea);
        tareas.add(tarea);
        tareaAdapter.notifyItemInserted(tareas.size() - 1);
    }
}