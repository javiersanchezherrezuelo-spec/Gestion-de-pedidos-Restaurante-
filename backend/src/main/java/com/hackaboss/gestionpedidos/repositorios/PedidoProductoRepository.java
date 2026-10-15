package com.hackaboss.gestionpedidos.repositorios;

import com.hackaboss.gestionpedidos.entidades.PedidoProducto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Repository
public interface PedidoProductoRepository extends JpaRepository<PedidoProducto, Long> {
	
	Optional<PedidoProducto> findByPedidoIdAndProductoId(Long pedidoId, Long productoId);
	
	List<PedidoProducto> findByPedidoId(Long pedidoId);
	
	@Transactional
	void deleteByPedidoIdAndProductoId(Long pedidoId, Long productoId);
}
