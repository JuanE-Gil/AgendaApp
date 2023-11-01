package com.juanegil.agendaapp.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.juanegil.agendaapp.models.Cita;

import java.util.List;

@Dao
public interface CitaDao {

    @Query("SELECT * FROM citas")
    List<Cita> obtenerCitas();

    @Insert
    void agregarCita(Cita... cita);

    @Update
    void actualizarCita(Cita... cita);

    @Delete
    void elimniarCita(Cita... cita);

}
