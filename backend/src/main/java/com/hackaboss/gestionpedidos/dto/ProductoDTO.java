package com.hackaboss.gestionpedidos.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductoDTO {
	private Long id;
	@NotBlank(message = "El nombre del producto es obligatorio")
	private String nombre;
	@NotNull(message = "El precio es obligatorio")
	@DecimalMin(value = "0.01", message = "El precio debe ser mayor que cero")
	private BigDecimal precio;
	private String urlFoto;
	private Boolean activo = true;
	
	@NotNull(message = "La categoría es obligatoria")
	private Long categoriaId;
	private String categoria;
}
