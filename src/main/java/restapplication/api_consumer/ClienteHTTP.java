/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package restapplication.api_consumer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * @author David Villeda
 */
public class ClienteHTTP {
    
    public static String peticionHttpGet(String urlParaVisitar) throws Exception {
        // Esto es lo que vamos a devolver
        StringBuilder resultado = new StringBuilder();
        // Crear un objeto de tipo URL
        URL url = new URL(urlParaVisitar);
        // Abrir la conexión e indicar que será de tipo GET
        HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
        conexion.setRequestMethod("GET");
        // Búferes para leer
        BufferedReader rd = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
        String linea;
        // Mientras el BufferedReader se pueda leer, agregar contenido a resultado
        while ((linea = rd.readLine()) != null) {
                resultado.append(linea);
        }
        // Cerrar el BufferedReader
        rd.close();
        // Regresar resultado, pero como cadena, no como StringBuilder
        return resultado.toString();
    }
    
    public  static String httpPOST(String url, String jsonInputString) throws Exception, MalformedURLException, MalformedURLException, IOException, MalformedURLException{
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        //Set the Request Method
        con.setRequestMethod("POST");
        //Set the Request Content-Type Header Parameter
        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        //Set Response Format Type
        con.setRequestProperty("Accept", "application/json");
        //Ensure the Connection Will Be Used to Send Content
        con.setDoOutput(true);
        con.setDoInput(true);
        // Create the Request Body
        OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
        wr.write(jsonInputString);
        wr.flush();
        // Read the Response from Input Stream
        StringBuilder response = new StringBuilder();
        try(BufferedReader br = new BufferedReader(
            new InputStreamReader(con.getInputStream(), "UTF-8"))) {
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            System.out.println(response.toString());
        }
        return response.toString();
    }
    
    public  static String httpPUT(String url, String jsonInputString) throws Exception, MalformedURLException, MalformedURLException, IOException, MalformedURLException{
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        //Set the Request Method
        con.setRequestMethod("PUT");
        //Set the Request Content-Type Header Parameter
        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        //Set Response Format Type
        con.setRequestProperty("Accept", "application/json");
        //Ensure the Connection Will Be Used to Send Content
        con.setDoOutput(true);
        con.setDoInput(true);
        // Create the Request Body
        OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
        wr.write(jsonInputString);
        wr.flush();
        // Read the Response from Input Stream
        StringBuilder response = new StringBuilder();
        try(BufferedReader br = new BufferedReader(
            new InputStreamReader(con.getInputStream(), "UTF-8"))) {
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            System.out.println(response.toString());
        }
        return response.toString();
    }
    
}
