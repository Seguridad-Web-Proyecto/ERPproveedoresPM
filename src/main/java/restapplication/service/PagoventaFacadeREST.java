/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package restapplication.service;

import entidades.Pagoventa;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import restapplication.Common;

/**
 *
 * @author jcami
 */
@Stateless
@Path("pagos")
public class PagoventaFacadeREST extends AbstractFacade<Pagoventa> {

    @PersistenceContext(unitName = "com.mycompany_ERProveedores_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    public PagoventaFacadeREST() {
        super(Pagoventa.class);
    }

    @POST
    @Override
    //@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(Pagoventa entity) {
        return super.create(entity);
    }

    @GET
    @Path("{id}")
    //@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public Pagoventa find(@PathParam("id") Long id) {
        Pagoventa pagoventa = super.find(id);
        return Common.limpiarPagoVenta(pagoventa);
    }

    @GET
    @Override
    //@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public List<Pagoventa> findAll() {
        List<Pagoventa> pagos = super.findAll();
        List<Pagoventa> returnList = new ArrayList<>();
        for(Pagoventa pv: pagos){
            returnList.add(Common.limpiarPagoVenta(pv));
        }
        return returnList;
    }

    @GET
    @Path("{from}/{to}")
    //@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public List<Pagoventa> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        List<Pagoventa> pagos = super.findRange(new int[]{from, to});
        List<Pagoventa> returnList = new ArrayList<>();
        for(Pagoventa pv: pagos){
            returnList.add(Common.limpiarPagoVenta(pv));
        }
        return returnList;
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}