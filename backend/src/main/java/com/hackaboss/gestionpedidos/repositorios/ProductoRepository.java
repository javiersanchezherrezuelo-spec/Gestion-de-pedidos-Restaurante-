package com.hackaboss.gestionpedidos.repositorios;

import com.hackaboss.gestionpedidos.entidades.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
	
	boolean existsByNombreIgnoreCase(String nombre);
	
	List<Producto> findByActivo(boolean activo);
	
	List<Producto> findByCategoriaId(Long categoriaId);
	
	List<Producto> findByActivoAndCategoriaId(boolean activo, Long categoriaId);
	
}
