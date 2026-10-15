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

// Agrupa los productos de la carta (por ejemplo "Comida" o "Bebida").
// No usamos @Data: en entidades con relaciones en los dos sentidos, su toString/equals/hashCode
// se llaman entre sí (Categoria -> Producto -> Categoria...) y acaban en StackOverflowError.
@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre no puede estar vacio")
    private String nombre; // nombre de la categoria

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "categoria") //una categoria tiene muchos productos
    private List<Producto> productos = new ArrayList<>();
}
