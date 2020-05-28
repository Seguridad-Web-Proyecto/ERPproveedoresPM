/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package restapplication.pojos;

import entidades.Categoria;
import entidades.Compradetalle;
import entidades.Ganancia;
import entidades.Ventadetalle;
import java.io.Serializable;
import java.util.Collection;

/**
 *
 * @author jcami
 */
public class ProductoPOJO implements Serializable{
    private static final long serialVersionUID = 1L;

    private Long productoid;

    private String nombre;

    private String descripcion;

    private String unidadMedida;

    private int precioUnitario;

    private Collection<Compradetalle> compradetalleCollection;

    private Categoria categoriaid;

    private Ganancia ganancia;

    private Collection<Ventadetalle> ventadetalleCollection;
    
    private Collection<InventarioPOJO> inventarioCollection;

    public ProductoPOJO() {
    }
    
    public ProductoPOJO(Long productoid, String nombre, String descripcion, String unidadMedida, int precioUnitario) {
        this.productoid = productoid;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.unidadMedida = unidadMedida;
        this.precioUnitario = precioUnitario;
    }

    public Long getProductoid() {
        return productoid;
    }

    public void setProductoid(Long productoid) {
        this.productoid = productoid;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public int getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(int precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public Collection<Compradetalle> getCompradetalleCollection() {
        return compradetalleCollection;
    }

    public void setCompradetalleCollection(Collection<Compradetalle> compradetalleCollection) {
        this.compradetalleCollection = compradetalleCollection;
    }

    public Categoria getCategoriaid() {
        return categoriaid;
    }

    public void setCategoriaid(Categoria categoriaid) {
        this.categoriaid = categoriaid;
    }

    public Ganancia getGanancia() {
        return ganancia;
    }

    public void setGanancia(Ganancia ganancia) {
        this.ganancia = ganancia;
    }

    public Collection<Ventadetalle> getVentadetalleCollection() {
        return ventadetalleCollection;
    }

    public void setVentadetalleCollection(Collection<Ventadetalle> ventadetalleCollection) {
        this.ventadetalleCollection = ventadetalleCollection;
    }

    public Collection<InventarioPOJO> getInventarioCollection() {
        return inventarioCollection;
    }

    public void setInventarioCollection(Collection<InventarioPOJO> inventarioCollection) {
        this.inventarioCollection = inventarioCollection;
    }
    
    
    
}
