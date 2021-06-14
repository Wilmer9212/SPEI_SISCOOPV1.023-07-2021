/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fenoreste.rest.Entidades;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * @author Elliot
 */
@Entity
@Table(name = "v_alertas")
public class v_Alertas implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sec_validaciones_tsiscoop")
    @SequenceGenerator(name = "sec_validaciones_tsiscoop", sequenceName = "sec_validaciones_tsiscoop")
    private Integer id;
    @Column(name="code")
    private String code;
    @Column(name="enabled")
    private boolean enabled;
    @Column(name="customerid")
    private String customerid;
    @Column(name="fechaejecucion")
    private Date fechaejecucion;
    @Column(name="validationid")
    private String validationid;
    
    public v_Alertas() {
        
    }

    public v_Alertas(Integer id, String code, boolean enabled, String customerid, Date fechaejecucion, String validattionid) {
        this.id = id;
        this.code = code;
        this.enabled = enabled;
        this.customerid = customerid;
        this.fechaejecucion = fechaejecucion;
        this.validationid = validattionid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getCustomerid() {
        return customerid;
    }

    public void setCustomerid(String customerid) {
        this.customerid = customerid;
    }

    public Date getFechaejecucion() {
        return fechaejecucion;
    }

    public void setFechaejecucion(Date fechaejecucion) {
        this.fechaejecucion = fechaejecucion;
    }

    public String getValidattionid() {
        return validationid;
    }

    public void setValidattionid(String validattionid) {
        this.validationid = validattionid;
    }

    @Override
    public String toString() {
        return "v_Alertas{" + "id=" + id + ", text=" + code + ", enabled=" + enabled + ", customerid=" + customerid + ", fechaejecucion=" + fechaejecucion + ", validattionid=" + validationid + '}';
    }
     
    
}