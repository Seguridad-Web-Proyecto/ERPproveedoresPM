/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package restapplication;

import entidades.Compradetalle;
import entidades.Facturacompra;
import entidades.Facturaventa;
import entidades.Ordencompra;
import entidades.Ordenventa;
import entidades.Pagoventa;
import entidades.Producto;
import entidades.Proveedor;
import entidades.Tarjetacreditoventa;
import entidades.Ventadetalle;
import entidades.VentadetallePK;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import restapplication.pojos.ProductoPOJO;

/**
 *
 * @author jcami
 */
public class Common {
    
    public static ProductoPOJO aplicarGananciaAlProductoPOJO(ProductoPOJO producto){
        int gananciaxproducto = ((int)producto.getGanancia().getPorcentaje())*(int)producto.getPrecioUnitario();
        gananciaxproducto /= 100;
        int nuevoPrecio = (int)producto.getPrecioUnitario() + gananciaxproducto;
        ProductoPOJO productoQ = new ProductoPOJO(producto.getProductoid(), producto.getNombre(), 
                producto.getDescripcion(), producto.getUnidadMedida(), nuevoPrecio,producto.getExist(), producto.getCantidad());
        productoQ.setCategoriaid(producto.getCategoriaid());
        if(producto.getInventarioCollection()!=null){
            productoQ.setInventarioCollection(producto.getInventarioCollection());
        }
        return productoQ;
    }
    
    public static Producto aplicarGananciaAlProducto(Producto producto){
        int gananciaxproducto = ((int)producto.getGanancia().getPorcentaje())*(int)producto.getPrecioUnitario();
        gananciaxproducto /= 100;
        int nuevoPrecio = (int)producto.getPrecioUnitario() + gananciaxproducto;
        Producto productoQ = new Producto(producto.getProductoid(), producto.getNombre(), 
                producto.getDescripcion(), producto.getUnidadMedida(), nuevoPrecio);
        productoQ.setCategoriaid(producto.getCategoriaid());
        return productoQ;
    }
    
    public static Tarjetacreditoventa limpiarTarjetaCredito(Tarjetacreditoventa tarjeta){
        Tarjetacreditoventa nuevaTarjeta = new Tarjetacreditoventa();
        nuevaTarjeta.setLugarFacturacion(tarjeta.getLugarFacturacion());
        String numeroStr = String.valueOf(tarjeta.getNumero());
        numeroStr = numeroStr.substring(numeroStr.length()-3);
        nuevaTarjeta.setNumero(new BigInteger(numeroStr));
        return nuevaTarjeta;
    }
    
    public static Pagoventa limpiarPagoVenta(Pagoventa pagoventa){
        Pagoventa pago = new Pagoventa(pagoventa.getPagoventaid(), pagoventa.getFechaPago(), pagoventa.getMonto(), pagoventa.getEstado());
        pago.setTarjetacreditoid(Common.limpiarTarjetaCredito(pagoventa.getTarjetacreditoid()));
        return pago;
    }
    
    public static Ordenventa limpiarOrdenVenta(Ordenventa ordenventa){
        Ordenventa returnOrden = new Ordenventa(ordenventa.getOrdenventaid(), 
                ordenventa.getFechaVenta(), ordenventa.getStatus(), ordenventa.getIva(),
                ordenventa.getSubtotal(), ordenventa.getTotal(), ordenventa.getDescripcion());
        List<Ventadetalle> ventadetalleList = new ArrayList<>();
        for(Ventadetalle vd: ordenventa.getVentadetalleCollection()){
            Producto p = Common.aplicarGananciaAlProducto(vd.getProducto());
            VentadetallePK detallePK = new VentadetallePK(vd.getVentadetallePK().getVentaid(),
                    vd.getVentadetallePK().getProductoid());
            Ventadetalle detalle =  new Ventadetalle(detallePK, vd.getCantidad(), p.getPrecioUnitario(), vd.getCantidad()*p.getPrecioUnitario());
            detalle.setProducto(p);
            ventadetalleList.add(detalle);
        }
        returnOrden.setClienteid(ordenventa.getClienteid());
        if(ordenventa.getFacturaid()!=null){
            returnOrden.setFacturaid(Common.limpiarFacturaVenta(ordenventa.getFacturaid()));
        }
        returnOrden.setVentadetalleCollection((ArrayList<Ventadetalle>) ventadetalleList);
        return returnOrden;
    }
    
    public static Facturaventa limpiarFacturaVenta(Facturaventa factura){
        Facturaventa returnFactura = new Facturaventa(factura.getFacturaventaid(), factura.getFechaEmision(), 
                factura.getFechaVencimientoPago(), factura.getDescripcion());
        if(factura.getPagoid()!=null){
            returnFactura.setPagoid(Common.limpiarPagoVenta(factura.getPagoid()));
        }
        return returnFactura;
    }
    
    public static Producto convertirProductoPojoAProducto(ProductoPOJO productoPOJO){
        Producto producto = new Producto();
        producto.setProductoid(productoPOJO.getProductoid());
        producto.setNombre(productoPOJO.getNombre());
        producto.setDescripcion(productoPOJO.getDescripcion());
        producto.setPrecioUnitario(productoPOJO.getPrecioUnitario());
        producto.setUnidadMedida(productoPOJO.getUnidadMedida());
        producto.setCategoriaid(productoPOJO.getCategoriaid());
        if(productoPOJO.getCompradetalleCollection()!=null){
            producto.setCompradetalleCollection(productoPOJO.getCompradetalleCollection());
        }
        if(productoPOJO.getVentadetalleCollection()!=null){
            producto.setVentadetalleCollection(productoPOJO.getVentadetalleCollection());
        }
        if(productoPOJO.getGanancia()!=null){
            producto.setGanancia(productoPOJO.getGanancia());
        }
        return producto;
    }
    
    public static Ordencompra convertirOrdenCompraAVenta(Ordenventa ordenventa){
        Proveedor proveedor = new Proveedor((long)1); // busca el proveedor 1 -> subproveedor@company.mx
       
        Ordencompra ordencompra = new Ordencompra();
        ordencompra.setDescripcion("Realizando una compra al proveedor");
        ordencompra.setStatus("Orden de compra realizada");
        ordencompra.setFechaCompra(new Date());
        ordencompra.setIva(ordenventa.getIva());
        ordencompra.setSubtotal(ordenventa.getSubtotal());
        ordencompra.setTotal(ordenventa.getTotal());
        
        ArrayList<Compradetalle> compraDetalles = new ArrayList<>();
        for(Ventadetalle ventadetalle: ordenventa.getVentadetalleCollection()){
            Compradetalle compradetalle = new Compradetalle();
            compradetalle.setCantidad(ventadetalle.getCantidad());
            compradetalle.setProducto(ventadetalle.getProducto());
            compraDetalles.add(compradetalle);
        }
        
        ordencompra.setCompradetalleCollection(compraDetalles);
        Facturacompra facturacompra = new Facturacompra();
        facturacompra.setDescripcion("Factura de la orden de compra emitidas al proveedor "+proveedor.getEmpresa());
        facturacompra.setFechaEmision(ordenventa.getFacturaid().getFechaEmision());
        facturacompra.setFechaVencimientoPago(ordenventa.getFacturaid().getFechaVencimientoPago());
        ordencompra.setFacturaid(facturacompra);
        return ordencompra;
    }
    
}
