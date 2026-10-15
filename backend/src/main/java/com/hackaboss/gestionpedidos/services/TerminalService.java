package com.hackaboss.gestionpedidos.services;

import com.hackaboss.gestionpedidos.dto.GlobalDTOMapper;
import com.hackaboss.gestionpedidos.dto.TerminalDTO;
import com.hackaboss.gestionpedidos.entidades.Terminal;
import com.hackaboss.gestionpedidos.excepciones.ConflictException;
import com.hackaboss.gestionpedidos.excepciones.NotFoundException;
import com.hackaboss.gestionpedidos.repositorios.TerminalRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TerminalService {
	
	private final TerminalRepository terminalRepository;
	
	public TerminalService(TerminalRepository terminalRepository) {
		this.terminalRepository = terminalRepository;
	}
	
	public List<TerminalDTO> listarTerminales() {
		
		return terminalRepository.findAll()
				       .stream()
				       .map(GlobalDTOMapper::terminalToTerminalDTO)
				       .toList();
	}
	
	public TerminalDTO buscarPorId(Long id) {
		
		Terminal terminal = terminalRepository.findById(id)
				                    .orElseThrow(() ->
						                                 new NotFoundException(
								                                 "Terminal no encontrada con id: " + id
						                                 )
				                    );
		
		return GlobalDTOMapper.terminalToTerminalDTO(terminal);
	}
	
	public TerminalDTO crearTerminal(TerminalDTO dto) {
		
		if (terminalRepository.existsByNombreIgnoreCase(dto.getNombre())) {
			throw new ConflictException(
					"Ya existe un terminal con el nombre: "
							+ dto.getNombre()
			);
		}
		
		Terminal terminal = new Terminal();
		
		terminal.setNombre(dto.getNombre());
		
		Terminal guardada = terminalRepository.save(terminal);
		
		return GlobalDTOMapper.terminalToTerminalDTO(guardada);
	}
	
	public TerminalDTO actualizarTerminal(Long id, TerminalDTO dto) {
		
		Terminal terminal = terminalRepository.findById(id)
				                    .orElseThrow(() ->
						                                 new NotFoundException(
								                                 "Terminal no encontrada con id: " + id
						                                 )
				                    );
		
		terminal.setNombre(dto.getNombre());
		
		Terminal actualizada = terminalRepository.save(terminal);
		
		return GlobalDTOMapper.terminalToTerminalDTO(actualizada);
	}
	
	public void eliminarTerminal(Long id) {
		
		if (!terminalRepository.existsById(id)) {
			throw new NotFoundException(
					"Terminal no encontrada con id: " + id
			);
		}
		
		terminalRepository.deleteById(id);
	}
	
}
