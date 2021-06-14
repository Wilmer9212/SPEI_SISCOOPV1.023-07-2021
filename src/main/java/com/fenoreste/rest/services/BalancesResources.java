package com.fenoreste.rest.services;

import com.fenoreste.rest.Auth.Security;
import com.fenoreste.rest.Dao.BalanceDAO;
import com.github.cliftonlabs.json_simple.JsonObject;
import java.util.ArrayList;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonValue;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.json.JSONArray;
import org.json.JSONObject;

@Path("api/balances")
public class BalancesResources {
  @POST
  @Produces({MediaType.APPLICATION_JSON})
  @Consumes({MediaType.APPLICATION_JSON})
  public Response balances(String cadena,@HeaderParam("authorization") String authString){
    Security scr=new Security();
     if(!scr.isUserAuthenticated(authString)){
            return Response.status(Response.Status.UNAUTHORIZED).build();
     }
    JsonObject datosOk = new JsonObject();
    JsonObject datosError = new JsonObject();
    JSONObject datosEntrada = new JSONObject(cadena);
    String customerId = "", balanceLedger = "", balanceAvalaible = "";
    String fecha1 = "", fecha2 = "";
    BalanceDAO dao = new BalanceDAO();
    javax.json.JsonObject json1 = null;
    JsonArrayBuilder jsona = Json.createArrayBuilder();
    List<String> accountsId = new ArrayList<String>();
    try {
      JSONArray accounts = datosEntrada.getJSONArray("accounts");
      for (int i = 0; i < accounts.length(); i++) {
        JSONObject auxJson = accounts.getJSONObject(i);
        String accountId = auxJson.getString("accountId");
        System.out.println("AccountId:" + accountId);
        accountsId.add(accountId);
      } 
        System.out.println("CcountID:"+accountsId);
      String[] arr = dao.balances(accountsId);
        System.out.println("arr[0]:"+arr[0]+", arr[1]:"+arr[1]+",arr[2]:"+arr[2]);
        if(arr[1]==null){
            arr[1]="0";
        }
        
        if(arr[2]==null){
            
            arr[2]="0";
        }
        System.out.println("arr2"+arr[2]);
      javax.json.JsonObject clientes1 = Json.createObjectBuilder().add("balanceType", "ledger").add("amount", (JsonValue)Json.createObjectBuilder().add("amount", arr[1]).add("currencyCode", "MXN").build()).build();
        System.out.println("clientes1:"+clientes1);
      javax.json.JsonObject clientes2 = Json.createObjectBuilder().add("balanceType", "available").add("amount", (JsonValue)Json.createObjectBuilder().add("amount", arr[2]).add("currencyCode", "MXN").build()).build();
      
      json1 = Json.createObjectBuilder().add("balances", jsona.add(Json.createObjectBuilder().add("accountId", arr[0]).add("balances", Json.createArrayBuilder().add((JsonValue)clientes1).add((JsonValue)clientes2)))).build();
  
      return Response.status(Response.Status.OK).entity(json1).build();
    } catch (Exception e) {
      datosError.put("Error:", "Request Json Failed");
      System.out.println("Error:" + e.getMessage());
       return Response.status(Response.Status.BAD_GATEWAY).entity(datosError).build();
    } 
   }
 }

