/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package restapplication.service;

import entidades.Facturaventa;
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
@Path("facturas")
public class FacturaventaFacadeREST extends AbstractFacade<Facturaventa> {

    @PersistenceContext(unitName = "com.mycompany_ERProveedores_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    public FacturaventaFacadeREST() {
        super(Facturaventa.class);
    }

    @POST
    @Override
    //@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(Facturaventa entity) {
        return super.create(entity);
    }

    @GET
    @Path("{id}")
    //@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public Facturaventa find(@PathParam("id") Long id) {
        return Common.limpiarFacturaVenta(super.find(id));
    }

    @GET
    @Override
    //@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public List<Facturaventa> findAll() {
        List<Facturaventa> facturas = super.findAll();
        List<Facturaventa> returnList = new ArrayList<>();
        for(Facturaventa fv : facturas){
            returnList.add(Common.limpiarFacturaVenta(fv));
        }
        return returnList;
    }

    @GET
    @Path("{from}/{to}")
    //@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public List<Facturaventa> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        List<Facturaventa> facturas = super.findRange(new int[]{from, to});
        List<Facturaventa> returnList = new ArrayList<>();
        for(Facturaventa fv : facturas){
            returnList.add(Common.limpiarFacturaVenta(fv));
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