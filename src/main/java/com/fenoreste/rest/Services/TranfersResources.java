 package com.fenoreste.rest.Services;
 
 import com.fenoreste.rest.ResponseDTO.AccountHoldersDTO;
 import com.fenoreste.rest.ResponseDTO.validateMonetaryInstructionDTO;
 import com.fenoreste.rest.dao.TransfersDAO;
 import com.github.cliftonlabs.json_simple.JsonObject;
 import java.sql.Timestamp;
 import java.util.ArrayList;
 import java.util.List;
 import javax.json.Json;
 import javax.json.JsonValue;
 import javax.ws.rs.Consumes;
 import javax.ws.rs.GET;
 import javax.ws.rs.POST;
 import javax.ws.rs.Path;
 import javax.ws.rs.Produces;
 import javax.ws.rs.core.Response;
 import org.json.JSONObject;
 
 @Path("Transfers")
 public class TranfersResources {
   @POST
   @Path("/validateMonetaryInstruction")
   @Produces({"application/json"})
   @Consumes({"application/json"})
   public Response validateMonetaryInstruction(String cadena) {
     JSONObject request = new JSONObject(cadena);
     String customerId = "";
     String tipotransferencia = "";
     String corigen = "";
     String cdestino = "";
     int montoTransferencia = 0;
     String comentario = "";
     String corigenprop = "";
     String fechaejecucion = "";
     String tipoejecucion = "";
     JsonObject jsonb = new JsonObject();
     try {
       customerId = request.getString("customerId");
       tipotransferencia = request.getString("originatorTransactionType");
       corigen = request.getString("debitAccountId");
       JSONObject dcdestino = request.getJSONObject("creditAccount");
       cdestino = dcdestino.getString("accountId");
       comentario = request.getString("debtorComments");
       corigenprop = request.getString("creditorComments");
       JSONObject detallemn = request.getJSONObject("monetaryOptions");
       JSONObject monto = detallemn.getJSONObject("amount");
       montoTransferencia = monto.getInt("amount");
       JSONObject execution = detallemn.getJSONObject("execution");
       fechaejecucion = execution.getString("executionDate");
       tipoejecucion = execution.getString("executionType");
     } catch (Exception e) {
       System.out.println("Error:" + e.getMessage());
     } 
     System.out.println("CustomerId:" + customerId + "\ntipoTransferencia=" + tipotransferencia + "\nctaorigen:" + corigen + "\ncdestino:" + cdestino + "\ncomentario:" + comentario + "\npropietariocuentaorigen:" + corigenprop + "\nmontoTransferencia:" + montoTransferencia + "\nfechaejeccuion:" + fechaejecucion + "\ntipoejecucion:" + tipoejecucion);
     TransfersDAO dao = new TransfersDAO();
     validateMonetaryInstructionDTO dto = null;
     try {
       if (!customerId.equals("") && !cdestino.equals("") && montoTransferencia > 0) {
         dto = dao.validateMonetaryInstruction(customerId, tipotransferencia, corigen, cdestino, 
             
             Integer.valueOf(montoTransferencia), comentario, cdestino, fechaejecucion, tipotransferencia);
         if (dto != null)
           return Response.status(Response.Status.OK).entity(dto).build(); 
       } else {
         jsonb.put("Error:", "No cumple los requisitos para realizar la transaccion");
         return Response.status(Response.Status.BAD_GATEWAY).entity(jsonb).build();
       } 
     } catch (Exception e) {
       dao.cerrar();
       System.out.println("Error en response:" + e.getMessage());
     } finally {
       dao.cerrar();
     } 
     return null;
   }
   
   @POST
   @Path("/executeMonetaryInstruction")
   @Produces({"application/json"})
   @Consumes({"application/json"})
   public Response executeMonetaryInstruction(String cadena) {
     JSONObject request = new JSONObject(cadena);
     String validationId = "";
     JsonObject jsonb = new JsonObject();
     try {
       validationId = request.getString("validationId");
     } catch (Exception e) {
       System.out.println("Error:" + e.getMessage());
     } 
     System.out.println("validationId:" + validationId);
     TransfersDAO dao = new TransfersDAO();
     String msj = "";
     try {
       msj = dao.executeMonetaryInstruction(validationId);
       if (msj.equalsIgnoreCase("completed")) {
         jsonb.put("status", msj);
         return Response.status(Response.Status.OK).entity(jsonb).build();
       } 
       jsonb.put("status", msj.toUpperCase());
       return Response.status(Response.Status.BAD_GATEWAY).entity(jsonb).build();
     } catch (Exception e) {
       dao.cerrar();
       System.out.println("Error en response:" + e.getMessage());
     } finally {
       dao.cerrar();
     } 
     return null;
   }
   
   @POST
   @Path("/accountHolders")
   @Produces({"application/json"})
   @Consumes({"application/json"})
   public Response accountHolders(String cadena) {
     JSONObject request = new JSONObject(cadena);
     String accountId = "";
     JsonObject jsonb = new JsonObject();
     List<AccountHoldersDTO> listaHolders = new ArrayList<AccountHoldersDTO>();
     try {
       accountId = request.getString("accountId");
     } catch (Exception e) {
       System.out.println("Error:" + e.getMessage());
     } 
     System.out.println("accountId:" + accountId);
     TransfersDAO dao = new TransfersDAO();
     String msj = "";
     try {
       listaHolders = dao.accountHolders(accountId);
       jsonb.put("holders", listaHolders);
       return Response.status(Response.Status.OK).entity(jsonb).build();
     } catch (Exception e) {
       dao.cerrar();
       System.out.println("Error en response:" + e.getMessage());
     } finally {
       dao.cerrar();
     } 
     return null;
   }
   
   @GET
   @Path("/exchangeRates")
   @Produces({"application/json"})
   @Consumes({"application/json"})
   public Response exhangeRates() {
     Timestamp timestamp = new Timestamp(System.currentTimeMillis());
     System.out.println("Timestamp:" + timestamp);
     try {
       javax.json.JsonObject jsonCreate = Json.createObjectBuilder().add("exchangeRates", (JsonValue)Json.createArrayBuilder().add((JsonValue)Json.createObjectBuilder().add("bookRate", 1).add("timestamp", String.valueOf(timestamp)).add("rates", Json.createArrayBuilder().add((JsonValue)Json.createObjectBuilder().add("rateTypes", "payments").add("buy", 1).add("sell", 1).add("median", 1).build())).add("currencyCode", "MXN").build()).build()).build();
       return Response.status(Response.Status.OK).entity(jsonCreate).build();
     } catch (Exception e) {
       System.out.println("Error en response:" + e.getMessage());
       return null;
     } 
   }
 }
