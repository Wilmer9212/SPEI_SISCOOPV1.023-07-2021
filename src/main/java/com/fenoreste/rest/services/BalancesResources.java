/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fenoreste.rest.services;

import com.fenoreste.rest.dao.BalanceDAO;
import com.github.cliftonlabs.json_simple.JsonObject;
import java.util.ArrayList;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Elliot
 */
@Path("Balances")
public class BalancesResources {
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response balances(String cadena){
       JsonObject datosOk = new JsonObject();
        JsonObject datosError = new JsonObject();
        Double arr[];
        JSONObject datosEntrada = new JSONObject(cadena);
        String customerId="",balanceLedger="",balanceAvalaible="";
        String fecha1="",fecha2="";
        BalanceDAO dao=new BalanceDAO();
        javax.json.JsonObject json1=null;
        JsonArrayBuilder jsona=Json.createArrayBuilder();
        List<String>accountsId=new ArrayList<String>();
        try {
        JSONArray accounts=datosEntrada.getJSONArray("accounts");
        for(int i=0;i<accounts.length();i++){
        JSONObject auxJson=accounts.getJSONObject(i);
        String accountId=auxJson.getString("accountId");
        System.out.println("AccountId:"+accountId);
        accountsId.add(accountId);
        }
        arr=dao.balances(accountsId);        
        javax.json.JsonObject clientes1=Json.createObjectBuilder().add("balanceType","ledger").add("amount",Json.createObjectBuilder().add("amount",arr[0]).add("currencyCode","MXN").build()).build();
        javax.json.JsonObject clientes2=Json.createObjectBuilder().add("balanceType","avalaible").add("amount",Json.createObjectBuilder().add("amount",arr[1]).add("currencyCode","MXN").build()).build();
         
        json1=Json.createObjectBuilder().add("positionPerCurrency",jsona.add(Json.createObjectBuilder().add("currencyCode","MXN").add("balances",Json.createArrayBuilder().add(clientes1).add(clientes2)))).build();
       
           String status="";//dao.executeSetContactDetails(validationId);
           datosOk.put("status", status);            
         return Response.status(Response.Status.OK).entity(json1).build();
                } catch (Exception e) {
           datosError.put("Error:","Request Json Failed");
            System.out.println("Error:"+e.getMessage());
        return Response.status(Response.Status.BAD_GATEWAY).entity(datosError).build();
        } 
        
    }
}
