package com.hackaboss.gestionpedidos.dto;

import com.hackaboss.gestionpedidos.entidades.*;

import java.math.BigDecimal;
import java.util.List;

public class GlobalDTOMapper {
	
	// Conversion de categoria
	public static CategoriaDTO categoriaToCategoriaDTO(Categoria categoria) {
		CategoriaDTO categoriaDTO = new CategoriaDTO();
		
		categoriaDTO.setId(categoria.getId());
		categoriaDTO.setNombre(categoria.getNombre());
		categoriaDTO.setProductos(categoria
				                          .getProductos()
				                          .stream()
				                          .map(Producto::getNombre)
				                          .toList());
		
		return categoriaDTO;
	}
	
	public static Categoria categoriaDTOToCategoria(CategoriaDTO categoriaDTO) {
		Categoria categoria = new Categoria();
		
		categoria.setId(categoriaDTO.getId());
		categoria.setNombre(categoriaDTO.getNombre());
		
		return categoria;
	}
	
	// Conversion de producto
	public static ProductoDTO productoToProductoDTO(Producto producto) {
		ProductoDTO productoDTO = new ProductoDTO();
		
		productoDTO.setId(producto.getId());
		productoDTO.setNombre(producto.getNombre());
		productoDTO.setActivo(producto.getActivo());
		productoDTO.setUrlFoto(producto.getUrlFoto());
		productoDTO.setPrecio(producto.getPrecio());
		if (producto.getCategoria() != null) {
			productoDTO.setCategoriaId(producto.getCategoria().getId());
			productoDTO.setCategoria(producto.getCategoria().getNombre());
		}
		
		return productoDTO;
	}
	
	public static Producto productoDTOToProducto(ProductoDTO productoDTO) {
		Producto producto = new Producto();
		
		producto.setId(productoDTO.getId());
		producto.setNombre(productoDTO.getNombre());
		producto.setActivo(productoDTO.getActivo());
		producto.setUrlFoto(productoDTO.getUrlFoto());
		producto.setPrecio(productoDTO.getPrecio());
		
		return producto;
	}
	
	// Conversion de terminal
	public static TerminalDTO terminalToTerminalDTO(Terminal terminal) {
		
		TerminalDTO dto = new TerminalDTO();
		
		dto.setId(terminal.getId());
		dto.setNombre(terminal.getNombre());
		dto.setPedidos(terminal.getPedidos()
				               .stream()
				               .map(GlobalDTOMapper::pedidoToPedidoDTO)
				               .toList());
		
		return dto;
	}
	
	// Conversion de pedidos
	public static PedidoDTO pedidoToPedidoDTO(Pedido pedido) {
		
		PedidoDTO dto = new PedidoDTO();
		
		dto.setId(pedido.getId());
		dto.setCodigo(pedido.getCodigo());
		dto.setFechaCreacionTimestamp(pedido.getFechaCreacion().toString());
		dto.setEstado(pedido.getEstado().toString());
		
		Terminal terminal = pedido.getTerminal();
		if (terminal != null) {
			dto.setTerminalId(terminal.getId());
			dto.setTerminal(terminal.getNombre());
		}
		
		List<PedidoProductoDTO> productos =
				pedido.getItems()
						.stream()
						.map(GlobalDTOMapper::pedidoProdToPedidoProdDTO)
						.toList();
		
		dto.setItems(productos);
		
		BigDecimal total = productos.stream()
				                   .map(PedidoProductoDTO::getSubtotal)
				                   .reduce(
						                   BigDecimal.ZERO,
						                   BigDecimal::add
				                   );
		
		dto.setTotal(total);
		
		return dto;
	}
	
	public static PedidoProductoDTO pedidoProdToPedidoProdDTO(PedidoProducto pedidoProducto) {
		
		PedidoProductoDTO dto = new PedidoProductoDTO();
		
		dto.setProductoId(pedidoProducto.getProducto().getId());
		dto.setId(pedidoProducto.getId());
		if (pedidoProducto.getPedido() != null) {
			dto.setPedidoId(pedidoProducto.getPedido().getId());
		}
		
		dto.setProductoNombre(pedidoProducto.getProducto().getNombre());
		
		dto.setCantidad(pedidoProducto.getCantidad());
		
		dto.setPrecioUnitario(pedidoProducto.getPrecioUnitario());
		
		BigDecimal subtotal =
				pedidoProducto.getPrecioUnitario()
						.multiply(
								BigDecimal.valueOf(
										pedidoProducto.getCantidad()
								)
						);
		
		dto.setSubtotal(subtotal);
		
		return dto;
	}
	
}
