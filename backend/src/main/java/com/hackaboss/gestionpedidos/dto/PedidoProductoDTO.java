package com.hackaboss.gestionpedidos.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PedidoProductoDTO {
	
	private Long id;
	private Long pedidoId;
	@NotNull(message = "El producto es obligatorio")
	private Long productoId;
	private String productoNombre;
	@NotNull(message = "La cantidad es obligatoria")
	@Positive(message = "La cantidad debe ser mayor que cero")
	private Integer cantidad;
	private BigDecimal precioUnitario;
	private BigDecimal subtotal;
	
}
