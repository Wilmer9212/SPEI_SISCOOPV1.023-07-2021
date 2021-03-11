/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fenoreste.rest.entidades;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author wilmer
 */
@Entity
@Table(name = "tipos_cuenta_siscoop")
public class CuentasSiscoop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idproducto")
    private Integer idproducto;
    @Column(name="producttypeid")
    private Integer producttypeid;
    @Column(name="producttypename")
    private String producttypename;
    @Column(name="descripcion")
    private String descripcion;

    public CuentasSiscoop() {
    }

    public CuentasSiscoop(Integer idproducto, Integer producttypeid, String producttypename, String descripcion) {
        this.idproducto = idproducto;
        this.producttypeid = producttypeid;
        this.producttypename = producttypename;
        this.descripcion = descripcion;
    }

    public Integer getIdproducto() {
        return idproducto;
    }

    public void setIdproducto(Integer idproducto) {
        this.idproducto = idproducto;
    }

    public Integer getProducttypeid() {
        return producttypeid;
    }

    public void setProducttypeid(Integer producttypeid) {
        this.producttypeid = producttypeid;
    }

    public String getProducttypename() {
        return producttypename;
    }

    public void setProducttypename(String producttypename) {
        this.producttypename = producttypename;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return "CuentasSiscoop{" + "idproducto=" + idproducto + ", producttypeid=" + producttypeid + ", producttypename=" + producttypename + ", descripcion=" + descripcion + '}';
    }
    
    
}
