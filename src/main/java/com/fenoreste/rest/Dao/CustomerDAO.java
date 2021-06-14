package com.fenoreste.rest.Dao;

import com.fenoreste.rest.Entidades.Persona;

public class CustomerDAO extends FacadeCustomer<Persona> {

	 public CustomerDAO() {
	     super(Persona.class);
  }     
}
