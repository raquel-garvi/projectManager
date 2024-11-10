package com.ragar.projectmanager;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.room.Room;

import com.ragar.projectmanager.room.Database;
import com.ragar.projectmanager.room.Project;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EditText editTextNombre;
    private EditText editTextHoras;
    private CheckBox checkBoxSemanal;
    private Button buttonAdd;
    private Button buttonCompletar;
    private ProgressBar progressBar;
    private Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        editTextNombre = findViewById(R.id.editTextNombre);
        editTextHoras = findViewById(R.id.editTextHoras);
        checkBoxSemanal = findViewById(R.id.checkBoxSemanal);
        buttonAdd = findViewById(R.id.buttonAdd);
        buttonCompletar = findViewById(R.id.buttonCompletar);
        progressBar = findViewById(R.id.progressBar);

        // Inicializar la base de datos
        db = Room.databaseBuilder(getApplicationContext(),
                Database.class, "database-name").allowMainThreadQueries().build();

        buttonAdd.setOnClickListener(v -> {
            String nombre = editTextNombre.getText().toString();
            int horas = Integer.parseInt(editTextHoras.getText().toString());
            boolean esSemanal = checkBoxSemanal.isChecked();

            Project proyecto = new Project();
            proyecto.nombre = nombre;
            proyecto.horasRequeridas = horas;
            proyecto.esSemanal = esSemanal;
            proyecto.completado = false;
            db.projectDAO().insert(proyecto);

            actualizarProgreso();
        });

        buttonCompletar.setOnClickListener(v -> {
            // Aquí podrías implementar la lógica para marcar un proyecto como completado
            // Por ahora, lo marcaremos como completado el primer proyecto de la lista
            List<Project> projects = db.projectDAO().getAll();
            if (!projects.isEmpty()) {
                Project project = projects.get(0);
                project.completado = true;
                db.projectDAO().update(project);

                actualizarProgreso();
            }
        });

        actualizarProgreso();



    }

    private void actualizarProgreso() {
        List<Project> projects = db.projectDAO().getAll();
        int totalProgreso = 0;
        int totalProyectos = projects.size();

        for (Project project : projects) {
            if (project.completado) {
                totalProgreso += 100;
            } else {
                // Consideramos las horas requeridas y si es semanal o diario
                int factor = project.esSemanal ? 1 : 7;
                totalProgreso += (project.horasRequeridas / factor); // Aquí puedes ajustar el cálculo según la lógica que desees
            }
        }

        int progresoPromedio = (totalProyectos > 0) ? totalProgreso / totalProyectos : 0;
        progressBar.setProgress(progresoPromedio);
    }
}




