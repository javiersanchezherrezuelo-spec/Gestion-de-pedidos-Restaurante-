package com.hackaboss.gestionpedidos.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CambiarEstadoPedidoDTO {

	@NotBlank(message = "El estado es obligatorio")
	private String estado;
}
