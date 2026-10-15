package com.hackaboss.gestionpedidos.entidades;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.util.ArrayList;
import java.util.List;

// Punto de venta (caja o kiosco) desde el que se crean los pedidos.
@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Terminal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre no puede estar vacio")
    private String nombre;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "terminal") // un terminal tiene muchos pedidos
    private List<Pedido> pedidos = new ArrayList<>();
}
