/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package restapplication.service;

import entidades.Ordenventa;
import entidades.Producto;
import entidades.Ventadetalle;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import restapplication.api_consumer.APIConsumerProveedor;
import restapplication.pojos.ProductoPOJO;

/**
 *
 * @author jcami
 */
@Stateless
@Path("prueba")
public class PruebaWebService {
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String hello(){
        return "Hello!!!. This is the prueba test service";
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/all")
    public List<ProductoPOJO> getProductos(){
        return APIConsumerProveedor.productos("");
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postProducto(){
        ArrayList<Ventadetalle> detalles = new ArrayList<>();
        for(long i=40; i<42; i++){
            Producto producto = new Producto();
            producto.setProductoid(i);
            Ventadetalle ventadetalle = new Ventadetalle();
            ventadetalle.setProducto(producto);
            ventadetalle.setCantidad(10);
            detalles.add(ventadetalle);
        }
        System.out.println("Enviando petición ...");
        try {
            APIConsumerProveedor.generarPedidoCompleto("Realizando prueba de solicitud de productos", detalles);
            return Response.ok().build();
        } catch (Exception ex) {
            Logger.getLogger(PruebaWebService.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().build();
        }
    }
    
}
