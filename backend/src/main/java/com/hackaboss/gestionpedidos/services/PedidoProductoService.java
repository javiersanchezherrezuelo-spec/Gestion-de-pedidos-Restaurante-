package com.hackaboss.gestionpedidos.services;

import com.hackaboss.gestionpedidos.dto.GlobalDTOMapper;
import com.hackaboss.gestionpedidos.dto.PedidoProductoDTO;
import com.hackaboss.gestionpedidos.entidades.Pedido;
import com.hackaboss.gestionpedidos.entidades.PedidoProducto;
import com.hackaboss.gestionpedidos.entidades.Producto;
import com.hackaboss.gestionpedidos.entidades.EstadoPedido;
import com.hackaboss.gestionpedidos.excepciones.BadRequestException;
import com.hackaboss.gestionpedidos.excepciones.NotFoundException;
import com.hackaboss.gestionpedidos.repositorios.PedidoProductoRepository;
import com.hackaboss.gestionpedidos.repositorios.PedidoRepository;
import com.hackaboss.gestionpedidos.repositorios.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

// Gestiona las líneas de un pedido (añadir y quitar productos) y mantiene el total al día.
// Solo permite cambios mientras el pedido está en CREADO.
@Service
public class PedidoProductoService {

    private final PedidoProductoRepository pedidoProductoRepository;
    private final PedidoRepository pedidoRepository;
    private final ProductoRepository productoRepository;

    public PedidoProductoService(
            PedidoProductoRepository pedidoProductoRepository,
            PedidoRepository pedidoRepository,
            ProductoRepository productoRepository) {

        this.pedidoProductoRepository = pedidoProductoRepository;
        this.pedidoRepository = pedidoRepository;
        this.productoRepository = productoRepository;
    }

    // Añade un producto a un pedido mediante su ID y devuelve la línea del pedido
    @Transactional
    public PedidoProductoDTO agregarProducto(Long pedidoId, PedidoProductoDTO dto) {

        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() ->
                        new NotFoundException(
                                "Pedido no encontrado con id: " + pedidoId
                        )
                );

        Producto producto = productoRepository.findById(dto.getProductoId())
                .orElseThrow(() ->
                        new NotFoundException(
                                "Producto no encontrado con id: "
                                        + dto.getProductoId()
                        )
                );

        validarPedidoEditable(pedido);

        if (!producto.getActivo()) {
            throw new BadRequestException(
                    "El producto está desactivado"
            );
        }

        if (dto.getCantidad() == null || dto.getCantidad() <= 0) {
            throw new BadRequestException(
                    "La cantidad debe ser mayor que cero"
            );
        }
        PedidoProducto pedidoProducto =
                pedidoProductoRepository
                        .findByPedidoIdAndProductoId(
                                pedidoId,
                                dto.getProductoId()
                        )
                        .orElse(null);

        if (pedidoProducto != null) {
            pedidoProducto.setCantidad(
                    pedidoProducto.getCantidad()
                            + dto.getCantidad()
            );

        } else {

            pedidoProducto = new PedidoProducto();

            pedidoProducto.setPedido(pedido);
            pedidoProducto.setProducto(producto);
            pedidoProducto.setCantidad(dto.getCantidad());
            pedidoProducto.setPrecioUnitario(
                    producto.getPrecio()
            );
        }

        PedidoProducto guardado =
                pedidoProductoRepository.save(pedidoProducto);

        recalcularTotal(pedido);

        return GlobalDTOMapper.pedidoProdToPedidoProdDTO(guardado);
    }

    @Transactional
    public void eliminarProducto(
            Long pedidoId,
            Long productoId) {

        PedidoProducto pedidoProducto =
                pedidoProductoRepository
                        .findByPedidoIdAndProductoId(
                                pedidoId,
                                productoId
                        )
                        .orElseThrow(() ->
                                new NotFoundException(
                                        "El producto no está en el pedido"
                                )
                        );

        Pedido pedido = pedidoProducto.getPedido();
        validarPedidoEditable(pedido);

        pedidoProductoRepository.delete(pedidoProducto);
        pedidoProductoRepository.flush();
        recalcularTotal(pedido);
    }

    private void validarPedidoEditable(Pedido pedido) {
        if (pedido.getEstado() != EstadoPedido.CREADO) {
            throw new BadRequestException(
                    "Solo se pueden modificar productos de un pedido en estado CREADO"
            );
        }
    }

    // Suma precioUnitario * cantidad de todas las líneas y lo guarda en el pedido.
    private void recalcularTotal(Pedido pedido) {
        BigDecimal total = pedidoProductoRepository.findByPedidoId(pedido.getId())
                .stream()
                .map(item -> item.getPrecioUnitario()
                        .multiply(BigDecimal.valueOf(item.getCantidad())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        pedido.setTotal(total);
        pedidoRepository.save(pedido);
    }

}
