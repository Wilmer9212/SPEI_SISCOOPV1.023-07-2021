/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fenoreste.rest.services;

import com.fenoreste.rest.ResponseDTO.ProductsDTO;
import com.fenoreste.rest.dao.ProductsDAO;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Elliot
 */
@Path("/Products")
public class ProductsResources {

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response getProducts() {
        javax.json.JsonObject datosOK =null;
        JsonArrayBuilder arraycuentas = Json.createArrayBuilder();
        ProductsDAO pr = new ProductsDAO();
        try {
            List<ProductsDTO> ListaProductos = pr.getProductos();
            if(ListaProductos != null){
            for (int i=0;i<ListaProductos.size();i++) {
                    JsonObjectBuilder data = Json.createObjectBuilder();
                    ProductsDTO prod=ListaProductos.get(i);  
                    datosOK = data
                            .add("productCode",prod.getIdproducto())
                            .add("accountType",prod.getProducttypename())
                            .add("description",prod.getDescripcion())
                            .add("currencyCode", prod.getProducttypeid())
                            .add("frecuency",Json.createObjectBuilder().add("value",90).add("valueType","integer").add("isSensitive","false").build())
                            .add("period",Json.createObjectBuilder().add("value","D").add("valueType","string").add("isSensitive","false").build())
                            .build();
                    arraycuentas.add(datosOK);
            }
            javax.json.JsonObject Found = Json.createObjectBuilder()
                        .add("products", arraycuentas)
                        .build();
           
           return Response.status(Response.Status.OK).entity(Found).build();
        }else{
         com.github.cliftonlabs.json_simple.JsonObject Not_Found = new com.github.cliftonlabs.json_simple.JsonObject();
         Not_Found.put("Error","DATOS NO ENCONTRADOS");
         Response.status(Response.Status.NO_CONTENT).entity(Not_Found).build();
        }
        } catch (Exception e) {
                 return null;
        }finally{
         pr.cerrar();
        }
        return null;
    }
}