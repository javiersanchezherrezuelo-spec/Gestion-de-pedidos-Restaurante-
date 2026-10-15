package com.hackaboss.gestionpedidos.controllers;

import com.hackaboss.gestionpedidos.dto.CambiarEstadoPedidoDTO;
import com.hackaboss.gestionpedidos.dto.CrearPedidoDTO;
import com.hackaboss.gestionpedidos.dto.PedidoDTO;
import com.hackaboss.gestionpedidos.dto.PedidoProductoDTO;
import com.hackaboss.gestionpedidos.entidades.ApiRespuesta;
import com.hackaboss.gestionpedidos.entidades.EstadoPedido;
import com.hackaboss.gestionpedidos.excepciones.BadRequestException;
import com.hackaboss.gestionpedidos.services.PedidoProductoService;
import com.hackaboss.gestionpedidos.services.PedidoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api/pedidos")
public class ControladorPedidos {

	private final PedidoService pedidoService;
	private final PedidoProductoService pedidoProductoService;

	public ControladorPedidos(
			PedidoService pedidoService,
			PedidoProductoService pedidoProductoService) {
		this.pedidoService = pedidoService;
		this.pedidoProductoService = pedidoProductoService;
	}

	@GetMapping
	public ResponseEntity<ApiRespuesta<List<PedidoDTO>>> listarPedidos(
			@RequestParam(required = false) String estado) {
		EstadoPedido estadoPedido = estado == null ? null : convertirEstado(estado);
		return ResponseEntity.ok(new ApiRespuesta<>(
				true,
				"Lista de pedidos",
				pedidoService.listarPedidos(estadoPedido)
		));
	}

	@GetMapping("/{id}")
	public ResponseEntity<ApiRespuesta<PedidoDTO>> obtenerPedido(@PathVariable Long id) {
		return ResponseEntity.ok(new ApiRespuesta<>(
				true,
				"Pedido encontrado",
				pedidoService.buscarPorId(id)
		));
	}

	@GetMapping("/codigo/{codigo}")
	public ResponseEntity<ApiRespuesta<PedidoDTO>> obtenerPedidoPorCodigo(
			@PathVariable String codigo) {
		return ResponseEntity.ok(new ApiRespuesta<>(
				true,
				"Pedido encontrado",
				pedidoService.buscarPorCodigo(codigo.trim().toUpperCase(Locale.ROOT))
		));
	}

	@PostMapping
	public ResponseEntity<ApiRespuesta<PedidoDTO>> crearPedido(
			@Valid @RequestBody CrearPedidoDTO peticion) {
		return ResponseEntity.status(HttpStatus.CREATED).body(new ApiRespuesta<>(
				true,
				"Pedido creado correctamente",
				pedidoService.crearPedido(peticion.getTerminalId())
		));
	}

	@PostMapping("/{pedidoId}/productos")
	public ResponseEntity<ApiRespuesta<PedidoProductoDTO>> agregarProducto(
			@PathVariable Long pedidoId,
			@Valid @RequestBody PedidoProductoDTO producto) {
		return ResponseEntity.status(HttpStatus.CREATED).body(new ApiRespuesta<>(
				true,
				"Producto añadido al pedido",
				pedidoProductoService.agregarProducto(pedidoId, producto)
		));
	}

	@DeleteMapping("/{pedidoId}/productos/{productoId}")
	public ResponseEntity<Void> eliminarProducto(
			@PathVariable Long pedidoId,
			@PathVariable Long productoId) {
		pedidoProductoService.eliminarProducto(pedidoId, productoId);
		return ResponseEntity.noContent().build();
	}

	@PatchMapping("/{pedidoId}/estado")
	public ResponseEntity<ApiRespuesta<PedidoDTO>> cambiarEstado(
			@PathVariable Long pedidoId,
			@Valid @RequestBody CambiarEstadoPedidoDTO peticion) {
		return ResponseEntity.ok(new ApiRespuesta<>(
				true,
				"Estado del pedido actualizado",
				pedidoService.cambiarEstado(pedidoId, convertirEstado(peticion.getEstado()))
		));
	}

	private EstadoPedido convertirEstado(String texto) {
		try {
			return EstadoPedido.valueOf(texto.trim().toUpperCase(Locale.ROOT));
		}
		catch (IllegalArgumentException ex) {
			throw new BadRequestException(
					"Estado no válido. Valores permitidos: " + Arrays.toString(EstadoPedido.values())
			);
		}
	}
}
