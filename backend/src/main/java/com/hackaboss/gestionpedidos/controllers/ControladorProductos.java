package com.hackaboss.gestionpedidos.controllers;

import com.hackaboss.gestionpedidos.dto.ProductoDTO;
import com.hackaboss.gestionpedidos.entidades.ApiRespuesta;
import com.hackaboss.gestionpedidos.excepciones.BadRequestException;
import com.hackaboss.gestionpedidos.services.ProductoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ControladorProductos {

	private final ProductoService servicio;

	public ControladorProductos(ProductoService servicio) {
		this.servicio = servicio;
	}

	@GetMapping
	public ResponseEntity<ApiRespuesta<List<ProductoDTO>>> listarProductos(
			@RequestParam(required = false) Boolean activos,
			@RequestParam(required = false) Long categoria,
			@RequestParam(required = false) String ordenacion,
			@RequestParam(required = false) String ord,
			@RequestParam(name = "tipo_ord", defaultValue = "ASC") String tipoOrdenacion) {

		String criterio = resolverCriterioOrdenacion(ordenacion, ord);
		validarOrdenacion(criterio, tipoOrdenacion);

		return ResponseEntity.ok(new ApiRespuesta<>(
				true,
				"Lista de productos",
				servicio.listarProductos(activos, categoria, criterio, tipoOrdenacion)
		));
	}

	@GetMapping("/{id}")
	public ResponseEntity<ApiRespuesta<ProductoDTO>> obtenerProducto(@PathVariable Long id) {
		return ResponseEntity.ok(new ApiRespuesta<>(
				true,
				"Producto encontrado",
				servicio.buscarPorId(id)
		));
	}

	@PostMapping
	public ResponseEntity<ApiRespuesta<ProductoDTO>> crearProducto(
			@Valid @RequestBody ProductoDTO producto) {
		producto.setNombre(producto.getNombre().trim());
		return ResponseEntity.status(HttpStatus.CREATED).body(new ApiRespuesta<>(
				true,
				"Producto creado correctamente",
				servicio.crearProducto(producto)
		));
	}

	@PutMapping("/{id}")
	public ResponseEntity<ApiRespuesta<ProductoDTO>> actualizarProducto(
			@PathVariable Long id,
			@Valid @RequestBody ProductoDTO producto) {
		producto.setNombre(producto.getNombre().trim());
		return ResponseEntity.ok(new ApiRespuesta<>(
				true,
				"Producto actualizado correctamente",
				servicio.actualizarProducto(id, producto)
		));
	}

	@PatchMapping("/{id}/desactivar")
	public ResponseEntity<ApiRespuesta<Void>> desactivarProducto(@PathVariable Long id) {
		servicio.desactivarProducto(id);
		return ResponseEntity.ok(new ApiRespuesta<>(
				true,
				"Producto desactivado correctamente",
				null
		));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
		servicio.eliminarProducto(id);
		return ResponseEntity.noContent().build();
	}

	private String resolverCriterioOrdenacion(String ordenacion, String ord) {
		if (ordenacion != null && ord != null && !ordenacion.equalsIgnoreCase(ord)) {
			throw new BadRequestException("Use solo 'ordenacion' u 'ord', no valores distintos en ambos");
		}
		if (ordenacion != null) {
			return ordenacion;
		}
		return ord != null ? ord : "nombre";
	}

	private void validarOrdenacion(String criterio, String tipoOrdenacion) {
		if (criterio != null
				&& !criterio.equalsIgnoreCase("nombre")
				&& !criterio.equalsIgnoreCase("precio")
				&& !criterio.equalsIgnoreCase("categoria")) {
			throw new BadRequestException("La ordenación debe ser: nombre, precio o categoria");
		}
		if (!tipoOrdenacion.equalsIgnoreCase("ASC") && !tipoOrdenacion.equalsIgnoreCase("DESC")) {
			throw new BadRequestException("El tipo de ordenación debe ser ASC o DESC");
		}
	}
}
