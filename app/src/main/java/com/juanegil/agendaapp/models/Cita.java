package com.juanegil.agendaapp.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "citas")
public class Cita {

    @NotNull
    @PrimaryKey
    @ColumnInfo(name = "id_cita")
    public String idCita;

    @ColumnInfo(name = "nombre_cliente")
    public String nombreCliente;

    @ColumnInfo(name = "telefono_cliente")
    public String telefonoCliente;

    @ColumnInfo(name = "asunto_cliente")
    public String asuntoCliente;

    @ColumnInfo(name = "hora_cita")
    public String horaCita;

    @ColumnInfo(name = "dia_cita")
    public String diaCita;
}
