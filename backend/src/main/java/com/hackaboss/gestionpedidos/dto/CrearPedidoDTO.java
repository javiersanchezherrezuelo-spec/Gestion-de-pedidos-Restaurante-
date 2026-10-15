package com.hackaboss.gestionpedidos.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CrearPedidoDTO {

	@NotNull(message = "La terminal es obligatoria")
	@Positive(message = "El identificador de la terminal debe ser mayor que cero")
	private Long terminalId;
}
