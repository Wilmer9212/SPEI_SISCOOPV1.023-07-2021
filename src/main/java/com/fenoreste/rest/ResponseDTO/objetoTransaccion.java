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
public class objetoTransaccion {
    
    private String identificadorUnicoSPEI;
    private String claveRastreo;
    private String conceptoPago;
    private String estado;
    private String fechaHoraOperacion;
    private Double monto;
    private Double iva;
    private Integer numeroReferencia;
    private String fechaHoraTransferencia;
    private String fechaHoraCaptura;
    private String fechaHoraAcuse;
    private String fechaHoraDevolucion;
    private String fechaHoraEntrega;
    private String fechaHoraLiquidacion;
    private Double comision;

    public objetoTransaccion() {
    }

    public objetoTransaccion(String identificadorUnicoSPEI, String claveRastreo, String conceptoPago, String estado, String fechaHoraOperacion, Double monto, Double iva, Integer numeroReferencia, String fechaHoraTransferencia, String fechaHoraCaptura, String fechaHoraAcuse, String fechaHoraDevolucion, String fechaHoraEntrega, String fechaHoraLiquidacion, Double comision) {
        this.identificadorUnicoSPEI = identificadorUnicoSPEI;
        this.claveRastreo = claveRastreo;
        this.conceptoPago = conceptoPago;
        this.estado = estado;
        this.fechaHoraOperacion = fechaHoraOperacion;
        this.monto = monto;
        this.iva = iva;
        this.numeroReferencia = numeroReferencia;
        this.fechaHoraTransferencia = fechaHoraTransferencia;
        this.fechaHoraCaptura = fechaHoraCaptura;
        this.fechaHoraAcuse = fechaHoraAcuse;
        this.fechaHoraDevolucion = fechaHoraDevolucion;
        this.fechaHoraEntrega = fechaHoraEntrega;
        this.fechaHoraLiquidacion = fechaHoraLiquidacion;
        this.comision = comision;
    }

    public String getIdentificadorUnicoSPEI() {
        return identificadorUnicoSPEI;
    }

    public void setIdentificadorUnicoSPEI(String identificadorUnicoSPEI) {
        this.identificadorUnicoSPEI = identificadorUnicoSPEI;
    }

    public String getClaveRastreo() {
        return claveRastreo;
    }

    public void setClaveRastreo(String claveRastreo) {
        this.claveRastreo = claveRastreo;
    }

    public String getConceptoPago() {
        return conceptoPago;
    }

    public void setConceptoPago(String conceptoPago) {
        this.conceptoPago = conceptoPago;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getFechaHoraOperacion() {
        return fechaHoraOperacion;
    }

    public void setFechaHoraOperacion(String fechaHoraOperacion) {
        this.fechaHoraOperacion = fechaHoraOperacion;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public Double getIva() {
        return iva;
    }

    public void setIva(Double iva) {
        this.iva = iva;
    }

    public Integer getNumeroReferencia() {
        return numeroReferencia;
    }

    public void setNumeroReferencia(Integer numeroReferencia) {
        this.numeroReferencia = numeroReferencia;
    }

    public String getFechaHoraTransferencia() {
        return fechaHoraTransferencia;
    }

    public void setFechaHoraTransferencia(String fechaHoraTransferencia) {
        this.fechaHoraTransferencia = fechaHoraTransferencia;
    }

    public String getFechaHoraCaptura() {
        return fechaHoraCaptura;
    }

    public void setFechaHoraCaptura(String fechaHoraCaptura) {
        this.fechaHoraCaptura = fechaHoraCaptura;
    }

    public String getFechaHoraAcuse() {
        return fechaHoraAcuse;
    }

    public void setFechaHoraAcuse(String fechaHoraAcuse) {
        this.fechaHoraAcuse = fechaHoraAcuse;
    }

    public String getFechaHoraDevolucion() {
        return fechaHoraDevolucion;
    }

    public void setFechaHoraDevolucion(String fechaHoraDevolucion) {
        this.fechaHoraDevolucion = fechaHoraDevolucion;
    }

    public String getFechaHoraEntrega() {
        return fechaHoraEntrega;
    }

    public void setFechaHoraEntrega(String fechaHoraEntrega) {
        this.fechaHoraEntrega = fechaHoraEntrega;
    }

    public String getFechaHoraLiquidacion() {
        return fechaHoraLiquidacion;
    }

    public void setFechaHoraLiquidacion(String fechaHoraLiquidacion) {
        this.fechaHoraLiquidacion = fechaHoraLiquidacion;
    }

    public Double getComision() {
        return comision;
    }

    public void setComision(Double comision) {
        this.comision = comision;
    }

    @Override
    public String toString() {
        return "objetoTransaccion{" + "identificadorUnicoSPEI=" + identificadorUnicoSPEI + ", claveRastreo=" + claveRastreo + ", conceptoPago=" + conceptoPago + ", estado=" + estado + ", fechaHoraOperacion=" + fechaHoraOperacion + ", monto=" + monto + ", iva=" + iva + ", numeroReferencia=" + numeroReferencia + ", fechaHoraTransferencia=" + fechaHoraTransferencia + ", fechaHoraCaptura=" + fechaHoraCaptura + ", fechaHoraAcuse=" + fechaHoraAcuse + ", fechaHoraDevolucion=" + fechaHoraDevolucion + ", fechaHoraEntrega=" + fechaHoraEntrega + ", fechaHoraLiquidacion=" + fechaHoraLiquidacion + ", comision=" + comision + '}';
    }
    
    
}