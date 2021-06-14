package com.fenoreste.rest.services;

import com.fenoreste.rest.Auth.Security;
import com.fenoreste.rest.ResponseDTO.ClearingCodeRulesDTO;
import com.fenoreste.rest.ResponseDTO.CountriesDTO;
import com.fenoreste.rest.Util.AbstractFacade;
import com.github.cliftonlabs.json_simple.JsonObject;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Selection;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import com.fenoreste.rest.Entidades.*;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.core.MediaType;

@Path("/api")
public class CountriesResources {
    
  @GET
  @Path("/countries")
  @Produces({MediaType.APPLICATION_JSON})
  @Consumes({MediaType.APPLICATION_JSON})
  public Response getCountries( @HeaderParam("authorization") String authString) {
    EntityManagerFactory emf = AbstractFacade.conexion();
    JsonObject json = new JsonObject();
    JsonObject jsonInfo = new JsonObject();
      System.out.println("autString:"+authString);
    Security scr=new Security();
     if(!scr.isUserAuthenticated(authString)){
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    try {
      EntityManager em = emf.createEntityManager();
      CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
      cq.select((Selection)cq.from(Paises_Siscoop.class));
      List<Paises_Siscoop> lista = em.createQuery(cq).getResultList();
      List<CountriesDTO> ListaReal = new ArrayList<CountriesDTO>();
      System.out.println("Lista:" + lista);
      for (int x = 0; x < lista.size(); x++) {
      Paises_Siscoop ps=lista.get(x);
      if(ps.getName().toUpperCase().contains("MEXI")){
          System.out.println("si");
      CountriesDTO dto = new CountriesDTO(ps.getCode(), ps.getName());
      ListaReal.add(dto);
      }
      json.put("countries",ListaReal);
      }
      return Response.status(Response.Status.OK).entity(json).build();
    } catch (Exception e) {
      System.out.println("Error en buscar paises:" + e.getMessage());
      jsonInfo.put("Error", "Datos no encontrados");
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsonInfo).build();
    } finally {
      emf.close();
    } 
  }
  
  @GET
  @Path("/clearingcoderules")
  @Produces({MediaType.APPLICATION_JSON})
  @Consumes({MediaType.APPLICATION_JSON})
  public Response getClearingCodeRules(@HeaderParam("authorization") String authString) {
    EntityManagerFactory emf = AbstractFacade.conexion();
    JsonObject json = new JsonObject();
    JsonObject jsonInfo = new JsonObject();
    Security scr=new Security();
     if(!scr.isUserAuthenticated(authString)){
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    try {
      EntityManager em = emf.createEntityManager();
      CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
      cq.select((Selection)cq.from(Paises_Siscoop.class));
      List<Paises_Siscoop> lista = em.createQuery(cq).getResultList();
      List<ClearingCodeRulesDTO> ListaReal = new ArrayList<ClearingCodeRulesDTO>();
      for (int x = 0; x < lista.size(); x++) {
        Paises_Siscoop sc=lista.get(x);
        if(sc.getName().toUpperCase().contains("MEX")){
            ClearingCodeRulesDTO dto = new ClearingCodeRulesDTO(sc.getCode().toUpperCase(), "numeric");
            ListaReal.add(dto);
        }
        
      } 
      json.put("clearingCodeRules", ListaReal);
      return Response.status(Response.Status.OK).entity(json).build();
    } catch (Exception e) {
      System.out.println("Error en ClearingCodeRules:" + e.getMessage());
      jsonInfo.put("Error", "Datos no encontrados");
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsonInfo).build();
    } finally {
      emf.close();
    } 
  }
}
