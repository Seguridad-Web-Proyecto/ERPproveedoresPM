package jsf.clases;

import entidades.Producto;
import entidades.Facturaventa;
import entidades.Ventadetalle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.print.attribute.HashAttributeSet;
import restapplication.api_consumer.APIConsumer;
import restapplication.pojos.InventarioPOJO;
import restapplication.pojos.ProductoPOJO;

@Named("productoController1")
@SessionScoped
public class ProductoController1 implements Serializable
{

    private List<ProductoPOJO> items = null;
    private ProductoPOJO selected;
    private List<ProductoPOJO> selectedWs;


    public ProductoController1()
    {
    }

    public ProductoPOJO getSelected()
    {
        return selected;
    }

    public void setSelected(ProductoPOJO selected)
    {
        this.selected = selected;
    }

    public List<ProductoPOJO> getSelectedWs()
    {
        return selectedWs;
    }

    public void setSelectedWs(List<ProductoPOJO> selectedWs)
    {
        this.selectedWs = selectedWs;
    }

    protected void setEmbeddableKeys()
    {
    }

    protected void initializeEmbeddableKey()
    {
    }

    public List<ProductoPOJO> getItemsWs()
    {
        items = APIConsumer.productos("");
        return items;
    }



    
    public void solicitarPedido(){
        ArrayList<Ventadetalle> ventadetalleList = new ArrayList<>();
        for(ProductoPOJO productoWs: selectedWs){
            //Construimos el producto para solicitar
            Producto producto = new Producto();
            producto.setProductoid(productoWs.getProductoid());
            //Construimos el objeto de venta detalle
            Ventadetalle ventadetalle = new Ventadetalle();
            ventadetalle.setProducto(producto);
            ventadetalle.setCantidad(productoWs.getCantidad());
            ventadetalleList.add(ventadetalle);
        }
        try {
            Facturaventa facturaventa = APIConsumer.generarPedidoCompleto("Solicitando pedido de frutas y verduras para el proveedor",
                    ventadetalleList);
            System.out.println("Pedido realizado exitosamente!");
        } catch (Exception ex) {
            Logger.getLogger(ProductoController1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}