package com.fenoreste.rest.Entidades;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "validaciones_telefonos_siscoop")
public class validaciones_telefono_siscoop implements Serializable {
  private static final long serialVersionUID = 1L;
  
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sec_validaciones_siscoop")
  @SequenceGenerator(name = "sec_validaciones_siscoop", sequenceName = "sec_validaciones_siscoop")
  private int id;
  
  @Column(name = "validacion")
  private String validacion;
  
  @Column(name = "customerid")
  private String customerid;
  
  @Column(name = "settelefono")
  private String settelefono;
  
  public validaciones_telefono_siscoop() {}
  
  public validaciones_telefono_siscoop(int id, String validacion, String customerid, String settelefono) {
    this.id = id;
    this.validacion = validacion;
    this.customerid = customerid;
    this.settelefono = settelefono;
  }
  
  public int getId() {
    return this.id;
  }
  
  public void setId(int id) {
    this.id = id;
  }
  
  public String getValidacion() {
    return this.validacion;
  }
  
  public void setValidacion(String validacion) {
    this.validacion = validacion;
  }
  
  public String getCustomerid() {
    return this.customerid;
  }
  
  public void setCustomerid(String customerid) {
    this.customerid = customerid;
  }
  
  public String getSettelefono() {
    return this.settelefono;
  }
  
  public void setSettelefono(String settelefono) {
    this.settelefono = settelefono;
  }
  
  public String toString() {
    return "validaciones_telefono_siscoop{id=" + this.id + ", validacion=" + this.validacion + ", customerid=" + this.customerid + ", settelefono=" + this.settelefono + '}';
  }
}

