package jsf.clases;

import entidades.Producto;
import jsf.clases.util.JsfUtil;
import jsf.clases.util.JsfUtil.PersistAction;
import beans.sessions.ProductoFacade;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import restapplication.api_consumer.APIConsumer;
import restapplication.pojos.ProductoPOJO;
import restapplication.service.ProductoFacadeREST;

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

}