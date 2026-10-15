package com.hackaboss.gestionpedidos.repositorios;

import com.hackaboss.gestionpedidos.entidades.EstadoPedido;
import com.hackaboss.gestionpedidos.entidades.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
	
	Optional<Pedido> findByCodigo(String codigo);
	
	List<Pedido> findByEstadoOrderByFechaCreacionAsc(EstadoPedido estado);
	
	List<Pedido> findAllByOrderByFechaCreacionAsc();
}
