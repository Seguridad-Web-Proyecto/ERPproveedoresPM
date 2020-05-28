/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package restapplication.service;

import entidades.Ganancia;
import restapplication.service.AbstractFacade;
import entidades.Producto;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import restapplication.Common;
import restapplication.api_consumer.APIConsumer;
import restapplication.api_consumer.ClienteHTTP;
import restapplication.pojos.ProductoPOJO;

/**
 *
 * @author jcami
 */
@Stateless
@Path("productos")
public class ProductoFacadeREST extends AbstractFacade<Producto> {

    @PersistenceContext(unitName = "com.mycompany_ERProveedores_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    public ProductoFacadeREST() {
        super(Producto.class);
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Producto find(@PathParam("id") Long id) {
        Producto producto = APIConsumer.obtenerProductoXId(id);
        Ganancia ganancia = new Ganancia();
        ganancia.setPorcentaje((short)10);
        producto.setGanancia(ganancia);
        Producto p = Common.aplicarGananciaAlProducto(producto);
        return p;
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<ProductoPOJO> findEverything() {
        List<ProductoPOJO> productosAPI = APIConsumer.productos("");
        List<ProductoPOJO> returnList = new ArrayList<>();
        for(ProductoPOJO producto: productosAPI){
            Ganancia ganancia = new Ganancia();
            ganancia.setPorcentaje((short)10);
            producto.setGanancia(ganancia);
            ProductoPOJO p = Common.aplicarGananciaAlProductoPOJO(producto);
            returnList.add(p);
        }
        return returnList;
    }

    @GET
    @Path("{from}/{to}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ProductoPOJO> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        List<ProductoPOJO> returnList = APIConsumer.productos("/"+from.toString()+"/"+to.toString());
        return returnList;
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return APIConsumer.productosCOUNT();
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    
}