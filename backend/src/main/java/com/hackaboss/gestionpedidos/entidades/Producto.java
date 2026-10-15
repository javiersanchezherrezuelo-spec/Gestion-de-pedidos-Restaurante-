package com.hackaboss.gestionpedidos.entidades;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.math.BigDecimal;

// Producto de la carta. "activo = false" lo oculta de la carta sin borrarlo,
// para no perder los pedidos antiguos que lo contienen.
@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private BigDecimal precio;

    private String urlFoto;

    private Boolean activo = true;

    @ToString.Exclude
    @ManyToOne // muchos productos pertenecen a una categoria
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;
}
