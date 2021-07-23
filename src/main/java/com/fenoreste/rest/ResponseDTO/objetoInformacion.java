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
public class objetoInformacion {
    
    private objetoSolicitante solicitante;
    private objetoBeneficiario beneficiario;
    private objetoTransaccion transaccion;
    private String URLConsultaCEP;

    public objetoInformacion() {
    }

    public objetoInformacion(objetoSolicitante solicitante, objetoBeneficiario beneficiario, objetoTransaccion transaccion, String URLConsultaCEP) {
        this.solicitante = solicitante;
        this.beneficiario = beneficiario;
        this.transaccion = transaccion;
        this.URLConsultaCEP = URLConsultaCEP;
    }

    public objetoSolicitante getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(objetoSolicitante solicitante) {
        this.solicitante = solicitante;
    }

    public objetoBeneficiario getBeneficiario() {
        return beneficiario;
    }

    public void setBeneficiario(objetoBeneficiario beneficiario) {
        this.beneficiario = beneficiario;
    }

    public objetoTransaccion getTransaccion() {
        return transaccion;
    }

    public void setTransaccion(objetoTransaccion transaccion) {
        this.transaccion = transaccion;
    }

    public String getURLConsultaCEP() {
        return URLConsultaCEP;
    }

    public void setURLConsultaCEP(String URLConsultaCEP) {
        this.URLConsultaCEP = URLConsultaCEP;
    }

    @Override
    public String toString() {
        return "objetoInformacion{" + "solicitante=" + solicitante + ", beneficiario=" + beneficiario + ", transaccion=" + transaccion + ", URLConsultaCEP=" + URLConsultaCEP + '}';
    }
    
    
}
