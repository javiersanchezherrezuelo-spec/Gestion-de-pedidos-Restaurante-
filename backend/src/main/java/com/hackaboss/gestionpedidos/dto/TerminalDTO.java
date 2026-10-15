package com.hackaboss.gestionpedidos.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class TerminalDTO {
	private Long id;
	@NotBlank(message = "El nombre de la terminal es obligatorio")
	private String nombre;
	private List<PedidoDTO> pedidos;
}
