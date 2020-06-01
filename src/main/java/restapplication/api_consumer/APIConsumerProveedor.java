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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response.Status;

import static restapplication.api_consumer.ClienteHTTP.peticionHttpGet;
import restapplication.pojos.ProductoPOJO;

/**
 *
 * @author jcami
 */
@Stateless
public class APIConsumerProveedor {
    
    private static final String pathProductos = "http://localhost:8080/ERPsubproveedoresPM/webresources/productos";
    private static final String pathCategorias = "http://localhost:8080/ERPsubproveedoresPM/webresources/categorias";
    
    private static final String USER_AGENT = "Mozilla/5.0";
     private static final String URL_BASE = "localhost:8080/ERPsubproveedoresPM/webresources";
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
    
    public static Object realizarPedido(Ordenventa ordenventa){
        String jsonOrdenVenta = ClassToJson.ordenVentaToJson(ordenventa);
        String responseString = "";
        try {
            System.out.println(jsonOrdenVenta);
            ObjectMapper mapper = new ObjectMapper();
            Ordenventa pruebaOrden = mapper.readValue(jsonOrdenVenta, new TypeReference<Ordenventa>(){});
            
            responseString = ClienteHTTP.httpPOST(URL_BASE+"/pedidos", jsonOrdenVenta);
            Ordenventa ordenventaResult = mapper.readValue(responseString, new TypeReference<Ordenventa>(){});
            return ordenventaResult;
        } catch (Exception ex) {
            Logger.getLogger(APIConsumerProveedor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return responseString;
    }
    
    public static Object agregarDetallesAlPedido(Ordenventa ordenventa) throws JsonProcessingException{
        System.out.println("Proveedores -> Subproveedores. Agregando detalles al pedido");
        if(ordenventa.getVentadetalleCollection()==null) return null;
        ObjectMapper mapper = new ObjectMapper();
        String jsonOrdenVenta = mapper.writeValueAsString(ordenventa);
        String responseString = "";
        try {
            System.out.println(jsonOrdenVenta);
            Ordenventa pruebaOrden = mapper.readValue(jsonOrdenVenta, new TypeReference<Ordenventa>(){});
            responseString = ClienteHTTP.httpPUT(URL_BASE+"/pedidos/detalles", jsonOrdenVenta);
            return responseString;
        } catch (Exception ex) {
            Logger.getLogger(APIConsumerProveedor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return responseString;
    }
    
    public static Object concluirPedido(Ordenventa ordenventa) throws JsonProcessingException{
        System.out.println("Proveedores -> Subproveedores. Solicitando el pedido...");
        ObjectMapper mapper = new ObjectMapper();
        String jsonOrdenVenta = mapper.writeValueAsString(ordenventa);
        String responseString = "";
        try {
            System.out.println(jsonOrdenVenta);
            Ordenventa pruebaOrden = mapper.readValue(jsonOrdenVenta, new TypeReference<Ordenventa>(){});
            responseString = ClienteHTTP.httpPOST(URL_BASE+"/pedidos/solicitar", jsonOrdenVenta);
            Facturaventa facturaventaResult = mapper.readValue(responseString, new TypeReference<Facturaventa>(){});
            return facturaventaResult;
        } catch (Exception ex) {
            Logger.getLogger(APIConsumerProveedor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return responseString;
    }
    
    public static Ordenventa generarPedidoCompleto(String descripcion, ArrayList<Ventadetalle> ventaDetalleList) throws Exception{
        //CONSTRUYO EL OBJETO DE ORDEN DE VENTA
        Ordenventa ordenventa = new Ordenventa();
        Cliente cliente = new Cliente();
        cliente.setEmail("proveedor@company.mx");
        ordenventa.setClienteid(cliente);
        ordenventa.setDescripcion(descripcion);
        ordenventa.setVentadetalleCollection(ventaDetalleList);
        //ENVIANDO ORDEN DE VENTA
        String msg; //String response
        
        Object responseOrdenVenta = APIConsumerProveedor.realizarPedido(ordenventa);
        Ordenventa ordenVentaResult;
        try{
            ordenVentaResult = (Ordenventa) responseOrdenVenta;
        }catch(Exception ex){ //si no puede castear el objeto es un error que arrojó
            msg = String.valueOf(responseOrdenVenta);
            throw new Exception("Whoops!!. Error al realizar un pedido!\n"+msg);
        }
        // DETALLES
        ordenVentaResult.setVentadetalleCollection(ventaDetalleList);
        Object responseDetalles = APIConsumerProveedor.agregarDetallesAlPedido(ordenVentaResult);
        msg = String.valueOf(responseDetalles);
        if(!msg.equals("OK")){
            throw new Exception("Whoops!!. Error al añadir los detalles al pedido!\n"+msg); 
        }
        // CONLUYENDO PEDIDO Y RECIBIENDO LA FACTURA
        Object responseCompletarPedido = APIConsumerProveedor.concluirPedido(ordenVentaResult);
        Facturaventa facturaventa = null;
        try{
            facturaventa = (Facturaventa) responseCompletarPedido;
        }catch(Exception ex){
            msg = String.valueOf(responseCompletarPedido);
            throw new Exception("Whoops!!. Error al concluir el pedido!");
        }
        ordenVentaResult.setFacturaid(facturaventa);
        return ordenVentaResult;
    }
    
    
    
}
