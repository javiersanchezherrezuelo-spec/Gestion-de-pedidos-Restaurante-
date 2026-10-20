package com.hackaboss.gestionpedidos.entidades;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;


@Getter
@Setter
public class ApiRespuesta<T> {

    private boolean correcta;

    private String mensaje;

    private T dato;

    private LocalDateTime timestamp;

    public ApiRespuesta(boolean correcta, String mensaje, T dato) {
        this.correcta = correcta;
        this.mensaje = mensaje;
        this.dato = dato;
        this.timestamp = LocalDateTime.now();
    }
}
