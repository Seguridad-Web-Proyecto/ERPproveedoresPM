/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package restapplication.service;

import restapplication.service.AbstractFacade;
import entidades.Categoria;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import restapplication.api_consumer.ClienteHTTP;

/**
 *
 * @author jcami
 */
@Stateless
@Path("categorias")
public class CategoriaFacadeREST extends AbstractFacade<Categoria> {

    @PersistenceContext(unitName = "com.mycompany_ERProveedores_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    public CategoriaFacadeREST() {
        super(Categoria.class);
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Categoria find(@PathParam("id") Long id) {
        //return super.find(id);
        Categoria categoria = ClienteHTTP.obtenerCategoriaXId(id);
        return categoria;
    }

    @GET
    @Override
    @Produces(MediaType.APPLICATION_JSON)
    public List<Categoria> findAll() {
        //return super.findAll();
        List<Categoria> categorias = ClienteHTTP.categoriasProveedor("");
        return categorias;
    }

    @GET
    @Path("{from}/{to}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Categoria> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        List<Categoria> categorias = ClienteHTTP.categoriasProveedor("/"+from.toString()+"/"+to.toString());
        return categorias;
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return ClienteHTTP.categoriasCOUNT();
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}