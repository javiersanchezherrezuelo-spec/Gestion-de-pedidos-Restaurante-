package com.hackaboss.gestionpedidos.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class PedidoDTO {
	
	private Long id;
	private String codigo;
	private String estado;
	private String fechaCreacionTimestamp;
	private BigDecimal total;
	private Long terminalId;
	private String terminal;
	private List<PedidoProductoDTO> items;
	
}
