package com.ragar.projectmanager.room;

import androidx.room.RoomDatabase;

@androidx.room.Database(entities = {Project.class}, version = 1)
public abstract class Database extends RoomDatabase {
    public abstract projectDAO projectDAO();
}
