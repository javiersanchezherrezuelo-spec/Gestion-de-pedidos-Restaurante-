package com.hackaboss.gestionpedidos.services;

import com.hackaboss.gestionpedidos.dto.GlobalDTOMapper;
import com.hackaboss.gestionpedidos.dto.PedidoDTO;
import com.hackaboss.gestionpedidos.entidades.EstadoPedido;
import com.hackaboss.gestionpedidos.entidades.Pedido;
import com.hackaboss.gestionpedidos.entidades.Terminal;
import com.hackaboss.gestionpedidos.excepciones.NotFoundException;
import com.hackaboss.gestionpedidos.repositorios.PedidoRepository;
import com.hackaboss.gestionpedidos.repositorios.TerminalRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.List;

// Gestiona el ciclo de vida del pedido: crearlo desde una terminal, consultarlo
// (por id, por código o por estado) y cambiar su estado.
@Service
public class PedidoService {
	
	private final PedidoRepository pedidoRepository;
	private final TerminalRepository terminalRepository;
	
	public PedidoService(
			PedidoRepository pedidoRepository,
			TerminalRepository terminalRepository) {
		
		this.pedidoRepository = pedidoRepository;
		this.terminalRepository = terminalRepository;
	}
	
	public PedidoDTO crearPedido(Long terminalId) {
		
		Terminal terminal = terminalRepository.findById(terminalId)
				                    .orElseThrow(() ->
						                                 new NotFoundException(
								                                 "Terminal no encontrada con id: " + terminalId
						                                 )
				                    );
		
		Pedido pedido = new Pedido();
		
		pedido.setCodigo(generarCodigo());
		pedido.setFechaCreacion(LocalDateTime.now());
		pedido.setEstado(EstadoPedido.CREADO);
		pedido.setTotal(BigDecimal.ZERO);
		pedido.setTerminal(terminal);
		
		Pedido guardado = pedidoRepository.save(pedido);
		
		return GlobalDTOMapper.pedidoToPedidoDTO(guardado);
	}
	
	public PedidoDTO buscarPorId(Long id) {
		
		Pedido pedido = pedidoRepository.findById(id)
				                .orElseThrow(() ->
						                             new NotFoundException(
								                             "Pedido no encontrado con id: " + id
						                             )
				                );
		
		return GlobalDTOMapper.pedidoToPedidoDTO(pedido);
	}
	
	public PedidoDTO buscarPorCodigo(String codigo) {
		
		Pedido pedido = pedidoRepository.findByCodigo(codigo)
				                .orElseThrow(() ->
						                             new NotFoundException(
								                             "Pedido no encontrado con código: " + codigo
						                             )
				                );
		
		return GlobalDTOMapper.pedidoToPedidoDTO(pedido);
	}
	
	public List<PedidoDTO> listarPedidos(EstadoPedido estado) {
		
		List<Pedido> pedidos;
		
		if (estado != null) {
			pedidos = pedidoRepository
					          .findByEstadoOrderByFechaCreacionAsc(estado);
		}
		else {
			pedidos = pedidoRepository
					          .findAllByOrderByFechaCreacionAsc();
		}
		
		return pedidos.stream()
				       .map(GlobalDTOMapper::pedidoToPedidoDTO)
				       .toList();
	}
	
	// Cambia el estado sin validar el orden: el backend acepta cualquier salto
	// (el frontend solo ofrece el estado siguiente).
	public PedidoDTO cambiarEstado(
			Long pedidoId,
			EstadoPedido nuevoEstado) {
		
		Pedido pedido = pedidoRepository.findById(pedidoId)
				                .orElseThrow(() ->
						                             new NotFoundException(
								                             "Pedido no encontrado con id: " + pedidoId
						                             )
				                );
		
		pedido.setEstado(nuevoEstado);
		
		Pedido actualizado = pedidoRepository.save(pedido);
		
		return GlobalDTOMapper.pedidoToPedidoDTO(actualizado);
	}
	
	// Genera un código aleatorio entre A100 y A999 que no esté ya usado.
	// Ojo: solo hay 900 códigos posibles.
	private String generarCodigo() {
		
		String codigo;
		
		do {
			int numero = 100 + (int) (Math.random() * 900);
			codigo = "A" + numero;
			
		} while (pedidoRepository.findByCodigo(codigo).isPresent());
		
		return codigo;
	}
}
