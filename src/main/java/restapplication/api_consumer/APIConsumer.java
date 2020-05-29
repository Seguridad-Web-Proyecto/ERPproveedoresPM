/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package restapplication.api_consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import entidades.Categoria;
import entidades.Cliente;
import entidades.Facturaventa;
import entidades.Ordenventa;
import entidades.Ventadetalle;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import javax.ws.rs.client.ClientBuilder;

import static restapplication.api_consumer.ClienteHTTP.peticionHttpGet;
import restapplication.pojos.ProductoPOJO;

/**
 *
 * @author jcami
 */
@Stateless
public class APIConsumer {
    
    private static final String pathProductos = "http://localhost:8080/ERPsubproveedoresPM/webresources/productos";
    private static final String pathCategorias = "http://localhost:8080/ERPsubproveedoresPM/webresources/categorias";
    
    private static final String USER_AGENT = "Mozilla/5.0";
     private static final String URL_BASE = "http://localhost:8080/ERPsubproveedoresPM/webresources";
    private static WebTarget webTarget;
    private static Client clientHttp;
    private static Invocation.Builder invocationBuilder;
    
    public static List<ProductoPOJO> productos(String path){
        System.out.println("Solicitando productos. Proveedor -> Subproveedor");
        List<ProductoPOJO> productoList = new ArrayList<>();
        String url = pathProductos+path;
        String respuesta = "";
        try {
            respuesta = peticionHttpGet(url);
            System.out.println("La respuesta es:\n" + respuesta);
            String jsonString = new String(respuesta.getBytes("ISO-8859-1"), "UTF-8");
            ObjectMapper mapper = new ObjectMapper();
            productoList = mapper.readValue(jsonString, new TypeReference<List<ProductoPOJO>>(){});
        } catch (Exception e) {
            // Manejar excepción
            e.printStackTrace();
        }
        return productoList;
    }
    
    public static List<ProductoPOJO> getProductos() throws JsonProcessingException{
        System.out.println("Solicitando productos. Proveedor -> Subproveedor");
        clientHttp = ClientBuilder.newClient();
        webTarget = clientHttp.target(URL_BASE).path("/productos");
        invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.get();
        System.out.println("Respuesta: "+response.getStatus());
        List<ProductoPOJO> productosPOJO = new ObjectMapper().
                readValue(response.readEntity(String.class), new TypeReference<List<ProductoPOJO>>(){});
        return productosPOJO;
    }
    
    public static ProductoPOJO obtenerProductoXId(Long productoid){
        String url = pathProductos+"/"+productoid.toString();
        String respuesta = "";
        ProductoPOJO productoPOJO= new ProductoPOJO();
        try {
            respuesta = peticionHttpGet(url);
            System.out.println("La respuesta es:\n" + respuesta);
            String jsonString = new String(respuesta.getBytes("ISO-8859-1"), "UTF-8");
            ObjectMapper mapper = new ObjectMapper();
            productoPOJO = mapper.readValue(jsonString, new TypeReference<ProductoPOJO>(){});
//            System.out.println("-------------------");
//            System.out.println("productoid: "+productoPOJO.getProductoid());
//            System.out.println("nombre: "+productoPOJO.getNombre());
//            System.out.println("descripcion: "+productoPOJO.getDescripcion());
//            System.out.println("unidad de medida: "+productoPOJO.getUnidadMedida());
//            System.out.println("categoría[ ");
//            System.out.println("categoriaid: "+productoPOJO.getCategoriaid());
//            System.out.println("categoría nombre: "+productoPOJO.getCategoriaid().getNombre());
//            System.out.println("]\n-------------------");
        } catch (Exception e) {
            // Manejar excepción
            e.printStackTrace();
        }
        return productoPOJO;
    }
    
    public static List<Categoria> categorias(String path){
        List<Categoria> categoriaList = new ArrayList<>();
        String url = pathCategorias+path;
        String respuesta = "";
        try {
            respuesta = peticionHttpGet(url);
            System.out.println("La respuesta es:\n" + respuesta);
            String jsonString = new String(respuesta.getBytes("ISO-8859-1"), "UTF-8");
            ObjectMapper mapper = new ObjectMapper();
            categoriaList = mapper.readValue(jsonString, new TypeReference<List<Categoria>>(){});
            for(Categoria categoria: categoriaList){
                System.out.println("-------------------");
                System.out.println("categoría[ ");
                System.out.println("categoriaid: "+categoria.getCategoriaid());
                System.out.println("categoría nombre: "+categoria.getNombre());
                System.out.println("]\n-------------------");
            }
        } catch (Exception e) {
            // Manejar excepción
            e.printStackTrace();
        }
        return categoriaList;
    }
    
    public static Categoria obtenerCategoriaXId(Long categoriaid){
        String url = pathCategorias+"/"+categoriaid.toString();
        String respuesta = "";
        Categoria categoria = new Categoria();
        try {
            respuesta = peticionHttpGet(url);
            String jsonString = new String(respuesta.getBytes("ISO-8859-1"), "UTF-8");
            ObjectMapper mapper = new ObjectMapper();
            categoria = mapper.readValue(jsonString, new TypeReference<Categoria>(){});
            System.out.println("-------------------");
            System.out.println("categoria[ ");
            System.out.println("categoriaid: "+categoria.getCategoriaid());
            System.out.println("categoría nombre: "+categoria.getNombre());
            System.out.println("]\n-------------------");
        } catch (Exception e) {
            // Manejar excepción
            e.printStackTrace();
        }
        return categoria;
    }
    
    public static String productosCOUNT(){
        String url = pathProductos+"/count";
        String respuesta="";
        try{
            respuesta = peticionHttpGet(url);
        }catch (Exception e) {
            // Manejar excepción
            e.printStackTrace();
        }
        return respuesta;
    }
    
    public static String categoriasCOUNT(){
        String url = pathCategorias+"/count";
        String respuesta="";
        try{
            respuesta = peticionHttpGet(url);
        }catch (Exception e) {
            // Manejar excepción
            e.printStackTrace();
        }
        return respuesta;
    }
    
    public static Response realizarPedido(Ordenventa ordenventa){
        System.out.println("Proveedores -> Realizando pedido a subproveedores...");
        clientHttp = ClientBuilder.newClient();
        webTarget = clientHttp.target(URL_BASE).path("/pedidos");
        invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.post(
                Entity.entity(ordenventa, MediaType.APPLICATION_JSON));
        System.out.println("Respuesta: "+response.getStatus());
        return response;
    }
    
    public static Response agregarDetallesAlPedido(Ordenventa ordenventa){
        System.out.println("Proveedores -> Agregando detalles al pedido");
        if(ordenventa.getVentadetalleCollection()==null) return null;
        clientHttp = ClientBuilder.newClient();
        webTarget = clientHttp.target(URL_BASE).path("/pedidos/detalles");
        invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.put(Entity.entity(ordenventa, 
                MediaType.APPLICATION_JSON));
        System.out.println("Respuesta: "+response.getStatus());
        return response;
    }
    
    public static Response concluirPedido(Ordenventa ordenventa){
        System.out.println("Solicitando el pedido...");
        clientHttp = ClientBuilder.newClient();
        webTarget = clientHttp.target(URL_BASE).path("/pedidos/solicitar");
        invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.post(Entity.entity(ordenventa, MediaType.APPLICATION_JSON));
        System.out.println("Respuesta: "+response.getStatus());
        return response;
    }
    
    public static Ordenventa generarPedidoCompleto(String descripcion, ArrayList<Ventadetalle> ventaDetalleList) throws Exception{
        Ordenventa ordenventa = new Ordenventa();
        Cliente cliente = new Cliente();
        cliente.setEmail("proveedor@company.mx");
        ordenventa.setClienteid(cliente);
        ordenventa.setDescripcion(descripcion);
        ordenventa.setVentadetalleCollection(ventaDetalleList);
        Response responseOrdenVenta = APIConsumer.realizarPedido(ordenventa);
        if(responseOrdenVenta.getStatus()!=200){
            String msg = responseOrdenVenta.readEntity(String.class);
            throw new Exception("Whoops!!. Error al realizar un pedido!\n"+msg);
        }
        // DETALLES
        Ordenventa ordenVentaResult = responseOrdenVenta.readEntity(Ordenventa.class);
        ordenVentaResult.setVentadetalleCollection(ventaDetalleList);
        Response responseDetalles = APIConsumer.agregarDetallesAlPedido(ordenVentaResult);
        if(responseDetalles.getStatus()!=200){
            String msg = responseDetalles.readEntity(String.class);
            throw new Exception("Whoops!!. Error al añadir los detalles al pedido!\n"+msg); 
        }
        // CONLUYENDO PEDIDO Y RECIBIENDO LA FACTURA
        Response responseCompletarPedido = APIConsumer.concluirPedido(ordenVentaResult);
        Facturaventa facturaVenta = responseCompletarPedido.readEntity(Facturaventa.class);
        if(responseCompletarPedido.getStatus()!=200){
            throw new Exception("Whoops!!. Error al concluir el pedido!");
        }
        ordenVentaResult.setFacturaid(facturaVenta);
        return ordenVentaResult;
    }
    
    
    
}
