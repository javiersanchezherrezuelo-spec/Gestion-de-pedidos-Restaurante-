package com.hackaboss.gestionpedidos.entidades;


public enum EstadoPedido {
    CREADO,          // el pedido acaba de crearse
    EN_PREPARACION,  // el pedido esta siendo preparado en cocina
    LISTO,           // el pedido esta listo para recoger
    ENTREGADO,       // el pedido ha sido entregado al cliente
    FINALIZADO       // el pedido se ha completado
}
