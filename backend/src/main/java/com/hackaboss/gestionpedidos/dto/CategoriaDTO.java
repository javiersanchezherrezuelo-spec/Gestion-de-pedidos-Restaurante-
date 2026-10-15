package com.hackaboss.gestionpedidos.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class CategoriaDTO {
	private Long id;
	@NotBlank(message = "El nombre de la categoría es obligatorio")
	private String nombre;
	private List<String> productos;
}
