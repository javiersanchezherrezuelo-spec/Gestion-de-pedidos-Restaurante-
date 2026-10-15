package com.hackaboss.gestionpedidos.repositorios;

import com.hackaboss.gestionpedidos.entidades.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    Categoria findByNombreIgnoreCase(String nombre);
}
