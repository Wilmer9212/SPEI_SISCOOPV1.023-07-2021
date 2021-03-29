/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fenoreste.rest.services;

import com.fenoreste.rest.ResponseDTO.CustomerAccountDTO;
import com.fenoreste.rest.ResponseDTO.CustomerContactDetailsDTO;
import com.fenoreste.rest.ResponseDTO.CustomerDetailsDTO;
import com.fenoreste.rest.ResponseDTO.CustomerSearchDTO;
import com.fenoreste.rest.dao.CustomerDAO;
import com.fenoreste.rest.dao.FacadeCustomer;
import com.github.cliftonlabs.json_simple.JsonObject;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.Consumes;
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
@Path("/Customer")
public class CustomerResources {

    @POST
    @Path("/search")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response search(String cadena){
        CustomerDAO datos = new CustomerDAO();

        JsonObject JsonSocios = new JsonObject();
        JsonObject Not_Found = new JsonObject();
        JSONObject datosEntrada = new JSONObject(cadena);
        System.out.println("Objeto Json:" + datosEntrada);
        System.out.println("cadena:" + cadena);

        JSONObject mainObject = new JSONObject(cadena);
        String cif="";
        for (int i = 0; i < mainObject.length(); i++) {
            JSONArray  fi = mainObject.getJSONArray("filters");
            for (int x = 0; x < fi.length(); x++) {
                JSONObject jsonO = (JSONObject) fi.get(x);
                cif=jsonO.getString("value");
            }
        }

        try {
            List<CustomerSearchDTO> lista = datos.search(cif);
            CustomerSearchDTO cliente = null;
            if (lista.size() > 0) {
                JsonSocios.put("customers", lista);
                return Response.status(Response.Status.OK).entity(JsonSocios).build();
            } else {
                Not_Found.put("title", "socios no encontrados");
                return Response.status(Response.Status.NO_CONTENT).entity(Not_Found.toString()).build();
            }

        } catch (Exception e) {
            datos.cerrar();
            return null;

        } finally {
            datos.cerrar();
        }
    }

    @POST
    @Path("/details")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response getDetails(String cadena){
        CustomerDAO datos = new CustomerDAO();

        JsonObject Not_Found = new JsonObject();
        JsonObject Error = new JsonObject();
        JsonObject JsonSocios = new JsonObject();
        try {
            JSONObject jsonE = new JSONObject(cadena);
            String customerId = jsonE.getString("customerId");
            CustomerDetailsDTO socio = datos.details(customerId);

            if (socio!=null) {
                JsonSocios.put("customer",socio);
                return Response.status(Response.Status.ACCEPTED).entity(JsonSocios).build();

            } else {
                Not_Found.put("Error", "socios no encontrados");
                return Response.status(Response.Status.NO_CONTENT).entity(JsonSocios).build();
            }
        } catch (Exception e) {
            return null;
        } finally {
            datos.cerrar();
        }

    }

    @POST
    @Path("/contactDetails")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response contactDetails(String cadena) throws Throwable {
        JsonObject Error = new JsonObject();
        CustomerDAO datos = new CustomerDAO();
        JsonObject MiddleContacts = new JsonObject();
        JSONObject datosEntrada = new JSONObject(cadena);
        String ogs = datosEntrada.getString("customerId");
        System.out.println("Objeto Json:" + datosEntrada);
        System.out.println("cadena:" + cadena);
        try {
            List<CustomerContactDetailsDTO> listaContacto = datos.ContactDetails(ogs);
            com.github.cliftonlabs.json_simple.JsonArray json=new com.github.cliftonlabs.json_simple.JsonArray();
            if (listaContacto.size() > 0) {
                for(int i=0;i<listaContacto.size();i++){    
                    System.out.println("entro al fo");
                    CustomerContactDetailsDTO dto = listaContacto.get(i);
                    JsonObject jsonT=new JsonObject();
                    if(dto.getPhoneNumber()!=null){
                        jsonT.put("customerContactId", ogs);
                        jsonT.put("customerContactType",dto.getCustomerContactType());
                        jsonT.put("phone",dto.getPhoneNumber()); 
                        json.add(jsonT);
                    }
                    if(dto.getCellphoneNumber()!=null){
                        jsonT.put("customerContactId", ogs);
                        jsonT.put("customerContactType",dto.getCustomerContactType());
                        jsonT.put("cellPhone",dto.getCellphoneNumber()); 
                        json.add(jsonT);
                    }
                    
                     if(dto.getEmail()!=null){
                        jsonT.put("customerContactId", ogs);
                        jsonT.put("customerContactType",dto.getCustomerContactType());
                        jsonT.put("email",dto.getEmail()); 
                        json.add(jsonT);
                    }
                } 
                MiddleContacts.put("contactDetails", json);
                return Response.status(Response.Status.OK).entity(MiddleContacts).build();
            } else {
                Error.put("Error", "Datos no encontrados");
                return Response.status(Response.Status.NO_CONTENT).entity(Error).build();
            }
        } catch (Exception e) {
            datos.cerrar();
            return null;
        }

    }

    @POST
    @Path("/accounts")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response getAccounts(String cadena){
        CustomerDAO datos = new CustomerDAO();
        javax.json.JsonObject datosOK = null;
        javax.json.JsonArrayBuilder arrayCuentas = Json.createArrayBuilder();
        JsonObject Not_Found = new JsonObject();
        JsonObject Error = null;
        try {
            JSONObject mainObject = new JSONObject(cadena);
            String cif = mainObject.getString("customerId");
            System.out.println("Cif:" + cif);
            List<CustomerAccountDTO> cuentas = datos.Accounts(cif);
            if (cuentas.size() > 0) {
                for (int i = 0; i < cuentas.size(); i++) {
                    JsonObjectBuilder data = Json.createObjectBuilder();
                    CustomerAccountDTO cuenta = cuentas.get(i);
                    datosOK = data
                            .add("accountId", cuenta.getAccountId())
                            .add("accountNumber", cuenta.getAccountNumber())
                            .add("displayAccountNumber", cuenta.getDisplayAccountNumber())
                            .add("accountType", cuenta.getAccountTye())
                            .add("currencyCode", cuenta.getCurrencyCode())
                            .add("productCode", cuenta.getProductCode())
                            .add("status", cuenta.getStatus())
                            .add("restrictions", Json.createArrayBuilder()
                                    .build())
                            .add("customerRelations", Json.createArrayBuilder()
                                    .add(Json.createObjectBuilder()
                                            .add("relationCode", "SOW")
                                            .add("relationtype", "self")
                                            .build())
                                    .build())
                            .add("hasBalances", true)
                            .build();

                    arrayCuentas.add(datosOK);

                }
                javax.json.JsonObject Found = Json.createObjectBuilder()
                        .add("accounts", arrayCuentas)
                        .build();
                return Response.status(Response.Status.OK).entity(Found).build();
            } else {
                Not_Found.put("Error", "Sin registros para usuario:" + cif);
                return Response.status(Response.Status.NOT_FOUND).entity(Not_Found).build();

            }

        } catch (Exception e) {
            Error.put("title", "parametros incorrectos");
            datos.cerrar();
            return null;

        } finally {
            datos.cerrar();
        }
    }

    @POST
    @Path("/templates")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response templates(String cadena) {
        JsonObject datosOk = new JsonObject();
        JsonObject jsito = new JsonObject();
        JSONObject datosEntrada = new JSONObject(cadena);
        String customerId = datosEntrada.getString("customerId").trim();
        CustomerDAO dao = new CustomerDAO();
        boolean bandera = dao.findCustomer(customerId);
        List lista = new ArrayList();
        lista.add("Single-user-Template");
        lista.add("Single-user for Apps Template");
        jsito.put("valueType","String");
        jsito.put("value",0);
        try { 
            if (bandera) {                  
                   System.out.println("Lista:"+lista);
                    datosOk.put("Templates",lista);
                    datosOk.put("property1",jsito);
                
                    return Response.status(Response.Status.OK).entity(datosOk).build(); 
            }else{
                datosOk.put("Error","SOCIO NO ENCONTRADO");
                     return Response.status(Response.Status.NO_CONTENT).entity(datosOk).build();
            }
        } catch (Exception e) {
            System.out.println("Error:" + e.getMessage());
            dao.cerrar();
        }finally{
            dao.cerrar();
        }
        return null;

   
    }

    @POST
    @Path("/validateSetContactDetails")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response ValidateSetContactDetails(String cadena) {
        JsonObject datosOk = new JsonObject();
        JsonObject datosError = new JsonObject();

        JSONObject datosEntrada = new JSONObject(cadena);
        String customerId=datosEntrada.getString("customerId");
        String tel1="",tel2="";
        try {
            System.out.println("ObjetoJson:"+datosEntrada);
            JSONArray jsona=datosEntrada.getJSONArray("contactEntities");
            JSONObject json1=(JSONObject) jsona.get(0);
            JSONObject json2=(JSONObject) jsona.get(1);
            tel1=json1.getString("phoneNumber");
            tel2=json2.getString("phoneNumber");
            System.out.println("Tel 1:"+tel1+",Tel 2:"+tel2);
            
        } catch (Exception e) {
            System.out.println("Error:"+e.getMessage());
        }
        CustomerDAO dao=new CustomerDAO();
        try {
            String cadenas=dao.validateSetContactDetails(customerId, tel1);
            if(cadenas.equals("")){
                datosError.put("Error","No existe id de validacion");
                return Response.status(Response.Status.NO_CONTENT).entity(datosError).build();
            }
            else{
                datosOk.put("validationId",cadenas.toUpperCase());
                return Response.status(Response.Status.OK).entity(datosOk).build();
            }
        } catch (Exception e) {
            dao.cerrar();
            System.out.println("Error general");
            return null;
        }finally{
            dao.cerrar();
        }
        
    }
    
    @POST
    @Path("/executeSetContactDetails")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response executeSetContactDetails(String cadena) {
        JsonObject datosOk = new JsonObject();
        JsonObject datosError = new JsonObject();

        JSONObject datosEntrada = new JSONObject(cadena);
        String validationId="";
        try {
        validationId=datosEntrada.getString("validationId");
        } catch (Exception e) {
        datosError.put("Error",validationId+" No es parametro reconocido");
        return Response.status(Response.Status.BAD_GATEWAY).entity(datosError).build();
        }
        
        CustomerDAO dao=new CustomerDAO();
        try {
           String status=dao.executeSetContactDetails(validationId);
           datosOk.put("status", status);            
         return Response.status(Response.Status.OK).entity(datosOk).build();
        } catch (Exception e) {
            dao.cerrar();
        }
        finally{
            dao.cerrar();
        }
    return null;
    }
    
    @POST
    @Path("/position")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response GetPosition(String cadena) {
        JsonObject datosOk = new JsonObject();
        JsonObject datosError = new JsonObject();

        JSONObject datosEntrada = new JSONObject(cadena);
        String customerId="",balanceLedger="",balanceAvalaible="";
        
        try {
        customerId=datosEntrada.getString("customerId");
        JSONArray lista=datosEntrada.getJSONArray("balanceTypes");
        balanceAvalaible=(String) lista.get(0);
        balanceLedger=(String)lista.get(1);
            System.out.println("Balance:"+balanceAvalaible+","+balanceLedger);
        
        } catch (Exception e) {
           datosError.put("Error","Request Json Failed");
        return Response.status(Response.Status.BAD_GATEWAY).entity(datosError).build();
        }
        
        CustomerDAO dao=new CustomerDAO();
        Double[]arr=new Double[2];
          javax.json.JsonObject json1=null;
        JsonArrayBuilder jsona=Json.createArrayBuilder();
         
         System.out.println("Json:"+json1);
        try {
           if(!balanceLedger.equals("") && !balanceAvalaible.equals("")){
              arr=dao.position(customerId);              
           } 
           
             javax.json.JsonObject clientes1=Json.createObjectBuilder().add("balanceType","ledger").add("amount",Json.createObjectBuilder().add("amount",arr[0]).add("currencyCode","MXN").build()).build();
          javax.json.JsonObject clientes2=Json.createObjectBuilder().add("balanceType","avalaible").add("amount",Json.createObjectBuilder().add("amount",arr[1]).add("currencyCode","MXN").build()).build();
         
            json1=Json.createObjectBuilder().add("positionPerCurrency",jsona.add(Json.createObjectBuilder().add("currencyCode","MXN").add("balances",Json.createArrayBuilder().add(clientes1).add(clientes2)))).build();
       
           String status="";//dao.executeSetContactDetails(validationId);
           datosOk.put("status", status);            
         return Response.status(Response.Status.OK).entity(json1).build();
        } catch (Exception e) {
            dao.cerrar();
        }
        finally{
            dao.cerrar();
        }
    return null;
    }
        
        
   @POST
    @Path("/positionHistory")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response PositionHistory(String cadena) {
        JsonObject datosOk = new JsonObject();
        JsonObject datosError = new JsonObject();

        JSONObject datosEntrada = new JSONObject(cadena);
        String customerId="",balanceLedger="",balanceAvalaible="";
        String fecha1="",fecha2="";
        try {
        customerId=datosEntrada.getString("customerId");
        JSONArray lista=datosEntrada.getJSONArray("balanceTypes");
        balanceAvalaible=(String) lista.get(0);
        balanceLedger=(String)lista.get(1);
            System.out.println("Balance:"+balanceAvalaible+","+balanceLedger);
        JSONArray filters=datosEntrada.getJSONArray("filters");
        JSONObject f1=filters.getJSONObject(0);
        JSONObject f2=filters.getJSONObject(1);
        fecha1=f1.getString("value");
        fecha2=f2.getString("value");
        } catch (Exception e) {
           datosError.put("Error:","Request Json Failed");
            System.out.println("Error:"+e.getMessage());
        return Response.status(Response.Status.BAD_GATEWAY).entity(datosError).build();
        }
        
        CustomerDAO dao=new CustomerDAO();
        Double[]arr=new Double[2];
        javax.json.JsonObject json1=null;
        JsonArrayBuilder jsona=Json.createArrayBuilder();
         
        try {
           if(!balanceLedger.equals("") && !balanceAvalaible.equals("")){
              arr=dao.positionHistory(customerId,fecha1.trim().replace("-","/"),fecha2.trim().replace("-","/"));              
           } 
           
          javax.json.JsonObject clientes1=Json.createObjectBuilder().add("balanceType","ledger").add("amount",Json.createObjectBuilder().add("amount",arr[0]).add("currencyCode","MXN").build()).build();
          javax.json.JsonObject clientes2=Json.createObjectBuilder().add("balanceType","avalaible").add("amount",Json.createObjectBuilder().add("amount",arr[1]).add("currencyCode","MXN").build()).build();
         
            json1=Json.createObjectBuilder().add("positionPerCurrency",jsona.add(Json.createObjectBuilder()
                    .add("currencyCode","MXN").add("balances",Json.createArrayBuilder().add(clientes1)
                    .add(clientes2)))).add("positionDate",dao.dateToString(new Date()).replace("/","-")).build();
       
           String status="";//dao.executeSetContactDetails(validationId);
           datosOk.put("status", status);            
         return Response.status(Response.Status.OK).entity(json1).build();
        } catch (Exception e) {
            dao.cerrar();
        }
        finally{
            dao.cerrar();
        }
    return null;
    }
    
    

}
