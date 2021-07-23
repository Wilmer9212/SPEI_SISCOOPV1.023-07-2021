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
public class objetoSolicitante {
     
    private Integer cif;
    private String cuentaTarjeta;
    private String nombre;
    private String rfcCurp;
    private Integer tipoCuenta;
    private String correoElectronico;
    public objetoSolicitante() {
    }

    public objetoSolicitante(Integer cif, String cuentaTarjeta, String nombre, String rfcCurp, Integer tipoCuenta, String correoElectronico) {
        this.cif = cif;
        this.cuentaTarjeta = cuentaTarjeta;
        this.nombre = nombre;
        this.rfcCurp = rfcCurp;
        this.tipoCuenta = tipoCuenta;
        this.correoElectronico = correoElectronico;
    }

    public Integer getCif() {
        return cif;
    }

    public void setCif(Integer cif) {
        this.cif = cif;
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
        return "objetoSolicitante{" + "cif=" + cif + ", cuentaTarjeta=" + cuentaTarjeta + ", nombre=" + nombre + ", rfcCurp=" + rfcCurp + ", tipoCuenta=" + tipoCuenta + ", correoElectronico_=" + correoElectronico + '}';
    }

   
    
}
