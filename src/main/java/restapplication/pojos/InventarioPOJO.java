/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package restapplication.pojos;

import entidades.Producto;
import java.io.Serializable;
import javax.json.bind.annotation.JsonbTransient;
import javax.xml.bind.annotation.XmlTransient;

public class InventarioPOJO implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Long inventarioid;
    
    private int existencias;
    
    private Producto productoid;

    public InventarioPOJO() {
    }

    public InventarioPOJO(Long inventarioid) {
        this.inventarioid = inventarioid;
    }

    public InventarioPOJO(Long inventarioid, int existencias) {
        this.inventarioid = inventarioid;
        this.existencias = existencias;
    }

    public Long getInventarioid() {
        return inventarioid;
    }

    public void setInventarioid(Long inventarioid) {
        this.inventarioid = inventarioid;
    }

    public int getExistencias() {
        return existencias;
    }

    public void setExistencias(int existencias) {
        this.existencias = existencias;
    }

    @XmlTransient
    @JsonbTransient
    public Producto getProductoid() {
        return productoid;
    }

    public void setProductoid(Producto productoid) {
        this.productoid = productoid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (inventarioid != null ? inventarioid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof InventarioPOJO)) {
            return false;
        }
        InventarioPOJO other = (InventarioPOJO) object;
        if ((this.inventarioid == null && other.inventarioid != null) || (this.inventarioid != null && !this.inventarioid.equals(other.inventarioid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Inventario[ inventarioid=" + inventarioid + " ]";
    }
    
}
