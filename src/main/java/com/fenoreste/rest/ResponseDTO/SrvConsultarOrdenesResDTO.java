/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fenoreste.rest.ResponseDTO;

/**
 *
 * @author wilmer
 */
public class SrvConsultarOrdenesResDTO {
    
    private objetoInformacion informacion;
    private Integer estatusProceso;
    private String idTransaccion;
    private String mensaje;

    public SrvConsultarOrdenesResDTO() {
    }

    public SrvConsultarOrdenesResDTO(objetoInformacion informacion, Integer estatusProceso, String idTransaccion, String mensaje) {
        this.informacion = informacion;
        this.estatusProceso = estatusProceso;
        this.idTransaccion = idTransaccion;
        this.mensaje = mensaje;
    }

    public objetoInformacion getInformacion() {
        return informacion;
    }

    public void setInformacion(objetoInformacion informacion) {
        this.informacion = informacion;
    }

    public Integer getEstatusProceso() {
        return estatusProceso;
    }

    public void setEstatusProceso(Integer estatusProceso) {
        this.estatusProceso = estatusProceso;
    }

    public String getIdTransaccion() {
        return idTransaccion;
    }

    public void setIdTransaccion(String idTransaccion) {
        this.idTransaccion = idTransaccion;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    @Override
    public String toString() {
        return "SrvConsultarOrdenesResDTO{" + "informacion=" + informacion + ", estatusProceso=" + estatusProceso + ", idTransaccion=" + idTransaccion + ", mensaje=" + mensaje + '}';
    }

    
}


