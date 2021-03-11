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
import java.util.ArrayList;
import java.util.List;
import javax.json.Json;
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
        JsonObject datosOk = null;
        JsonObject datosError = null;
        JsonObject NotFound = null;

        JSONObject datosEntrada = new JSONObject(cadena);
        String customerId = datosEntrada.getString("customerId").trim();
        CustomerDAO datos = new CustomerDAO();
        //Persona persona = datos.detailss(customerId);
        FacadeCustomer f = null;

        return Response.status(Response.Status.OK).entity(datosOk).build();
    }

}
