/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package restapplication.api_consumer;

import com.fasterxml.jackson.core.JsonProcessingException;

import entidades.Cliente;
import entidades.Facturaventa;
import entidades.Ordenventa;
import entidades.Producto;
import entidades.Ventadetalle;
import entidades.VentadetallePK;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Response;

/**
 *
 * @author jcami
 */
public class SolicitarPedidos {

    public static void main(String[] args) throws JsonProcessingException {
        try {
            ArrayList<Ventadetalle> detalles = new ArrayList<>();
            for(long i=40; i<42; i++){
                Producto producto = new Producto();
                producto.setProductoid(i);
                Ventadetalle ventadetalle = new Ventadetalle();
                ventadetalle.setProducto(producto);
                ventadetalle.setCantidad(10);
                detalles.add(ventadetalle);
            }
            /*Ordenventa ordenventa = new Ordenventa();
            ordenventa.setVentadetalleCollection(detalles);
            Cliente cliente = new Cliente();
            cliente.setEmail("supermercado@company.mx");
            ordenventa.setClienteid(cliente);
            ordenventa.setDescripcion("prueba");
            System.out.println(ClassToJson.ordenVentaToJson(ordenventa));*/
            Ordenventa ordenventa = APIConsumerProveedor.generarPedidoCompleto("Realizando prueba de solicitud de productos", detalles);
            System.out.println(ordenventa);
        } catch (Exception ex) {
            //ex.printStackTrace();
            Logger.getLogger(SolicitarPedidos.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public static void pruebaGenerarPedidoCompleto(){
        String description = "Generando un pedido para walmart";
        ArrayList<Ventadetalle> ventadetalleList = new ArrayList<>();
        for(long i=7; i<9; i++){
            Producto producto = new Producto();
            producto.setProductoid(i);
            Ventadetalle ventadetalle = new Ventadetalle();
            ventadetalle.setProducto(producto);
            ventadetalle.setCantidad(100);
            ventadetalleList.add(ventadetalle);
        }
        try {
            APIConsumerProveedor.generarPedidoCompleto(description, ventadetalleList);
        } catch (Exception ex) {
            Logger.getLogger(SolicitarPedidos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static Ordenventa pruebaGenerarPedido() {
        Ordenventa ordenventa = new Ordenventa();
        Cliente cliente = new Cliente();
        cliente.setEmail("compras@walmart.com.mx");
        ordenventa.setClienteid(cliente);
        ordenventa.setDescripcion("Orden de venta realizada a las 7:23pm 19/05/2020");
        //agregarDetallesAlPedido(ordenventa, detalles);
        System.out.println("Realizando pedido...");
        /*Response responseOrdenVenta = APIConsumerProveedor.realizarPedido(ordenventa);
        System.out.println("Respuesta: "+responseOrdenVenta.getStatus());
        if(responseOrdenVenta.getStatus()!=200){
            return null;
        }
        Ordenventa entityResult = responseOrdenVenta.readEntity(Ordenventa.class);
        return entityResult;*/
        return null;
    }
    
    public static Response pruebaAgregarDetallesAlPedido(Ordenventa ordenventa){
        System.out.println("Haciendo petición POST para insertar los detalles de la orden...");
        ArrayList<Ventadetalle> ventadetalleList = new ArrayList<>();
        for(long i=7; i<9; i++){
            Producto producto = new Producto();
            producto.setProductoid(i);
            Ventadetalle ventadetalle = new Ventadetalle(new VentadetallePK(ordenventa.getOrdenventaid(), producto.getProductoid()));
            ventadetalle.setOrdenventa(ordenventa);
            ventadetalle.setProducto(producto);
            ventadetalle.setCantidad(100);
            ventadetalleList.add(ventadetalle);
        }
        ordenventa.setVentadetalleCollection(ventadetalleList);
        /*Response responseDetalles = APIConsumerProveedor.agregarDetallesAlPedido(ordenventa);
        System.out.println("Respuesta: "+responseDetalles.getStatus());*/
        return null;
    }
    
}
