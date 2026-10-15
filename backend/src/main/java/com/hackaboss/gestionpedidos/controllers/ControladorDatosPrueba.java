package com.hackaboss.gestionpedidos.controllers;

import com.hackaboss.gestionpedidos.entidades.ApiRespuesta;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/pruebas")
public class ControladorDatosPrueba {

	private static final List<String> TABLAS = List.of(
			"pedido_producto",
			"pedido",
			"producto",
			"categoria",
			"terminal"
	);

	private final JdbcTemplate jdbcTemplate;

	public ControladorDatosPrueba(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@DeleteMapping("/datos")
	public ResponseEntity<ApiRespuesta<List<String>>> borrarDatosDePrueba() {
		List<String> tablasBorradas = new ArrayList<>();

		jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 0");
		try {
			for (String tabla : TABLAS) {
				if (existeTabla(tabla)) {
					jdbcTemplate.execute("TRUNCATE TABLE " + tabla);
					tablasBorradas.add(tabla);
				}
			}
		}
		finally {
			jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 1");
		}

		return ResponseEntity.ok(new ApiRespuesta<>(
				true,
				"Datos de prueba borrados correctamente",
				tablasBorradas
		));
	}

	private boolean existeTabla(String tabla) {
		Integer total = jdbcTemplate.queryForObject(
				"""
				SELECT COUNT(*)
				FROM information_schema.tables
				WHERE table_schema = DATABASE()
				AND table_name = ?
				""",
				Integer.class,
				tabla
		);
		return total != null && total > 0;
	}
}
