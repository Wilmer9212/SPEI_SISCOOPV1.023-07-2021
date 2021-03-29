/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fenoreste.rest.dao;

import com.fenoreste.rest.Util.AbstractFacade;
import com.fenoreste.rest.entidades.Auxiliares;
import com.fenoreste.rest.entidades.Productos;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

/**
 *
 * @author Elliot
 */
public abstract class FacadeBalances<T> {
    private static EntityManagerFactory emf;
    List<Object[]> lista = null;

    public FacadeBalances(Class<T> entityClass) {
        emf = AbstractFacade.conexion();
    }
    
    public Double[]balances(List<String> accountId) {
        EntityManager em = emf.createEntityManager();
        Double ledGer=0.0,avalaible=0.0;
        System.out.println("ListaAccountId:"+accountId);
        try {
            for(int i=0;i<accountId.size();i++){
             String aci=accountId.get(i);
             String c = "SELECT * FROM auxiliares a"
                    + " INNER JOIN productos pr USING(idproducto)"
                    + " WHERE pr.tipoproducto in(0,1) AND replace(to_char(a.idorigenp,'099999')||"
                    + "to_char(a.idproducto,'09999')||to_char(a.idauxiliar,'09999999'),' ','')='" + aci+ "'";
              
            
            System.out.println("Consulta:"+c);
            Query query = em.createNativeQuery(c, Auxiliares.class);
            List<Auxiliares> lista = query.getResultList();
            Double saldoAhorro = 0.0;
            Double saldoDPF=0.0;
            
            Double saldoGarantia=0.0;
            
            for (int ii = 0; ii < lista.size(); ii++) {
                Auxiliares a = lista.get(ii);
                Productos pr = em.find(Productos.class, a.getAuxiliaresPK().getIdproducto());
                Double garantiaDPF=0.0;
                Double saldoAvalaibleDPF=0.0;
                Double saldoLedgerDPF=0.0;
                Double garantiaAhorro=0.0;
                Double saldoAvalaibleAhorro=0.0;
                Double saldoLedgerAhorro=0.0;
                if (pr.getTipoproducto() == 1) {
                    //Se suma fechaactivacion mas plazos para determinar si el producto ya se puede cobrar o aun no
                    String cc = "SELECT a.fechaactivacion + " + Integer.parseInt(String.valueOf(a.getPlazo())) + " FROM auxiliares a WHERE a.idorigenp="
                            + a.getAuxiliaresPK().getIdorigenp()
                            + " AND a.idproducto=" + a.getAuxiliaresPK().getIdproducto()
                            + " AND a.idauxiliar=" + a.getAuxiliaresPK().getIdauxiliar();
                    Query query1 = em.createNativeQuery(cc);
                    String fecha = String.valueOf(query1.getSingleResult()).replace("-", "/");
                    Date hoy = new Date();
                    Date fa = stringToDate(fecha);//fecha obtenida
                    //si la fecha obtenida es igual al dia actual(hoy) o esta antes el dpf ya se puede retirar siempre y cuando no este amparando credito
                 saldoLedgerDPF=saldoLedgerDPF+Double.parseDouble(a.getSaldo().toString());
                    if (fa == hoy || fa.before(hoy)){
                        //si el dpf tiene amparado credito
                        if(Integer.parseInt(a.getGarantia().toString())>0){
                          garantiaDPF=garantiaDPF+Double.parseDouble(a.getGarantia().toString());                        
                          saldoAvalaibleDPF=saldoAvalaibleDPF+(Double.parseDouble(a.getSaldo().toString())-garantiaDPF);  
                        }else{
                          saldoAvalaibleDPF=Double.parseDouble(a.getSaldo().toString());
                        }       
                 //Si el dpf aun no se puede retirar
                }
                  
                }else if(pr.getTipoproducto()==0){
                saldoLedgerAhorro=saldoLedgerAhorro+Double.parseDouble(a.getSaldo().toString());
                if(Double.parseDouble(a.getGarantia().toString())>0){                        
                 garantiaAhorro= garantiaAhorro+Double.parseDouble(a.getGarantia().toString());
                 saldoAvalaibleAhorro=saldoAvalaibleAhorro+ (Double.parseDouble(a.getSaldo().toString())-garantiaAhorro);
                }else{
                 saldoAvalaibleAhorro=saldoAvalaibleAhorro+Double.parseDouble(a.getSaldo().toString());
                }
              }
              ledGer=ledGer+(saldoLedgerDPF+saldoLedgerAhorro);
              avalaible=avalaible+(saldoAvalaibleDPF+saldoAvalaibleAhorro);
            }          
            }
           } catch (Exception e) {
             e.getStackTrace();
            System.out.println("Error:" + e.getMessage());
        }
        Double saldos[]=new Double[2];
        saldos[0]=ledGer;
        saldos[1]=avalaible;
        System.out.println("Saldos:"+saldos[1]);
        em.close();
        return saldos;
    }
    
    public String dateToString(Date cadena) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String cadenaStr = sdf.format(cadena);
        return cadenaStr;
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

    public void cerrar() {
        emf.close();
    }
}
