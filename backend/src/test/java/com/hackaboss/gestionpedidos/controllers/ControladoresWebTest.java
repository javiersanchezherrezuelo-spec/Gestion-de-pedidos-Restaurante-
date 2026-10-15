package com.hackaboss.gestionpedidos.controllers;

import com.hackaboss.gestionpedidos.dto.PedidoDTO;
import com.hackaboss.gestionpedidos.dto.ProductoDTO;
import com.hackaboss.gestionpedidos.dto.TerminalDTO;
import com.hackaboss.gestionpedidos.entidades.EstadoPedido;
import com.hackaboss.gestionpedidos.services.CategoriaService;
import com.hackaboss.gestionpedidos.services.PedidoProductoService;
import com.hackaboss.gestionpedidos.services.PedidoService;
import com.hackaboss.gestionpedidos.services.ProductoService;
import com.hackaboss.gestionpedidos.services.TerminalService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({
		ControladorCategorias.class,
		ControladorDatosPrueba.class,
		ControladorPedidos.class,
		ControladorProductos.class,
		ControladorTerminales.class
})
class ControladoresWebTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private CategoriaService categoriaService;

	@MockitoBean
	private PedidoService pedidoService;

	@MockitoBean
	private PedidoProductoService pedidoProductoService;

	@MockitoBean
	private ProductoService productoService;

	@MockitoBean
	private TerminalService terminalService;

	@MockitoBean
	private JdbcTemplate jdbcTemplate;

	@Test
	void creaUnaTerminalDesdeJson() throws Exception {
		TerminalDTO terminalCreada = new TerminalDTO();
		terminalCreada.setId(1L);
		terminalCreada.setNombre("Caja principal");
		terminalCreada.setPedidos(List.of());
		when(terminalService.crearTerminal(any(TerminalDTO.class))).thenReturn(terminalCreada);

		mockMvc.perform(post("/api/terminales")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{"nombre":"Caja principal"}
								"""))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.correcta").value(true))
				.andExpect(jsonPath("$.dato.id").value(1))
				.andExpect(jsonPath("$.dato.nombre").value("Caja principal"));
	}

	@Test
	void rechazaUnProductoConDatosInvalidos() throws Exception {
		mockMvc.perform(post("/api/productos")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{"nombre":" ","precio":0,"categoriaId":null}
								"""))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.correcta").value(false))
				.andExpect(jsonPath("$.dato.nombre").exists())
				.andExpect(jsonPath("$.dato.precio").exists())
				.andExpect(jsonPath("$.dato.categoriaId").exists());
	}

	@Test
	void admiteElAliasOrdParaOrdenarProductos() throws Exception {
		when(productoService.listarProductos(null, null, "precio", "DESC"))
				.thenReturn(List.of());

		mockMvc.perform(get("/api/productos")
						.param("ord", "precio")
						.param("tipo_ord", "DESC"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.correcta").value(true));

		verify(productoService).listarProductos(null, null, "precio", "DESC");
	}

	@Test
	void convierteElEstadoSinDistinguirMayusculas() throws Exception {
		PedidoDTO pedidoActualizado = new PedidoDTO();
		pedidoActualizado.setId(7L);
		pedidoActualizado.setEstado("LISTO");
		when(pedidoService.cambiarEstado(eq(7L), eq(EstadoPedido.LISTO)))
				.thenReturn(pedidoActualizado);

		mockMvc.perform(patch("/api/pedidos/7/estado")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{"estado":"listo"}
								"""))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.dato.estado").value("LISTO"));
	}

	@Test
	void reiniciaLosDatosDePrueba() throws Exception {
		mockMvc.perform(delete("/api/pruebas/datos"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.correcta").value(true))
				.andExpect(jsonPath("$.mensaje").value("Datos de prueba borrados correctamente"));
	}
}
