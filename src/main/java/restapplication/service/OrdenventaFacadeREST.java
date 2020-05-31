/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package restapplication.service;

import dao.ClienteJpaController;
import entidades.Categoria;
import entidades.Cliente;
import entidades.Facturaventa;
import entidades.Ganancia;
import entidades.Ordencompra;
import entidades.Ordenventa;
import entidades.Producto;
import entidades.Proveedor;
import entidades.Ventadetalle;
import entidades.VentadetallePK;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import restapplication.Common;
import restapplication.api_consumer.APIConsumerProveedor;

/**
 *
 * @author jcami
 */
@Stateless
@Path("pedidos")
public class OrdenventaFacadeREST extends AbstractFacade<Ordenventa> {

    @PersistenceContext(unitName = "com.mycompany_ERProveedores_war_1.0-SNAPSHOTPU")
    private EntityManager em;
    
    private final ClienteJpaController clienteJpaController = 
            new ClienteJpaController(super.getUserTransaction(), super.getEntityManagerFactory());
    
    
    @EJB
    private beans.sessions.ProductoFacade productoFacade;
    
    @EJB
    private beans.sessions.VentadetalleFacade ventaDetalleFacade;
    
    @EJB
    private beans.sessions.GananciaFacade gananciaFacade;
    
   @EJB
   private beans.sessions.ProveedorFacade proveedorFacade;
   
   @EJB
   private beans.sessions.FacturaventaFacade facturaVentaFacade;

    public OrdenventaFacadeREST() {
        super(Ordenventa.class);
    }

    @POST
    @Override
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(Ordenventa entity) {
        // CLIENTE
        Cliente cliente = clienteJpaController.findClienteByEmail(entity.getClienteid().getEmail());
        if(cliente==null){
            return Response.status(Status.BAD_REQUEST).build();
        }
        entity.setClienteid(cliente);
        entity.setSubtotal(0);
        entity.setTotal(0);
        entity.setIva((short)16);
        entity.setFechaVenta(new Date());
        entity.setStatus("pedido pendiente...");
        entity.setVentadetalleCollection(null);
        Ordenventa ordenventaIngresado = null;
        Response response = super.create(entity);
        ordenventaIngresado = (Ordenventa) response.getEntity();
        return response;
    }
    
    @PUT
    @Path("/detalles")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response agregarDetalles(Ordenventa venta){
        try{
            //ORDEN VENTA
            // se hace una copia de la orden venta porque de alguna manera el ORM, está cambiando
            // las órdenes de ventas que están asociadas con los productos
            Ordenventa ordenventaQuery = super.find(venta.getOrdenventaid());
             if(ordenventaQuery==null){
                return Response.status(Status.NOT_FOUND).build();
            }
            Ordenventa ordenventa = new Ordenventa(ordenventaQuery.getOrdenventaid(), ordenventaQuery.getFechaVenta(),
                    ordenventaQuery.getStatus(), ordenventaQuery.getIva(), ordenventaQuery.getSubtotal(), 
                    ordenventaQuery.getTotal(), ordenventaQuery.getDescripcion());
            ordenventa.setClienteid(ordenventaQuery.getClienteid());
                       
            if(venta.getVentadetalleCollection()==null) return Response.status(Status.BAD_REQUEST).build();
            
            ArrayList<Ventadetalle> detalles = new ArrayList<>();
            for(Ventadetalle entity: venta.getVentadetalleCollection()){
                if(entity.getProducto()==null || 
                        entity.getProducto().getProductoid()==null){
                    return Response.status(Status.BAD_REQUEST).build();
                }
                //PRODUCTO
                Producto productoAPI = Common.convertirProductoPojoAProducto(APIConsumerProveedor.obtenerProductoXId(entity.getProducto().getProductoid()));
                if(productoAPI==null){
                    return Response.status(Status.NOT_FOUND).build();
                }
                Producto productoIngresado = ingresarOBuscarProducto(productoAPI);
                Producto p = Common.aplicarGananciaAlProducto(productoIngresado);
                //VENTA DETALLE
                entity.setPrecioUnitario(p.getPrecioUnitario());
                entity.setImporte(p.getPrecioUnitario()*entity.getCantidad());
                // MODIFICAR SUBTOTAL Y TOTAL DE ORDEN DE VENTA
                ordenventa.setSubtotal(ordenventa.getSubtotal()+entity.getImporte());
                long importeIva = (long)16*entity.getImporte()/(long)100;
                importeIva += entity.getImporte();
                ordenventa.setTotal(ordenventa.getTotal()+importeIva);
                // VENTA DETALLE
                entity.setProducto(p);
                entity.setOrdenventa(ordenventa);
                entity.setVentadetallePK(new VentadetallePK(ordenventa.getOrdenventaid(), p.getProductoid()));
                ventaDetalleFacade.create(entity);
                detalles.add(entity);
            }
            ordenventa.setVentadetalleCollection(detalles);
            super.edit(ordenventa);
            return Response.ok(ordenventa).build();
        }catch(Exception ex){
            ex.printStackTrace();
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @POST
    @Path("/solicitar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response realizarPedido(Ordenventa entity){
        try{
            //Consultando que exista la orden, y haciendo una copia de la orden
            Ordenventa ordenventaQuery = super.find(entity.getOrdenventaid());
            if(ordenventaQuery==null){
                return Response.status(Status.BAD_REQUEST).build();
            }
            
            ordenventaQuery.setStatus("Pedido realizado!");
            //Creando factura de venta
            Facturaventa facturaventa = WebServicesUtils.emitirFactura(ordenventaQuery);
            /*Facturaventa facturaCreada = facturaVentaFacade.createEntity(facturaventa);
            ordenventa.setFacturaid(facturaCreada);*/
            
            // solicitar pedidos subproveedores
            Collection<Ventadetalle> detalles = ordenventaQuery.getVentadetalleCollection();
            final ArrayList<Ventadetalle> array = new ArrayList(detalles);
            
            final Runnable task = new Runnable(){
                @Override
                public void run() {
                    try {
                        Ordenventa pedidoGenerado =
                                APIConsumerProveedor.generarPedidoCompleto("Pedido para de proveedor a subproveedor", array);
                    } catch (Exception ex) {
                        Logger.getLogger(OrdenventaFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            };
            task.run();
            
            return Response.ok(facturaventa).build();
        } catch (Exception ex) {
            Logger.getLogger(OrdenventaFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().build();
        }
    }
    
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Ordenventa find(@PathParam("id") Long id) {
        return Common.limpiarOrdenVenta(super.find(id));
    }

    @GET
    @Override
    @Produces(MediaType.APPLICATION_JSON)
    public List<Ordenventa> findAll() {
        List<Ordenventa> ordenes = super.findAll();
        List<Ordenventa> returnList = new ArrayList<>();
        for(Ordenventa orden: ordenes){
            returnList.add(Common.limpiarOrdenVenta(orden));
        }
        return returnList;
    }

    @GET
    @Path("{from}/{to}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Ordenventa> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        List<Ordenventa> ordenes = super.findRange(new int[]{from, to});
        List<Ordenventa> returnList = new ArrayList<>();
        for(Ordenventa orden: ordenes){
            returnList.add(Common.limpiarOrdenVenta(orden));
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
    
    private void guardarOrden(Ordenventa ordenventa){
        Ordencompra ordencompra = Common.convertirOrdenCompraAVenta(ordenventa);
        Proveedor proveedor = proveedorFacade.find((long)1);
    }
    
    private Producto ingresarOBuscarProducto(Producto producto){
        try{
            Producto productoIngresado = productoFacade.find(producto.getProductoid());
            if(productoIngresado==null){
                producto.setGanancia(null);
                producto.setVentadetalleCollection(null);
                producto.setCompradetalleCollection(null);
                Categoria categoria = ingresarOBuscarCategoria(producto.getCategoriaid());
                producto.setCategoriaid(categoria);
                Producto productoP = productoFacade.createEntity(producto);
                Ganancia ganancia = new Ganancia();
                ganancia.setPorcentaje((short)10);
                ganancia.setProductoid(productoP);
                Ganancia gananciaIngresada = gananciaFacade.createEntity(ganancia);
                productoP.setGanancia(gananciaIngresada);
                return productoP;
            }
            return productoIngresado;
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
    }
    
    @EJB
    private beans.sessions.CategoriaFacade categoriaFacade;
    
    private Categoria ingresarOBuscarCategoria(Categoria categoria){
        try{
            if(categoria==null) return null;
            Categoria categoriaIngresada = categoriaFacade.find(categoria.getCategoriaid());
            if(categoriaIngresada==null){
                Categoria c = categoriaFacade.createEntity(categoria);
                return c;
            }
            return categoriaIngresada;
        }catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
    }
    
}