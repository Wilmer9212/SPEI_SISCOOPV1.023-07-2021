/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fenoreste.rest.dao;

import com.fenoreste.rest.ResponseDTO.AccountHoldersDTO;
import com.fenoreste.rest.ResponseDTO.DetailsAccountDTO;
import com.fenoreste.rest.ResponseDTO.HoldsDTO;
import com.fenoreste.rest.ResponseDTO.StatementsDTO;
import com.fenoreste.rest.Util.AbstractFacade;
import com.fenoreste.rest.entidades.Auxiliares;
import com.fenoreste.rest.entidades.AuxiliaresD;
import com.fenoreste.rest.entidades.AuxiliaresPK;
import com.fenoreste.rest.entidades.Persona;
import com.fenoreste.rest.entidades.PersonasPK;
import com.fenoreste.rest.entidades.Productos;
import com.fenoreste.rest.entidades.ReferenciasP;
import com.fenoreste.rest.entidades.V_auxiliares;
import com.fenoreste.rest.entidades.tipos_cuenta_siscoop;
import com.fenoreste.rest.entidades.transferencias_completadas_siscoop;
import com.fenoreste.rest.entidades.v_auxiliaresPK;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

/**
 *
 * @author Elliot
 */
public abstract class FacadeAccounts<T> {

    private static EntityManagerFactory emf;

    List<Object[]> lista = null;

    public FacadeAccounts(Class<T> entityClass) {
        emf = AbstractFacade.conexion();
    }

    public List<AccountHoldersDTO> validateInternalAccount(String accountId) {
        EntityManager em = emf.createEntityManager();
        int o = Integer.parseInt(accountId.substring(0, 6));
        int p = Integer.parseInt(accountId.substring(6, 11));
        int a = Integer.parseInt(accountId.substring(11, 19));

        List<AccountHoldersDTO> holders = new ArrayList<AccountHoldersDTO>();
        try{
        AuxiliaresPK auxpk = new AuxiliaresPK(o, p, a);
        Auxiliares aa = em.find(Auxiliares.class, auxpk);
        PersonasPK personaspk = new PersonasPK(aa.getIdorigen(),aa.getIdgrupo(),aa.getIdsocio());
        Persona person = em.find(Persona.class, personaspk);
        String persona = person.getNombre() + " " + person.getAppaterno() + " " + person.getApmaterno();
        AccountHoldersDTO dto = new AccountHoldersDTO(persona, "SOW");
        holders.add(dto);
    }catch (Exception e) {
            System.out.println("Error al crear lista:" + e.getMessage());
    }
    System.out.println("Holders:"+holders);
    return holders;
}
    
  public List<String>validateBeneficiary(String accountId,String accountType,String name,String address) {
        EntityManager em = emf.createEntityManager();
        int o = Integer.parseInt(accountId.substring(0, 6));
        int p = Integer.parseInt(accountId.substring(6, 11));
        int a = Integer.parseInt(accountId.substring(11, 19));
        List<String>lista=new ArrayList<String>();
        try{
        AuxiliaresPK auxpk = new AuxiliaresPK(o, p, a);
        Auxiliares aa = em.find(Auxiliares.class, auxpk);
        PersonasPK personaspk = new PersonasPK(aa.getIdorigen(),aa.getIdgrupo(),aa.getIdsocio());
        Persona person = em.find(Persona.class, personaspk);
        String persona = person.getNombre() + " " + person.getAppaterno() + " " + person.getApmaterno();
        tipos_cuenta_siscoop tps=em.find(tipos_cuenta_siscoop.class,aa.getAuxiliaresPK().getIdproducto());
        if(persona.replace(" ","").contains(name.replace(" ","")) && tps.getProducttypename().replace(" ","").toUpperCase().contains(accountType.replace(" ",""))){
         lista.add(persona.toUpperCase());
         lista.add(tps.getProducttypename().toUpperCase());
         lista.add(accountId);
        }else{
            System.out.println("no es");
        }       
       
    }catch (Exception e) {
            System.out.println("Error al crear lista:" + e.getMessage());
    }
    return lista;
}
    
public List<StatementsDTO>statements(String accountId,String initialDate,String finalDate,int pageSize,int pageStartIndex){
    EntityManager em=emf.createEntityManager();
    List<StatementsDTO>lista=new ArrayList<StatementsDTO>();
    try{
        String consulta="SELECT * FROM auxiliares_d WHERE"
                + " replace(to_char(idorigenp,'099999')||to_char(idproducto,'09999')||to_char(idauxiliar,'09999999'),' ','')='"+accountId
                +"' AND date(fecha) BETWEEN '"+initialDate+"' AND '"+finalDate+"'";
        System.out.println("Consulta:"+consulta);
        Query query=em.createNativeQuery(consulta,AuxiliaresD.class);
        query.setFirstResult(pageStartIndex);
        query.setMaxResults(pageSize);
        List<AuxiliaresD>listaa=query.getResultList();
        for(int i=0;i<listaa.size();i++){
            AuxiliaresD a=listaa.get(i);
            System.out.println("a:"+a);
            StatementsDTO dto=new StatementsDTO(
                    accountId,
                    initialDate,
                    finalDate,
                    String.valueOf(a.getAuxiliaresDPK().getIdorigenp()+a.getAuxiliaresDPK().getIdproducto()+a.getAuxiliaresDPK().getIdauxiliar()),
                    String.valueOf(a.getIdorigenc()+a.getIdtipo()+a.getIdpoliza()+a.getAuxiliaresDPK().getIdproducto()+a.getAuxiliaresDPK().getIdauxiliar()));    
        lista.add(dto);
        }
        System.out.println("lista:"+lista);
          
    }catch(Exception e){
        System.out.println("Error:"+e.getMessage());
                
    }
    System.out.println("count:"+lista.size());
    return lista;
    
}

public List<HoldsDTO>holds(String accountId){
    EntityManager em=emf.createEntityManager();
    List<HoldsDTO>listaDTO=new ArrayList<HoldsDTO>();
    try {
      String consulta="SELECT * FROM auxiliares a WHERE"
                + " replace(to_char(a.idorigenp,'099999')||to_char(a.idproducto,'09999')||to_char(idauxiliar,'09999999'),' ','')='"+accountId
                + "' AND estatus=2 AND garantia > 0";
      System.out.println("consulta:"+consulta);
      
      Query query=em.createNativeQuery(consulta,Auxiliares.class);
      
      List<Auxiliares>lista=query.getResultList();
        System.out.println("listaSize:"+lista.size());
      for(int i=0;i<lista.size();i++){
          System.out.println("ne");
          Auxiliares a=lista.get(i);
          try {
              String consulta2="SELECT * FROM referenciasp WHERE idorigenpr="+a.getAuxiliaresPK().getIdorigenp()+" AND idproductor="+a.getAuxiliaresPK().getIdproducto()+" AND idauxiliarr="+a.getAuxiliaresPK().getIdauxiliar();
              System.out.println("consulta2:"+consulta2);
              Query query1=em.createNativeQuery(consulta2);
              List<Object[]>listarf=query1.getResultList();
              String fbloqueo="";
              Productos pr=em.find(Productos.class,a.getAuxiliaresPK().getIdproducto());
              System.out.println("io");
              System.out.println("lista:"+listarf);
              for(Object[] obj:listarf){
                  v_auxiliaresPK auxpk=new v_auxiliaresPK(Integer.parseInt(obj[0].toString()),Integer.parseInt(obj[1].toString()),Integer.parseInt(obj[2].toString()));                  
                  V_auxiliares aa=em.find(V_auxiliares.class,auxpk);
                  System.out.println("aaa:"+aa.getAuxiliaresPK().getIdproducto());
                  if(aa.getEstatus()<=2){
                  if(aa.getEstatus()==2){
                      fbloqueo=String.valueOf(aa.getFechaactivacion());
                  }else if(aa.getEstatus()<2){
                      fbloqueo=String.valueOf(aa.getFechaape());
                  }
                  HoldsDTO dto=new HoldsDTO(accountId, Double.parseDouble(a.getGarantia().toString()),fbloqueo,pr.getNombre());
                  System.out.println("DTO:"+dto);
                  listaDTO.add(dto);
              }}
          } catch (Exception e) {
              System.out.println("Error:"+e.getMessage());
          }
          
          
      }
          } catch (Exception e) {
        System.out.println("Error en holds:"+e.getMessage());
    }
    return listaDTO;
}

public List<transferencias_completadas_siscoop>History(String accountId,String initialDate,String finalDate,int pageSize,int pageStartIndex){
    EntityManager em=emf.createEntityManager();
    List<transferencias_completadas_siscoop>lista=null;
    try {
        String consulta="SELECT * FROM e_transferenciassiscoop WHERE "
        + "cuentaorigen='"+accountId.trim()
        + "' or cuentadestino='"+accountId.trim()
        + "' AND fechaejecucion BETWEEN '"+initialDate.trim()+"' AND '"+finalDate.trim()+"'";
        System.out.println("Consulta:"+consulta);
        Query query=em.createNativeQuery(consulta,transferencias_completadas_siscoop.class);
        query.setFirstResult(pageStartIndex);
        query.setMaxResults(pageSize);
        lista=query.getResultList();
        
    } catch (Exception e) {
        System.out.println("Error al leer transacciones:"+e.getMessage());
    }
    return lista;
    
}

public DetailsAccountDTO detailsAccount(String accountId){
    EntityManager em=emf.createEntityManager();
    DetailsAccountDTO dto=null;
    try {
      String consulta="SELECT * FROM auxiliares a WHERE replace(to_char(a.idorigenp,'099999')||to_char(a.idproducto,'09999')||to_char(a.idauxiliar,'09999999'),' ','')='"+accountId.trim()+"'";
        System.out.println("Consulta:"+consulta);
      Query query=em.createNativeQuery(consulta,Auxiliares.class);
      Auxiliares a=(Auxiliares)query.getSingleResult();
      System.out.println("a:"+a);
      tipos_cuenta_siscoop tps=em.find(tipos_cuenta_siscoop.class, a.getAuxiliaresPK().getIdproducto());
        System.out.println("tps:"+tps);
      String e="";
      if(a.getEstatus()==0){
          e="INACTIVE";
      }else if(a.getEstatus()==1){
          e="DORMANT";
      }else if(a.getEstatus()==2){
          e="ACTIVE";
      }else if(a.getEstatus()==3){
          e="CLOSED";
      }
      Query query1=em.createNativeQuery("SELECT nombre FROM origenes WHERE idorigen="+a.getAuxiliaresPK().getIdorigenp());
      String sucursal=(String)query1.getSingleResult();
        System.out.println("Sucursal:"+sucursal);
      dto=new DetailsAccountDTO(
                               accountId,
                               String.valueOf(a.getAuxiliaresPK().getIdorigenp())+""+String.valueOf(a.getAuxiliaresPK().getIdproducto())+""+String.valueOf(a.getAuxiliaresPK().getIdauxiliar()),
                               String.valueOf(a.getAuxiliaresPK().getIdorigenp())+""+String.valueOf(a.getAuxiliaresPK().getIdproducto())+""+String.valueOf(a.getAuxiliaresPK().getIdauxiliar()),
                               tps.getProducttypename().toUpperCase(),
                               "MXN",
                               String.valueOf(a.getAuxiliaresPK().getIdproducto()),
                               e,
                               sucursal,
                               String.valueOf(a.getFechaactivacion()));
    } catch (Exception e) {
        System.out.println("Error en buscar detalles de cuenta:"+e.getMessage());
    }
    return dto;
}

public String Holders(String accountId){
    EntityManager em=emf.createEntityManager();
    String nombre="";
    try {
        String consulta="SELECT * FROM auxiliares a WHERE replace(to_char(a.idorigenp,'099999')||to_char(a.idproducto,'09999')||to_char(a.idauxiliar,'09999999'),' ','')='"+accountId.trim()+"'";
        Query query=em.createNativeQuery(consulta,Auxiliares.class);
        Auxiliares a=(Auxiliares)query.getSingleResult();
        PersonasPK pk=new PersonasPK(a.getIdorigen(),a.getIdgrupo(),a.getIdsocio());
        Persona p=em.find(Persona.class,pk);
        System.out.println("p:"+p);
        nombre=p.getNombre()+" "+p.getAppaterno()+" "+p.getApmaterno();
    } catch (Exception e) {
        System.out.println("Error al buscar propietario de la cuenta:"+e.getMessage());
    }
    return nombre;
}
 public Date stringToDate(String cadena) {
        SimpleDateFormat formato = new SimpleDateFormat("yyyy/MM/dd");
        Date fechaDate = null;

        try {
            fechaDate = formato.parse(cadena);
        } catch (Exception ex) {
            System.out.println("Error fecha:" + ex.getMessage());
        }
        System.out.println("fechaDate:" + fechaDate);
        return fechaDate;
    }
public String accountType(int idproducto){
    EntityManager em=emf.createEntityManager();
    tipos_cuenta_siscoop tip=null;
    try {
        tip=em.find(tipos_cuenta_siscoop.class,idproducto);        
    } catch (Exception e) {
        System.out.println("Error al buscar tipo cuenta siscoop:"+e.getMessage());
    }
    return tip.getProducttypename();
}

public void cerrar(){
    emf.close();
}
}
