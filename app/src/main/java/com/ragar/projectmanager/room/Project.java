package com.ragar.projectmanager.room;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Project {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String nombre;
    public int horasRequeridas;
    public boolean esSemanal; // true para semanal, false para diario
    public boolean completado; // true si la tarea está completada, false en caso contrario
}

