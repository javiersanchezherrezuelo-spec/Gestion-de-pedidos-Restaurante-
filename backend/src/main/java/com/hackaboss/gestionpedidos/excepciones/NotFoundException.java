package com.hackaboss.gestionpedidos.excepciones;

public class NotFoundException extends RuntimeException {
	
	public NotFoundException(String message) {
		super(message);
	}
}