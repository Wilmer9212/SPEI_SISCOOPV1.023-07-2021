 package com.fenoreste.rest.Services;
 
 import com.fenoreste.rest.ResponseDTO.AccountHoldersDTO;
 import com.fenoreste.rest.ResponseDTO.DetailsAccountDTO;
 import com.fenoreste.rest.ResponseDTO.HoldsDTO;
 import com.fenoreste.rest.ResponseDTO.StatementsDTO;
 import com.fenoreste.rest.dao.AccountsDAO;
 import com.fenoreste.rest.entidades.transferencias_completadas_siscoop;
 import com.github.cliftonlabs.json_simple.JsonObject;
 import java.io.File;
 import java.text.SimpleDateFormat;
 import java.util.Calendar;
 import java.util.List;
 import javax.json.Json;
 import javax.json.JsonArrayBuilder;
 import javax.json.JsonValue;
 import javax.ws.rs.Consumes;
 import javax.ws.rs.GET;
 import javax.ws.rs.POST;
 import javax.ws.rs.Path;
 import javax.ws.rs.Produces;
 import javax.ws.rs.core.Response;
 import org.json.JSONArray;
 import org.json.JSONObject;
 
 @Path("/Accounts")
 public class AccountsResources {
   @POST
   @Path("/validateInternalAccount")
   @Produces({"application/json"})
   @Consumes({"application/json"})
   public Response validateInternalAccount(String cadena) {
     AccountsDAO acDao = new AccountsDAO();
     String accountId = "";
     System.out.println("Cadena:" + cadena);
     try {
       JSONObject jsonRecibido = new JSONObject(cadena);
       System.out.println("JsonRecibido:" + jsonRecibido);
       accountId = jsonRecibido.getString("accountNumber");
       int p = Integer.parseInt(accountId.substring(6, 11));
       List<AccountHoldersDTO> listaHolder = acDao.validateInternalAccount(accountId);
       AccountHoldersDTO holder = listaHolder.get(0);
       javax.json.JsonObject create = null;
       create = Json.createObjectBuilder().add("accountId", accountId).add("productType", acDao.accountType(p).toUpperCase()).add("holders", Json.createArrayBuilder().add((JsonValue)Json.createObjectBuilder().add("name", holder.getName()).add("relationCode", holder.getRelationCode()).build())).build();
       return Response.status(Response.Status.OK).entity(create).build();
     } catch (Exception e) {
       System.out.println("Error al obtener objetos Json:" + e.getMessage());
     } finally {
       acDao.cerrar();
     } 
     return null;
   }
   
   @POST
   @Path("/validateBeneficiary")
   @Produces({"application/json"})
   @Consumes({"application/json"})
   public Response validateBeneficiary(String cadena) {
     AccountsDAO acDao = new AccountsDAO();
     String accountId = "";
     System.out.println("Cadena:" + cadena);
     String accountType = "";
     String nameb = "";
     String address = "";
     try {
       JSONObject jsonRecibido = new JSONObject(cadena);
       System.out.println("JsonRecibido:" + jsonRecibido);
       JSONObject json2 = jsonRecibido.getJSONObject("beneficiaryAccount");
       JSONObject json3 = jsonRecibido.getJSONObject("beneficiary");
       accountId = json2.getString("accountNumber");
       int p = Integer.parseInt(accountId.substring(6, 11));
       accountType = json2.getString("accountType");
       nameb = json3.getString("name");
       address = json3.getString("address");
       System.out.println("json2:" + json2);
       System.out.println("AccountNumber:" + accountId + "\n accounType:" + accountType + "\n name:" + nameb + "\n address:" + address);
       List<String> lista = acDao.validateBeneficiary(accountId, accountType.toUpperCase(), nameb.toUpperCase(), address.toUpperCase());
       javax.json.JsonObject create = null;
       create = Json.createObjectBuilder().add("beneficiaryAccount", (JsonValue)Json.createObjectBuilder().add("accountNumber", lista.get(2)).add("accountType", lista.get(1)).add("accountSchemaType", "internal").build()).add("beneficiary", (JsonValue)Json.createObjectBuilder().add("name", lista.get(0)).add("countryCode", "MX").build()).build();
       return Response.status(Response.Status.OK).entity(create).build();
     } catch (Exception e) {
       System.out.println("Error al obtener objetos Json:" + e.getMessage());
     } finally {
       acDao.cerrar();
     } 
     return null;
   }
   
   @POST
   @Path("/Statements")
   @Produces({"application/json"})
   @Consumes({"application/json"})
   public Response statements(String cadena) {
     AccountsDAO acDao = new AccountsDAO();
     String accountId = "";
     int pageSize = 0;
     int pageStartIndex = 0;
     try {
       JSONObject jsonRecibido = new JSONObject(cadena);
       JSONArray listaFil = jsonRecibido.getJSONArray("filters");
       System.out.println("ListaFil:" + listaFil);
       String id = "";
       String fd = "";
       pageStartIndex = jsonRecibido.getInt("page");
       pageSize = jsonRecibido.getInt("pageSize");
       for (int i = 0; i < listaFil.length(); i++) {
         JSONObject js = (JSONObject)listaFil.get(0);
         JSONObject js1 = (JSONObject)listaFil.get(1);
         id = js.getString("value");
         fd = js1.getString("value");
         System.out.println("id:" + id + ",fd:" + fd);
       } 
       accountId = jsonRecibido.getString("accountId");
       System.out.println("AccountId:" + accountId);
       List<StatementsDTO> lista = acDao.statements(accountId, id, fd, pageStartIndex, pageSize);
       JsonObject create = null;
       JsonArrayBuilder listaJson = Json.createArrayBuilder();
       for (int j = 0; j < lista.size(); j++) {
         StatementsDTO dto = lista.get(j);
         javax.json.JsonObject jsi = Json.createObjectBuilder().add("statementId", dto.getStatementId()).add("dateFrom", dto.getInitialDate()).add("dateTo", dto.getFinalDate()).add("displayName", dto.getDisplayAccountNumber()).add("avalibleFormats", Json.createArrayBuilder().add((JsonValue)Json.createObjectBuilder().add("type", "PDF").add("fileId", dto.getIdpdf()).build())).build();
         listaJson.add((JsonValue)jsi);
       } 
       javax.json.JsonObject Found = Json.createObjectBuilder().add("statements", listaJson).build();
       return Response.status(Response.Status.OK).entity(Found).build();
     } catch (Exception e) {
       System.out.println("Error al obtener objetos Json:" + e.getMessage());
     } finally {
       acDao.cerrar();
     } 
     return null;
   }
   
   @GET
   @Path("/file")
   @Produces({"application/json"})
   @Consumes({"application/json"})
   public Response file() {
     File file = obtenerTxt();
     Response.ResponseBuilder response = Response.ok(file);
     response.header("Content-Disposition", "attachment; filename=cv.pdf");
     return response.build();
   }
   
   @POST
   @Path("/Holds")
   @Produces({"application/json"})
   @Consumes({"application/json"})
   public Response Holds(String cadena) {
     JSONObject jsonre = new JSONObject(cadena);
     String accountId = "";
     AccountsDAO dao = new AccountsDAO();
     try {
       accountId = jsonre.getString("accountId");
       List<HoldsDTO> lista = dao.holds(accountId);
       JsonObject create = null;
       JsonArrayBuilder listaJson = Json.createArrayBuilder();
       for (int i = 0; i < lista.size(); i++) {
         HoldsDTO dto = lista.get(i);
         javax.json.JsonObject jsi = Json.createObjectBuilder().add("holdId", dto.getHoldId()).add("amount", Json.createArrayBuilder().add((JsonValue)Json.createObjectBuilder().add("amount", dto.getAmount().doubleValue()).add("currencyCode", "MXN").build())).add("entryDate", dto.getEntryDate()).add("description", dto.getDescritpion()).build();
         listaJson.add((JsonValue)jsi);
       } 
       javax.json.JsonObject Found = Json.createObjectBuilder().add("holds", listaJson).build();
       return Response.status(Response.Status.OK).entity(Found).build();
     } catch (Exception e) {
       System.out.println("Error:" + e.getMessage());
       return null;
     } 
   }
   
   @GET
   @Path("/currentBusinessDate")
   @Produces({"application/json"})
   @Consumes({"application/json"})
   public Response BussinesDate() {
     Calendar c1 = Calendar.getInstance();
     String dia = Integer.toString(c1.get(5));
     String mes = Integer.toString(c1.get(2) + 1);
     String annio = Integer.toString(c1.get(1));
     String BusinessDate = dia + "/" + mes + "/" + annio;
     JsonObject json = new JsonObject();
     json.put("currentBusinessDate", BusinessDate);
     return Response.status(Response.Status.OK).entity(json).build();
   }
   
   @POST
   @Path("/History")
   @Produces({"application/json"})
   @Consumes({"application/json"})
   public Response History(String cadena) {
     String accountId = "", initialDate = "", finalDate = "";
     int pageSize = 0, pageStartIndex = 0;
     AccountsDAO dao = new AccountsDAO();
     JSONObject jsonRecibido = new JSONObject(cadena);
     try {
       JSONArray listaFil = jsonRecibido.getJSONArray("filters");
       System.out.println("ListaFil:" + listaFil);
       initialDate = "";
       finalDate = "";
       accountId = jsonRecibido.getString("accountId");
       pageSize = jsonRecibido.getInt("pageSize");
       pageStartIndex = jsonRecibido.getInt("page");
       for (int i = 0; i < listaFil.length(); i++) {
         JSONObject js = (JSONObject)listaFil.get(0);
         JSONObject js1 = (JSONObject)listaFil.get(1);
         initialDate = js.getString("value");
         finalDate = js1.getString("value");
         System.out.println("id:" + initialDate + ",fd:" + finalDate);
       } 
       List<transferencias_completadas_siscoop> lista = dao.History(accountId, initialDate, finalDate, pageSize, pageStartIndex);
       System.out.println("Lista:" + lista);
       JsonObject create = null;
       JsonArrayBuilder listaJson = Json.createArrayBuilder();
       SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
       String fe = "";
       for (int j = 0; j < lista.size(); j++) {
         transferencias_completadas_siscoop dto = lista.get(j);
         fe = sdf.format(dto.getFechaejecucion());
         javax.json.JsonObject jsi = Json.createObjectBuilder().add("transactionId", dto.getId().intValue()).add("amount", (JsonValue)Json.createObjectBuilder().add("amount", dto.getMonto().intValue()).add("currencyCode", "MXN").build()).add("postingDate", dto.getFechaejecucion().toString()).add("value", fe).add("runningBalance", (JsonValue)Json.createObjectBuilder().add("amount", 0).add("currencyCode", "MXN").build()).add("description", dto.getComentario1()).add("originatorReferencedId", dto.getId().intValue()).add("originatorCode", "").add("description2", (JsonValue)Json.createObjectBuilder().add("value", dto.getComentario2()).add("valueType", "String").build()).build();
         listaJson.add((JsonValue)jsi);
       } 
       javax.json.JsonObject Found = Json.createObjectBuilder().add("totalRecords", lista.size()).add("queryId", "").add("transactions", listaJson).build();
       return Response.status(Response.Status.OK).entity(Found).build();
     } catch (Exception e) {
       System.out.println("Error:" + e.getMessage());
     } finally {
       dao.cerrar();
     } 
     return null;
   }
   
   @POST
   @Path("/Details")
   @Produces({"application/json"})
   @Consumes({"application/json"})
   public Response Details(String cadena) {
     String accountId = "";
     AccountsDAO dao = new AccountsDAO();
     JSONObject jsonRecibido = new JSONObject(cadena);
     try {
       accountId = jsonRecibido.getString("accountId");
       DetailsAccountDTO dto = dao.detailsAccount(accountId);
       System.out.println("DTO:" + dto);
       JsonObject create = null;
       javax.json.JsonObject jsi = Json.createObjectBuilder().add("accountId", dto.getAccountId()).add("accountNumber", dto.getAccountNumber()).add("displayAccountNumber", dto.getDisplayAccountNumber()).add("accountType", dto.getAccountType()).add("currencyCode", dto.getCurrencyCode()).add("productCode", dto.getProductCode()).add("status", dto.getStatus()).add("branch", (JsonValue)Json.createObjectBuilder().add("value", dto.getSucursal()).add("valueType", "String").add("isSensitive", false).build()).add("openedDate", (JsonValue)Json.createObjectBuilder().add("value", dto.getOpenedDate()).add("valueType", "String").add("isSensitive", false).build()).add("iban", (JsonValue)Json.createObjectBuilder().add("description", "IBAN").add("isSensitive", false).add("valueType", "string").add("value", dto.getAccountNumber()).build()).build();
       return Response.status(Response.Status.OK).entity(jsi).build();
     } catch (Exception e) {
       System.out.println("Error:" + e.getMessage());
     } finally {
       dao.cerrar();
     } 
     return null;
   }
   
   @POST
   @Path("/Holders")
   @Produces({"application/json"})
   @Consumes({"application/json"})
   public Response Holders(String cadena) {
     String accountId = "";
     AccountsDAO dao = new AccountsDAO();
     JSONObject jsonRecibido = new JSONObject(cadena);
     try {
       accountId = jsonRecibido.getString("accountId");
       String nombre = dao.Holders(accountId);
       javax.json.JsonObject jsi = Json.createObjectBuilder().add("name", nombre).add("relationCode", "SOW").build();
       JsonArrayBuilder json = Json.createArrayBuilder();
       json.add((JsonValue)jsi);
       javax.json.JsonObject found = Json.createObjectBuilder().add("holders", json).build();
       return Response.status(Response.Status.OK).entity(found).build();
     } catch (Exception e) {
       System.out.println("Error:" + e.getMessage());
     } finally {
       dao.cerrar();
     } 
     return null;
   }
   
   private static File obtenerTxt() {
     String sf = "C:\\Users\\Elliot\\cv.pdf";
     File f = new File(sf);
     if (f.exists())
       return f; 
     System.out.println("El fichero no existe: " + sf);
     return null;
   }
 }
