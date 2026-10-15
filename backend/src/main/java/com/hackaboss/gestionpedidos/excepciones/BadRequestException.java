package com.hackaboss.gestionpedidos.excepciones;

public class BadRequestException extends RuntimeException {
	
	public BadRequestException(String message) {
		super(message);
	}
}