package com.hackaboss.gestionpedidos.controllers;

import com.hackaboss.gestionpedidos.dto.CategoriaDTO;
import com.hackaboss.gestionpedidos.entidades.ApiRespuesta;
import com.hackaboss.gestionpedidos.services.CategoriaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
public class ControladorCategorias {
	
	private final CategoriaService servicio;
	
	public ControladorCategorias(CategoriaService categoriaService) {
		this.servicio = categoriaService;
	}
	
	
	// Crea nueva categoria
	@PostMapping
	public ResponseEntity<ApiRespuesta<CategoriaDTO>> crearCategoria(
			@Valid @RequestBody CategoriaDTO categoria) {
		return ResponseEntity
				       .status(HttpStatus.CREATED)
				       .body(
						       new ApiRespuesta<>(
								       true,
								       "Categoría creada correctamente",
								       servicio.crearCategoria(categoria.getNombre().trim()))
				       );
	}
	
	// Lista las categorias
	@GetMapping
	public ResponseEntity<ApiRespuesta<List<CategoriaDTO>>> obtenerCategorias() {
		return ResponseEntity
				       .status(HttpStatus.OK)
				       .body(
						       new ApiRespuesta<>(
								       true,
								       "Lista de categorias",
								       servicio.listarCategorias()
						       )
				       );
	}

	@GetMapping("/{id}")
	public ResponseEntity<ApiRespuesta<CategoriaDTO>> obtenerCategoria(@PathVariable Long id) {
		return ResponseEntity.ok(new ApiRespuesta<>(
				true,
				"Categoría encontrada",
				servicio.buscarPorId(id)
		));
	}

	@PutMapping("/{id}")
	public ResponseEntity<ApiRespuesta<CategoriaDTO>> actualizarCategoria(
			@PathVariable Long id,
			@Valid @RequestBody CategoriaDTO categoria) {
		categoria.setNombre(categoria.getNombre().trim());
		return ResponseEntity.ok(new ApiRespuesta<>(
				true,
				"Categoría actualizada correctamente",
				servicio.actualizarCategoria(id, categoria)
		));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminarCategoria(@PathVariable Long id) {
		servicio.eliminarCategoria(id);
		return ResponseEntity.noContent().build();
	}
}
