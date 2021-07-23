/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fenoreste.spei.services;

import com.fenoreste.rest.Auth.Security;
import com.fenoreste.rest.ResponseDTO.SrvConsultarOrdenesResDTO;
import com.fenoreste.rest.ResponseDTO.objetoBeneficiario;
import com.fenoreste.rest.ResponseDTO.objetoInformacion;
import com.fenoreste.rest.ResponseDTO.objetoSolicitante;
import com.fenoreste.rest.ResponseDTO.objetoTransaccion;
import java.text.NumberFormat;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author wilmer
 */
@Path("/spei")
public class SPEIResources {

    @Path("/srvConsultPet")
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response test(String cadena, @HeaderParam("authorization") String authString) {

        Security sc = new Security();
        if (!sc.isUserAuthenticated(authString)) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Credenciales incorrectas").build();
        }
        int intNumber = 25;
        float floatNumber = 25.546f;
        NumberFormat format = NumberFormat.getInstance();
        format.setMaximumIntegerDigits(6);
        //format.setMaximumFractionDigits(6);
        //format.setMinimumFractionDigits(6);
        format.setMinimumIntegerDigits(6);

        //System.out.println("Formatted Integer : " + format.format(intNumber).replace(",", ""));
        //System.out.println("Formatted Float   : " + format.format(floatNumber).replace(",", ""));
        System.out.println("aui");
        
        SrvConsultarOrdenesResDTO response = new SrvConsultarOrdenesResDTO();
        objetoSolicitante solicitanteDTO=new objetoSolicitante();
        solicitanteDTO.setCif(1234567890);
        solicitanteDTO.setCorreoElectronico("gmail.com");
        solicitanteDTO.setCuentaTarjeta("********3245");
        solicitanteDTO.setNombre("Mario Robelli Fuentes");
        solicitanteDTO.setRfcCurp("FURM7801205HCSTML05");
        solicitanteDTO.setTipoCuenta(2);
        
        objetoBeneficiario beneficiarioDTO=new objetoBeneficiario();
        beneficiarioDTO.setNombre("Jesus Andres Garcia Castañeda");
        beneficiarioDTO.setCorreoElectronico("j54@gmail.com");
        beneficiarioDTO.setCuentaTarjeta("*****5683");
        beneficiarioDTO.setRfcCurp("GACJ923006HCSTML01");
        beneficiarioDTO.setTipoCuenta(2);
        beneficiarioDTO.setInstitucionContraparte(1);
        
        objetoTransaccion transaccionDTO=new objetoTransaccion();
        transaccionDTO.setClaveRastreo(authString);
        transaccionDTO.setComision(10.2);
        transaccionDTO.setConceptoPago("SPEI TRANSFER");
        transaccionDTO.setEstado("PROCESO");
        transaccionDTO.setFechaHoraAcuse("2021-07-06T22:44:24.845Z");
        transaccionDTO.setFechaHoraCaptura("2021-07-06T22:44:24.845Z");
        transaccionDTO.setFechaHoraDevolucion("2021-07-06T22:44:24.845Z");
        transaccionDTO.setFechaHoraEntrega("2021-07-06T22:44:24.845Z");
        transaccionDTO.setFechaHoraLiquidacion("2021-07-06T22:44:24.845Z");
        transaccionDTO.setFechaHoraOperacion("2021-07-06T22:44:24.845Z");
        transaccionDTO.setFechaHoraTransferencia("2021-07-06T22:44:24.845Z");
        transaccionDTO.setIdentificadorUnicoSPEI("90567457745555");
        transaccionDTO.setIva(0.0);
        transaccionDTO.setMonto(0.0);
        transaccionDTO.setNumeroReferencia(568006789);
        
        objetoInformacion informacionDTO=new objetoInformacion();
        informacionDTO.setBeneficiario(beneficiarioDTO);
        informacionDTO.setSolicitante(solicitanteDTO);
        informacionDTO.setTransaccion(transaccionDTO);
        informacionDTO.setURLConsultaCEP("tu url");
        
        response.setEstatusProceso(0);
        response.setIdTransaccion("677855656");
        response.setInformacion(informacionDTO);
        response.setMensaje("Exitoso");
        
        
        
        System.out.println("respons:"+response);

        return Response.status(Response.Status.OK).entity(response).build();
    }
}
