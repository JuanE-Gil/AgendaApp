package com.juanegil.agendaapp.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.juanegil.agendaapp.dao.CitaDao;
import com.juanegil.agendaapp.models.Cita;

@Database(
        entities = {Cita.class},
        version = 1
)
public abstract class AppDatabase extends RoomDatabase {
    public abstract CitaDao citaDao();
}
