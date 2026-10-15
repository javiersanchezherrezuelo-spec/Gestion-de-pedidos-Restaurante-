package com.hackaboss.gestionpedidos.entidades;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// Pedido hecho desde una terminal. Avanza por los estados de EstadoPedido
// (CREADO -> EN_PREPARACION -> LISTO -> ENTREGADO -> FINALIZADO) y solo admite
// cambios de productos mientras está en CREADO (ver PedidoProductoService).
@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Pedido {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	// Código corto que ve el cliente en la pantalla de recogida (por ejemplo "A123").
	@Column(unique = true)
	private String codigo;
	
	@Enumerated(EnumType.STRING)
	private EstadoPedido estado;
	
	private LocalDateTime fechaCreacion;
	
	// Se recalcula cada vez que se añade o se quita un producto.
	private BigDecimal total;
	
	@ToString.Exclude
	@ManyToOne // muchos pedidos se hacen desde un terminal
	@JoinColumn(name = "terminal_id")
	private Terminal terminal;
	
	@ToString.Exclude
	@OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
	// cascade = ALL: si borramos el pedido, se borran sus items automaticamente
	// orphanRemoval = true: si quitamos un item de la lista, se borra de la BD
	private List<PedidoProducto> items = new ArrayList<>();
	
}
