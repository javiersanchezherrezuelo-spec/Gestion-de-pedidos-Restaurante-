package com.hackaboss.gestionpedidos.excepciones;

import com.hackaboss.gestionpedidos.entidades.ApiRespuesta;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

// Convierte cualquier excepción en una respuesta ApiRespuesta con correcta = false
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<ApiRespuesta<Object>> manejarPeticionIncorrecta(BadRequestException ex) {
		return respuesta(HttpStatus.BAD_REQUEST, ex.getMessage(), null);
	}

	@ExceptionHandler(ConflictException.class)
	public ResponseEntity<ApiRespuesta<Object>> manejarConflicto(ConflictException ex) {
		return respuesta(HttpStatus.CONFLICT, ex.getMessage(), null);
	}

	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<ApiRespuesta<Object>> manejarNoEncontrado(NotFoundException ex) {
		return respuesta(HttpStatus.NOT_FOUND, ex.getMessage(), null);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiRespuesta<Object>> manejarValidacion(MethodArgumentNotValidException ex) {
		Map<String, String> errores = ex.getBindingResult().getFieldErrors().stream()
				.collect(Collectors.toMap(
						error -> error.getField(),
						error -> error.getDefaultMessage() == null
								? "Valor no válido"
								: error.getDefaultMessage(),
						(mensajeAnterior, mensajeNuevo) -> mensajeAnterior,
						LinkedHashMap::new
				));

		return respuesta(HttpStatus.BAD_REQUEST, "Hay datos inválidos en la petición", errores);
	}

	@ExceptionHandler({
			HttpMessageNotReadableException.class,
			MethodArgumentTypeMismatchException.class,
			ConstraintViolationException.class
	})
	public ResponseEntity<ApiRespuesta<Object>> manejarFormatoIncorrecto(Exception ex) {
		return respuesta(HttpStatus.BAD_REQUEST, "El formato de la petición no es válido", null);
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ApiRespuesta<Object>> manejarIntegridad(DataIntegrityViolationException ex) {
		return respuesta(
				HttpStatus.CONFLICT,
				"La operación entra en conflicto con datos relacionados existentes",
				null
		);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiRespuesta<Object>> manejarErrorGeneral(Exception ex) {
		return respuesta(
				HttpStatus.INTERNAL_SERVER_ERROR,
				"Ha ocurrido un error interno en el servidor",
				null
		);
	}

	private ResponseEntity<ApiRespuesta<Object>> respuesta(
			HttpStatus estado,
			String mensaje,
			Object dato) {
		return ResponseEntity
				.status(estado)
				.body(new ApiRespuesta<>(false, mensaje, dato));
	}
}
