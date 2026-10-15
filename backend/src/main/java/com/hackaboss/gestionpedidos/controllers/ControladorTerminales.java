package com.hackaboss.gestionpedidos.controllers;

import com.hackaboss.gestionpedidos.dto.TerminalDTO;
import com.hackaboss.gestionpedidos.entidades.ApiRespuesta;
import com.hackaboss.gestionpedidos.services.TerminalService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/terminales")
public class ControladorTerminales {

	private final TerminalService servicio;

	public ControladorTerminales(TerminalService servicio) {
		this.servicio = servicio;
	}

	@GetMapping
	public ResponseEntity<ApiRespuesta<List<TerminalDTO>>> listarTerminales() {
		return ResponseEntity.ok(new ApiRespuesta<>(
				true,
				"Lista de terminales",
				servicio.listarTerminales()
		));
	}

	@GetMapping("/{id}")
	public ResponseEntity<ApiRespuesta<TerminalDTO>> obtenerTerminal(@PathVariable Long id) {
		return ResponseEntity.ok(new ApiRespuesta<>(
				true,
				"Terminal encontrada",
				servicio.buscarPorId(id)
		));
	}

	@PostMapping
	public ResponseEntity<ApiRespuesta<TerminalDTO>> crearTerminal(
			@Valid @RequestBody TerminalDTO terminal) {
		terminal.setNombre(terminal.getNombre().trim());
		return ResponseEntity.status(HttpStatus.CREATED).body(new ApiRespuesta<>(
				true,
				"Terminal creada correctamente",
				servicio.crearTerminal(terminal)
		));
	}

	@PutMapping("/{id}")
	public ResponseEntity<ApiRespuesta<TerminalDTO>> actualizarTerminal(
			@PathVariable Long id,
			@Valid @RequestBody TerminalDTO terminal) {
		terminal.setNombre(terminal.getNombre().trim());
		return ResponseEntity.ok(new ApiRespuesta<>(
				true,
				"Terminal actualizada correctamente",
				servicio.actualizarTerminal(id, terminal)
		));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminarTerminal(@PathVariable Long id) {
		servicio.eliminarTerminal(id);
		return ResponseEntity.noContent().build();
	}
}
