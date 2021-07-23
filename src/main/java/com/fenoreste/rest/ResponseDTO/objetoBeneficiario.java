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
public class objetoBeneficiario {
     
    private String cuentaTarjeta;
    private String nombre;
    private String rfcCurp;
    private Integer institucionContraparte;
    private Integer tipoCuenta;
    private String correoElectronico;

    public objetoBeneficiario() {
    }

    public objetoBeneficiario(String cuentaTarjeta, String nombre, String rfcCurp, Integer institucionContraparte, Integer tipoCuenta, String correoElectronico) {
        this.cuentaTarjeta = cuentaTarjeta;
        this.nombre = nombre;
        this.rfcCurp = rfcCurp;
        this.institucionContraparte = institucionContraparte;
        this.tipoCuenta = tipoCuenta;
        this.correoElectronico = correoElectronico;
    }

    public String getCuentaTarjeta() {
        return cuentaTarjeta;
    }

    public void setCuentaTarjeta(String cuentaTarjeta) {
        this.cuentaTarjeta = cuentaTarjeta;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRfcCurp() {
        return rfcCurp;
    }

    public void setRfcCurp(String rfcCurp) {
        this.rfcCurp = rfcCurp;
    }

    public Integer getInstitucionContraparte() {
        return institucionContraparte;
    }

    public void setInstitucionContraparte(Integer institucionContraparte) {
        this.institucionContraparte = institucionContraparte;
    }

    public Integer getTipoCuenta() {
        return tipoCuenta;
    }

    public void setTipoCuenta(Integer tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    @Override
    public String toString() {
        return "objetoBeneficiario{" + "cuentaTarjeta=" + cuentaTarjeta + ", nombre=" + nombre + ", rfcCurp=" + rfcCurp + ", institucionContraparte=" + institucionContraparte + ", tipoCuenta=" + tipoCuenta + ", correoElectronico=" + correoElectronico + '}';
    }

    
}
