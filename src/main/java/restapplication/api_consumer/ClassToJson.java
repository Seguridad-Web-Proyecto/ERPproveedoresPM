/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package restapplication.api_consumer;

import entidades.Ordenventa;
import entidades.Ventadetalle;

/**
 *
 * @author jcami
 */
public class ClassToJson {
    
    public static String ordenVentaToJson(Ordenventa ordenventa){
        String jsonOrdenventa= "{";
        jsonOrdenventa+="\"descripcion\":\""+ordenventa.getDescripcion()+"\",";
        jsonOrdenventa+="\"clienteid\":{\"email\":\""+ordenventa.getClienteid().getEmail()+"\"},";
        jsonOrdenventa+="\"ventadetalleCollection\":[";
        for(Ventadetalle ventadetalle: ordenventa.getVentadetalleCollection()){
            jsonOrdenventa+="{\"cantidad\":"+ventadetalle.getCantidad()+","
                    + "\"producto\":{\"productoid\":"+ventadetalle.getProducto().getProductoid()+"}},";
        }
        jsonOrdenventa = jsonOrdenventa.substring(0, jsonOrdenventa.length()-1);
        jsonOrdenventa+="]}";
        return jsonOrdenventa;
    }
    
}
