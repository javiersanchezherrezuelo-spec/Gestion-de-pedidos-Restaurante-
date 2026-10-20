package com.hackaboss.gestionpedidos.services;

import com.hackaboss.gestionpedidos.dto.CategoriaDTO;
import com.hackaboss.gestionpedidos.dto.GlobalDTOMapper;
import com.hackaboss.gestionpedidos.entidades.Categoria;
import com.hackaboss.gestionpedidos.excepciones.ConflictException;
import com.hackaboss.gestionpedidos.excepciones.NotFoundException;
import com.hackaboss.gestionpedidos.repositorios.CategoriaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public List<CategoriaDTO> listarCategorias() {
        return categoriaRepository.findAll()
                .stream()
                .map(GlobalDTOMapper::categoriaToCategoriaDTO)
                .toList();
    }

    public CategoriaDTO buscarPorId(Long id) {

        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException(
                                "Categoría no encontrada con id: " + id
                        )
                );

        return GlobalDTOMapper.categoriaToCategoriaDTO(categoria);
    }

    public CategoriaDTO crearCategoria(String nombre) {

        Categoria encontrada = categoriaRepository.findByNombreIgnoreCase(nombre);
        if (encontrada != null) {
            throw new ConflictException(
                    "Ya existe una categoría con el nombre: "
                            + nombre
            );
        }

        Categoria categoria = new Categoria();
        categoria.setNombre(nombre);
        Categoria guardada = categoriaRepository.save(categoria);

        return GlobalDTOMapper.categoriaToCategoriaDTO(guardada);
    }

    public CategoriaDTO actualizarCategoria(Long id, CategoriaDTO dto) {

        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException(
                                "Categoría no encontrada con id: " + id
                        )
                );

        categoria.setNombre(dto.getNombre());

        Categoria actualizada = categoriaRepository.save(categoria);

        return GlobalDTOMapper.categoriaToCategoriaDTO(actualizada);
    }

    public void eliminarCategoria(Long id) {

        if (!categoriaRepository.existsById(id)) {
            throw new NotFoundException(
                    "Categoría no encontrada con id: " + id
            );
        }

        categoriaRepository.deleteById(id);
    }

}