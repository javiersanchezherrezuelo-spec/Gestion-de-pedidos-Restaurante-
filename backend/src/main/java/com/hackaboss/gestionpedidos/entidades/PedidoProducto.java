package com.hackaboss.gestionpedidos.entidades;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.math.BigDecimal;

// Línea de un pedido: qué producto, cuántas unidades y a qué precio.
@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class PedidoProducto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ToString.Exclude
    @ManyToOne //muchos items pertenecen a un pedido
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;

    @ToString.Exclude
    @ManyToOne // muchos items pueden ser del mismo producto
    @JoinColumn(name = "producto_id")
    private Producto producto;

    private Integer cantidad;

    // Precio copiado del producto al añadirlo: si luego cambia el precio de la carta,
    // el pedido mantiene el que tenía.
    private BigDecimal precioUnitario;
}
