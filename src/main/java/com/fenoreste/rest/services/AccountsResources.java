package com.fenoreste.rest.services;

import com.fenoreste.rest.Auth.Security;
import com.fenoreste.rest.ResponseDTO.AccountHoldersDTO;
import com.fenoreste.rest.ResponseDTO.DetailsAccountDTO;
import com.fenoreste.rest.ResponseDTO.HoldsDTO;
import com.fenoreste.rest.ResponseDTO.StatementsDTO;
import com.fenoreste.rest.Dao.AccountsDAO;
import com.fenoreste.rest.Dao.TransfersDAO;
import com.fenoreste.rest.Entidades.AuxiliaresD;
import com.fenoreste.rest.Entidades.transferencias_completadas_siscoop;
import com.fenoreste.rest.Util.AbstractFacade;
import com.github.cliftonlabs.json_simple.JsonObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonValue;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.codehaus.jettison.json.JSONException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.xhtmlrenderer.pdf.ITextRenderer;

@Path("api/account")
public class AccountsResources {

    @POST
    @Path("/holders")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response accountHolders(String cadena, @HeaderParam("authorization") String authString) throws JSONException {
        Security scr = new Security();
        if (!scr.isUserAuthenticated(authString)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        JSONObject request = new JSONObject(cadena);
        String accountId = "";
        JsonObject jsonb = new JsonObject();
        List<AccountHoldersDTO> listaHolders = null;
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

    @POST
    @Path("/internal/validate")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response validateInternalAccount(String cadena, @HeaderParam("authorization") String authString) {
        Security scr = new Security();
        if (!scr.isUserAuthenticated(authString)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
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
            create = Json.createObjectBuilder().add("accountId", accountId).add("productType", acDao.accountType(p).toUpperCase()).add("holders", Json.createArrayBuilder().add((JsonValue) Json.createObjectBuilder().add("name", holder.getName()).add("relationCode", holder.getRelationCode()).build())).build();
            return Response.status(Response.Status.OK).entity(create).build();
        } catch (Exception e) {
            System.out.println("Error al obtener objetos Json:" + e.getMessage());
        } finally {
            acDao.cerrar();
        }
        return null;
    }

    @POST
    @Path("/statements")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response statements(String cadena, @HeaderParam("authorization") String authString) {
        Security scr = new Security();
        System.out.println("cadena:"+cadena);
        if (!scr.isUserAuthenticated(authString)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
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
                JSONObject js = (JSONObject) listaFil.get(0);
                JSONObject js1 = (JSONObject) listaFil.get(1);
                id = js.getString("value");
                fd = js1.getString("value");
                System.out.println("id:" + id + ",fd:" + fd);
            }
            accountId = jsonRecibido.getString("accountId");
            System.out.println("AccountId:" + accountId);
            String nombrePDF = acDao.statements(accountId, id, fd, pageStartIndex, pageSize);

            JsonObject create = null;
            JsonArrayBuilder listaJson = Json.createArrayBuilder();

            javax.json.JsonObject jsi = Json.createObjectBuilder().add("statementId", accountId).add("dateFrom", id).add("dateTo", fd).add("displayName", accountId).add("availableFormats", Json.createArrayBuilder().add((JsonValue) Json.createObjectBuilder().add("type", "PDF").add("fileId", nombrePDF.replace(".pdf", "")).build())).build();
            listaJson.add((JsonValue) jsi);

            javax.json.JsonObject Found = Json.createObjectBuilder().add("statements", listaJson).build();
            return Response.status(Response.Status.OK).entity(Found).build();
        } catch (Exception e) {
            System.out.println("Error al obtener objetos Json:" + e.getMessage());
        } finally {
            acDao.cerrar();
        }
        return null;
    }

    @POST
    @Path("/holds")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response Holds(String cadena, @HeaderParam("authorization") String authString) {
        Security scr = new Security();
        if (!scr.isUserAuthenticated(authString)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
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
                String ff = String.valueOf(dto.getEntryDate()) + " 00:00:00";
                Timestamp tss = Timestamp.valueOf(ff);
                System.out.println("tss:" + tss);
                ZonedDateTime zonedDateTime = ZonedDateTime.parse(dto.getEntryDate()+"T00:00:00.000-07:00");
                String feR = String.valueOf(zonedDateTime);
                System.out.println("feR:" + feR);
                javax.json.JsonObject jsi = Json.createObjectBuilder().add("holdId", dto.getHoldId()).add("amount", Json.createObjectBuilder().add("amount", dto.getAmount().doubleValue()).add("currencyCode", "MXN").build()).add("entryDate", feR).add("description", dto.getDescritpion()).build();
                listaJson.add((JsonValue) jsi);
            }
            javax.json.JsonObject Found = Json.createObjectBuilder().add("holds", listaJson).build();
            return Response.status(Response.Status.OK).entity(Found).build();
        } catch (Exception e) {
            System.out.println("Error:" + e.getMessage());
            return null;
        }
    }

    @POST
    @Path("/history")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response History(String cadena, @HeaderParam("authorization") String authString) {
        Security scr = new Security();
        if (!scr.isUserAuthenticated(authString)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
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
                JSONObject js = (JSONObject) listaFil.get(0);
                JSONObject js1 = (JSONObject) listaFil.get(1);
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
                fe = sdf.format(dto.getFechaejecucion()).replace("/","-");
                ZonedDateTime zonedDateTime = ZonedDateTime.parse(fe+"T00:00:00.000-07:00");
                String feR = String.valueOf(zonedDateTime); 
                
                System.out.println("Fer:"+feR);
                javax.json.JsonObject jsi = Json.createObjectBuilder().add("transactionId", String.valueOf(dto.getId())).add("amount", (JsonValue) Json.createObjectBuilder().add("amount", dto.getMonto().intValue()).add("currencyCode", "MXN").build()).add("postingDate",feR).add("valueDate", fe.replace("/","-")).add("runningBalance", (JsonValue) Json.createObjectBuilder().add("amount", 0).add("currencyCode", "MXN").build()).add("description", dto.getComentario1()).add("originatorReferenceId", String.valueOf(dto.getId())).add("originatorCode", String.valueOf(dto.getId())).add("description2", (JsonValue) Json.createObjectBuilder().add("value",String.valueOf(dto.getId())).add("valueType", "string").add("isSensitive",false).build()).build();
                listaJson.add((JsonValue) jsi);
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
    @Path("/details")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response Details(String cadena, @HeaderParam("authorization") String authString) {
        Security scr = new Security();
        if (!scr.isUserAuthenticated(authString)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        String accountId = "";
        AccountsDAO dao = new AccountsDAO();
        JSONObject jsonRecibido = new JSONObject(cadena);
        try {
            accountId = jsonRecibido.getString("accountId");
            DetailsAccountDTO dto = dao.detailsAccount(accountId);
            System.out.println("DTO:" + dto);
            JsonObject create = null;
            javax.json.JsonObject jsi = Json.createObjectBuilder().add("accountDetails", Json.createObjectBuilder()
                    .add("accountId", dto.getAccountId())
                    .add("accountNumber", dto.getAccountNumber())
                    .add("displayAccountNumber", dto.getDisplayAccountNumber())
                    .add("accountType", dto.getAccountType())
                    .add("currencyCode", dto.getCurrencyCode())
                    .add("productCode", dto.getProductCode())
                    .add("status", dto.getStatus())
                    .add("branch", (JsonValue) Json.createObjectBuilder()
                            .add("value", dto.getSucursal())
                            .add("valueType", "string")
                            .add("isSensitive", false)
                            .build())
                    .add("openedDate", (JsonValue) Json.createObjectBuilder()
                            .add("value", dto.getOpenedDate())
                            .add("valueType", "date")
                            .add("isSensitive", false)
                            .build())
                    .add("iban", (JsonValue) Json.createObjectBuilder()
                            .add("description", "IBAN")
                            .add("isSensitive", true)
                            .add("valueType", "string")
                            .add("value", "******" + dto.getAccountId().substring(15, 19))
                            .build())
                    .build()).build();
            return Response.status(Response.Status.OK).entity(jsi).build();
        } catch (Exception e) {
            System.out.println("Error:" + e.getMessage());
        } finally {
            dao.cerrar();
        }
        return null;
    }

    @POST
    @Path("/create/validate")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response validateCreate(String cadena) {
        try {
            org.codehaus.jettison.json.JSONObject jsonR = new org.codehaus.jettison.json.JSONObject(cadena);
            String accountType = "", customerId = "", productType = "";
            int page = 0, pageSize = 0;
            accountType = jsonR.getString("accountType");
            customerId = jsonR.getString("customerId");
            productType = jsonR.getString("productCode");

            javax.json.JsonObject json = Json.createObjectBuilder().add("validationId", "LK890007")
                    .add("fees", Json.createArrayBuilder())
                    .add("executionDate", "2021-05-12")
                    .build();

            return Response.status(Response.Status.OK).entity(json).build();

        } catch (JSONException ex) {
            System.out.println("Error al crear json:" + ex.getMessage());
        }
        return null;

    }

    @POST
    @Path("/create/execute")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response executeCreate(String cadena) {
        try {
            org.codehaus.jettison.json.JSONObject jsonR = new org.codehaus.jettison.json.JSONObject(cadena);
            String validationId = "";

            validationId = jsonR.getString("validationId");

            javax.json.JsonObject json = Json.createObjectBuilder().add("status", "completed")
                    .add("accountId", "TIME")
                    .add("accountNumber", "67564236465646")
                    .build();

            return Response.status(Response.Status.OK).entity(json).build();

        } catch (JSONException ex) {
            System.out.println("Error al crear json:" + ex.getMessage());
        }
        return null;

    }

    @POST
    @Path("/status/change/validate")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response changeStatus(String cadena, @HeaderParam("authorization") String authString) {
        Security scr = new Security();
        if (!scr.isUserAuthenticated(authString)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        JSONObject jsonRecibido = new JSONObject(cadena);
        javax.json.JsonObject json = null;
        try {
            json = Json.createObjectBuilder().add("validationId", "0967998686787").add("fees", Json.createArrayBuilder()).add("effectiveDate", "").build();
        } catch (Exception e) {
            System.out.println("Error:" + e.getMessage());
        }
        return Response.status(Response.Status.OK).entity(json).build();
    }

    @POST
    @Path("/status/change/execute")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response changeStatusExecute(String cadena, @HeaderParam("authorization") String authString) {
        Security scr = new Security();
        if (!scr.isUserAuthenticated(authString)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        JSONObject jsonRecibido = new JSONObject(cadena);
        javax.json.JsonObject json = null;
        try {
            json = Json.createObjectBuilder().add("status", "completed").build();
        } catch (Exception e) {
            System.out.println("Error:" + e.getMessage());
        }
        return Response.status(Response.Status.OK).entity(json).build();
    }

    @POST
    @Path("/validateBeneficiary")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response validateBeneficiary(String cadena, @HeaderParam("authorization") String authString) {
        Security scr = new Security();
        if (!scr.isUserAuthenticated(authString)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
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
            create = Json.createObjectBuilder().add("beneficiaryAccount", (JsonValue) Json.createObjectBuilder().add("accountNumber", lista.get(2)).add("accountType", lista.get(1)).add("accountSchemaType", "internal").build()).add("beneficiary", (JsonValue) Json.createObjectBuilder().add("name", lista.get(0)).add("countryCode", "MX").build()).build();
            return Response.status(Response.Status.OK).entity(create).build();
        } catch (Exception e) {
            System.out.println("Error al obtener objetos Json:" + e.getMessage());
        } finally {
            acDao.cerrar();
        }
        return null;
    }

    @GET
    @Path("/currentBusinessDate")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response BussinesDate(@HeaderParam("authorization") String authString) {
        Security scr = new Security();
        if (!scr.isUserAuthenticated(authString)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
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
    @Path("/file")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response file(String cadena) throws FileNotFoundException {
        /*String opa="",fechaInit="",fechaFin="";
        JSONObject jsonRequest=new JSONObject(cadena);
        try{
            opa=jsonRequest.getString("accountId");
            fechaInit=jsonRequest.getString("initialDate");
            fechaFin=jsonRequest.getString("finalDate");            
        }catch(Exception e){
            System.out.println("Error al obtener datos json:"+e.getMessage());
        }
        
         File file=null;
        try{
        file = crear_llenar_txt(opa, fechaInit,fechaFin);
        //file=new File(ruta()+"e_cuenta_ahorro_0101010011000010667_2.txt");
        System.out.println("fileNameTxt:"+file.getName());
        File fileTxt=new File(ruta()+file.getName());
        if(fileTxt.exists()){
        File fileHTML= crear_llenar_html(fileTxt,fileTxt.getName().replace(".txt",".html"));  
        if(crearPDF(ruta(),fileHTML.getName())){
            String pdf=ruta()+fileHTML.getName().replace(".html","pdf");
            fileTxt.delete();
            fileHTML.delete();              
        }  
        }
        }catch(Exception e){
            System.out.println("Error en conver:"+e.getMessage()); 
        }
        
        Response.ResponseBuilder response = Response.ok(file);
        response.header("Content-Disposition", "attachment; filename=cv.pdf");
        return response.build();*/
        return null;
    }

}
