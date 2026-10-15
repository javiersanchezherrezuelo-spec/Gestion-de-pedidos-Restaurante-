package com.hackaboss.gestionpedidos.repositorios;

import com.hackaboss.gestionpedidos.entidades.Terminal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TerminalRepository extends JpaRepository<Terminal, Long> {
	
	boolean existsByNombreIgnoreCase(String nombre);
	
	Terminal findByNombreIgnoreCase(String nombre);
}
