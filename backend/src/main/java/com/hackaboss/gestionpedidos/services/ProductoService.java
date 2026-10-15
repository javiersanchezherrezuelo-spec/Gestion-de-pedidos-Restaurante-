package com.hackaboss.gestionpedidos.services;

import com.hackaboss.gestionpedidos.dto.GlobalDTOMapper;
import com.hackaboss.gestionpedidos.dto.ProductoDTO;
import com.hackaboss.gestionpedidos.entidades.Categoria;
import com.hackaboss.gestionpedidos.entidades.Producto;
import com.hackaboss.gestionpedidos.excepciones.ConflictException;
import com.hackaboss.gestionpedidos.excepciones.NotFoundException;
import com.hackaboss.gestionpedidos.repositorios.CategoriaRepository;
import com.hackaboss.gestionpedidos.repositorios.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService {
	
	private final ProductoRepository productoRepository;
	private final CategoriaRepository categoriaRepository;
	
	public ProductoService(
			ProductoRepository productoRepository,
			CategoriaRepository categoriaRepository) {
		
		this.productoRepository = productoRepository;
		this.categoriaRepository = categoriaRepository;
	}
	
	public List<ProductoDTO> listarProductos(
			Boolean activos,
			Long categoria,
			String ordenacion,
			String tipoOrd) {
		
		List<Producto> productos;
		
		if (activos != null && categoria != null) {
			
			productos = productoRepository
					            .findByActivoAndCategoriaId(activos, categoria);
			
		}
		else if (activos != null) {
			
			productos = productoRepository
					            .findByActivo(activos);
			
		}
		else if (categoria != null) {
			
			productos = productoRepository
					            .findByCategoriaId(categoria);
			
		}
		else {
			
			productos = productoRepository.findAll();
		}
		
		if (ordenacion != null) {
			
			boolean descendente =
					"DESC".equalsIgnoreCase(tipoOrd);
			
			productos.sort((p1, p2) -> {
				
				int resultado;
				
				switch (ordenacion.toLowerCase()) {
					
					case "precio":
						resultado = p1.getPrecio()
								            .compareTo(p2.getPrecio());
						break;
					
					case "categoria":
						resultado = p1.getCategoria()
								            .getNombre()
								            .compareToIgnoreCase(
										            p2.getCategoria().getNombre()
								            );
						break;
					
					case "nombre":
					default:
						resultado = p1.getNombre()
								            .compareToIgnoreCase(
										            p2.getNombre()
								            );
						break;
				}
				
				return descendente ? -resultado : resultado;
			});
			
		}
		else {
			
			productos.sort(
					(p1, p2) -> p1.getNombre()
							            .compareToIgnoreCase(p2.getNombre())
			);
		}
		
		return productos.stream()
				       .map(GlobalDTOMapper::productoToProductoDTO)
				       .toList();
	}
	
	public ProductoDTO buscarPorId(Long id) {
		
		Producto producto = productoRepository.findById(id)
				                    .orElseThrow(() ->
						                                 new NotFoundException(
								                                 "Producto no encontrado con id: " + id
						                                 )
				                    );
		
		return GlobalDTOMapper.productoToProductoDTO(producto);
	}
	
	public ProductoDTO crearProducto(ProductoDTO dto) {
		
		if (productoRepository.existsByNombreIgnoreCase(dto.getNombre())) {
			throw new ConflictException(
					"Ya existe un producto con el nombre: "
							+ dto.getNombre()
			);
		}
		
		Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
				                      .orElseThrow(() ->
						                                   new NotFoundException(
								                                   "Categoría no encontrada con id: "
										                                   + dto.getCategoriaId()
						                                   )
				                      );
		
		Producto producto = new Producto();
		
		producto.setNombre(dto.getNombre());
		producto.setPrecio(dto.getPrecio());
		producto.setActivo(true);
		producto.setUrlFoto(dto.getUrlFoto());
		producto.setCategoria(categoria);
		
		Producto guardado = productoRepository.save(producto);
		
		return GlobalDTOMapper.productoToProductoDTO(guardado);
	}
	
	public ProductoDTO actualizarProducto(Long id, ProductoDTO dto) {
		
		Producto producto = productoRepository.findById(id)
				                    .orElseThrow(() ->
						                                 new NotFoundException(
								                                 "Producto no encontrado con id: " + id
						                                 )
				                    );
		
		Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
				                      .orElseThrow(() ->
						                                   new NotFoundException(
								                                   "Categoría no encontrada con id: "
										                                   + dto.getCategoriaId()
						                                   )
				                      );
		
		producto.setNombre(dto.getNombre());
		producto.setPrecio(dto.getPrecio());
		producto.setUrlFoto(dto.getUrlFoto());
		producto.setCategoria(categoria);
		
		if (dto.getActivo() != null) {
			producto.setActivo(dto.getActivo());
		}
		
		Producto actualizado = productoRepository.save(producto);
		
		return GlobalDTOMapper.productoToProductoDTO(actualizado);
	}
	
	public void desactivarProducto(Long id) {
		
		Producto producto = productoRepository.findById(id)
				                    .orElseThrow(() ->
						                                 new NotFoundException(
								                                 "Producto no encontrado con id: " + id
						                                 )
				                    );
		
		producto.setActivo(false);
		
		productoRepository.save(producto);
	}
	
	public void eliminarProducto(Long id) {
		
		if (!productoRepository.existsById(id)) {
			throw new NotFoundException(
					"Producto no encontrado con id: " + id
			);
		}
		
		productoRepository.deleteById(id);
	}
	
}
