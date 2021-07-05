package com.fenoreste.rest.services;

import com.fenoreste.rest.Auth.Security;
import com.fenoreste.rest.Dao.TransfersDAO;
import com.fenoreste.rest.ResponseDTO.AccountHoldersDTO;
import com.fenoreste.rest.ResponseDTO.validateMonetaryInstructionDTO;
import com.github.cliftonlabs.json_simple.JsonObject;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonValue;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

@Path("api/instructions")
public class InstructionsResources {

    @POST
    @Path("/monetary")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response MonetaryInstruction(String cadena, @HeaderParam("authorization") String authString) throws JSONException {
        System.out.println("cadenaMonetaryInstruction:" + cadena);
        Security scr = new Security();
        if (!scr.isUserAuthenticated(authString)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        JSONObject jrecibido = new JSONObject(cadena);
        String customerId = "";
        int pageSize = 0, page = 0;
        try {
            customerId = jrecibido.getString("customerId");
            page = jrecibido.getInt("page");
            pageSize = jrecibido.getInt("pageSize");
            ZonedDateTime zonedDateTime = ZonedDateTime.parse("2021-03-23T18:21+01:00");
            javax.json.JsonObject jprincipal = null;
            jprincipal = Json.createObjectBuilder().add("totalRecords", 0)
                    .add("instructions", Json.createArrayBuilder().add(Json.createObjectBuilder()
                            .add("monetaryInstructionId", "094567")
                            .add("originatorTransactionType", "TRANSFER_OWN")
                            .add("debitAccount", Json.createObjectBuilder()
                                    .add("accountId", "08999")
                                    .add("accountNumber", "08999")
                                    .add("accountType", "SAVINGS")
                                    .build())
                            .add("creditDetails", Json.createObjectBuilder()
                                    .add("instructionType", "single")
                                    .add("creditAccount", Json.createObjectBuilder()
                                            .add("accountSchemaType", "internal")
                                            .add("accountId", "078987654676")
                                            .add("accountNumber", "053472372372")
                                            .build()).build())
                            .add("nextExecution", Json.createObjectBuilder()
                                    .add("executionDate", "2021-12-03")
                                    .add("executionAmount", Json.createObjectBuilder()
                                            .add("amount", 8934)
                                            .add("currencyCode", "MXN")
                                            .build()))
                            .add("frequency", Json.createObjectBuilder()
                                    .add("frequencyType", "none").build())
                            .build())
                            .build())
                    .build();

            return Response.status(Response.Status.OK).entity(jprincipal).build();
        } catch (Exception e) {
            System.out.println("Error en metodo:" + e.getMessage());
        }
        return null;
    }

    @POST
    @Path("/monetary/validate")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response validateMonetaryInstruction(String cadena, @HeaderParam("authorization") String authString) throws JSONException {
        Security scr = new Security();
        if (!scr.isUserAuthenticated(authString)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        System.out.println("Cadena de monetary validate:" + cadena);
        JSONObject request = new JSONObject(cadena);
        String customerId = "", tipoTranferencia = "", cuentaOrigen = "", cuentaDestino = "", comentario = "", propCuenta = "", fechaEjecucion = "", tipoEjecucion = "";
        Double monto = 0.0;
         String value="";
        boolean bandera1 = false, bandera2 = false;
        try {
            if (request.getString("originatorTransactionType").toUpperCase().contains("BIL")) {
                System.out.println("entro aqui");
                customerId = request.getString("customerId");
                tipoTranferencia = request.getString("originatorTransactionType");
                cuentaOrigen = request.getString("debitAccountId");
                JSONObject credit = request.getJSONObject("creditAccount");
                cuentaDestino = credit.getString("billerCode");
                comentario = credit.getString("agreementCode");
                JSONObject billerFields = credit.getJSONObject("billerFields");
                JSONObject fieldTxt = billerFields.getJSONObject("01");
                value = fieldTxt.getString("value");
                JSONObject montoOP = request.getJSONObject("monetaryOptions");
                JSONObject montoR = montoOP.getJSONObject("amount");
                monto = montoR.getDouble("amount");
                JSONObject execution = montoOP.getJSONObject("execution");
                fechaEjecucion = execution.getString("executionDate");
                tipoEjecucion = execution.getString("executionType");
                bandera1 = true;
            } else {
                customerId = request.getString("customerId");
                tipoTranferencia = request.getString("originatorTransactionType");
                cuentaOrigen = request.getString("debitAccountId");
                JSONObject credit = request.getJSONObject("creditAccount");
                cuentaDestino = credit.getString("accountId");
                comentario = request.getString("debtorComments");
                JSONObject montoOP = request.getJSONObject("monetaryOptions");
                JSONObject montoR = montoOP.getJSONObject("amount");
                monto = montoR.getDouble("amount");
                JSONObject execution = montoOP.getJSONObject("execution");
                fechaEjecucion = execution.getString("executionDate");
                tipoEjecucion = execution.getString("executionType");
                bandera2 = true;
            }

            System.out.println("montoooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo:" + monto);
        } catch (Exception e) {
            JsonObject json = new JsonObject();
            json.put("Error", "Parametros desconocidos:" + e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
        TransfersDAO dao = new TransfersDAO();
        try {
            validateMonetaryInstructionDTO dto = null;
            if (bandera1) {
                System.out.println("siiiii");
                dto = dao.validateMonetaryInstruction(customerId, tipoTranferencia,
                        cuentaOrigen, cuentaDestino, monto, "Pago de servicios:"+comentario,"Codigo recibo:"+value,fechaEjecucion, tipoEjecucion);
            } else if (bandera2) {
                dto = dao.validateMonetaryInstruction(customerId, tipoTranferencia,
                        cuentaOrigen, cuentaDestino, monto, comentario,"",fechaEjecucion, tipoEjecucion);
            }
            javax.json.JsonObject jsonResponse = null;

            JsonObject jsi = new JsonObject();
            if (dto != null) {
                jsonResponse = Json.createObjectBuilder().add("validationId", dto.getValidationId())
                        .add("fees", Json.createArrayBuilder())
                        .add("executionDate", dto.getExecutionDate())
                        .build();
                return Response.status(Response.Status.OK).entity(jsonResponse).build();
            }
        } catch (Exception e) {
            System.out.println("error:" + e.getMessage());
        } finally {
            dao.cerrar();
        }
        return null;
    }

    @POST
    @Path("/monetary/execute")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response executeMonetaryInstruction(String cadena, @HeaderParam("authorization") String authString) throws JSONException {
        Security scr = new Security();
        if (!scr.isUserAuthenticated(authString)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
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
    @Path("/monetary/details")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response MonetaryDetails(String cadena, @HeaderParam("authorization") String authString) throws JSONException {
        Security scr = new Security();
        if (!scr.isUserAuthenticated(authString)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        JSONObject jrecibido = new JSONObject(cadena);
        try {
            String monetaryInstructionId = jrecibido.getString("monetaryInstructionId");
            javax.json.JsonObject jsonResponse = null;
            jsonResponse = Json.createObjectBuilder().add("details", Json.createObjectBuilder()
                    .add("monetaryInstructionId", "B67454MJ")
                    .add("originatorTransactionType", "TRANSFER_OWN")
                    .add("debitAccount", Json.createObjectBuilder()
                            .add("accountId:", "65HMNV78")
                            .add("accountNumber", "345VBHG7")
                            .add("accountSchemaType", "SAVINGS")
                            .build())
                    .add("creditAccount", Json.createObjectBuilder()
                            .add("accountId:", "65HMNV78")
                            .add("accountNumber", "345VBHG7")
                            .add("accountType", "SAVINGS")
                            .build())
                    .add("monetary", Json.createObjectBuilder()
                            .add("amount", Json.createObjectBuilder()
                                    .add("amount", 1150.56)
                                    .add("currencycode", "MXN")
                                    .build())
                            .add("execution", Json.createObjectBuilder()
                                    .add("executionType", "specific")
                                    .add("executionDate", "12-05-2021")
                                    .build())
                            .add("frecuency", Json.createObjectBuilder()
                                    .add("frecuencyType", "none")
                                    .build())
                            .add("fees", Json.createArrayBuilder())
                            .add("nextExecution", Json.createObjectBuilder()
                                    .add("executionDate", "04-05-2021")
                                    .add("executionAmount", Json.createObjectBuilder()
                                            .add("amount", 45000.00)
                                            .add("currencyCode", "MXN")
                                            .build())
                                    .build())
                            .build())
                    .build())
                    .build();

            return Response.status(Response.Status.OK).entity(jsonResponse).build();
        } catch (Exception e) {
            System.out.println("Error al obtener atributo json:" + e.getMessage());
        }
        return null;
    }

    @POST
    @Path("/monetary/instruction/history")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response MonetaryHistory(String cadena) {
        try {
            JSONObject jsonR = new JSONObject(cadena);
            String monetaryInstructionId = "";
            int page = 0, pageSize = 0;
            monetaryInstructionId = jsonR.getString("monetaryInstructionId");
            page = jsonR.getInt("page");
            pageSize = jsonR.getInt("pageSize");

            javax.json.JsonObject json = Json.createObjectBuilder().add("records", Json.createArrayBuilder().add(Json.createObjectBuilder()
                    .add("status", "pending")
                    .add("amount", Json.createObjectBuilder()
                            .add("amount", "10.0")
                            .add("currencyCode", "MXN")
                            .build())
                    .add("inputDate", "2021-05-12")
                    .add("executionDate", "2021-05-12")
                    .build()))
                    .build();

            return Response.status(Response.Status.OK).entity(json).build();

        } catch (JSONException ex) {
            System.out.println("Error al crear json:" + ex.getMessage());
        }
        return null;

    }

    @POST
    @Path("/instructions/monetary/cancel/validate")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response MonetaryCancellation(String cadena) {
        try {
            JSONObject jsonR = new JSONObject(cadena);
            String monetaryInstructionId = "";
            int page = 0, pageSize = 0;
            monetaryInstructionId = jsonR.getString("monetaryInstructionId");

            javax.json.JsonObject json = Json.createObjectBuilder().add("validationId", "340JKLWEDFGDGGDF")
                    .build();

            return Response.status(Response.Status.OK).entity(json).build();

        } catch (JSONException ex) {
            System.out.println("Error al crear json:" + ex.getMessage());
        }
        return null;

    }

    @POST
    @Path("/monetary/cancel/execute")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response MonetaryCancellationE(String cadena) {
        try {
            JSONObject jsonR = new JSONObject(cadena);
            String monetaryInstructionId = "";
            int page = 0, pageSize = 0;
            monetaryInstructionId = jsonR.getString("validationId");

            javax.json.JsonObject json = Json.createObjectBuilder().add("status", "completed")
                    .build();

            return Response.status(Response.Status.OK).entity(json).build();

        } catch (JSONException ex) {
            System.out.println("Error al crear json:" + ex.getMessage());
        }
        return null;

    }
}
