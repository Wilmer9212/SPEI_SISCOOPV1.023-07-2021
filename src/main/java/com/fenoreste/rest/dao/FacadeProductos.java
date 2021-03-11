package com.fenoreste.rest.dao;

import com.fenoreste.rest.ResponseDTO.ProductsDTO;
import com.fenoreste.rest.Util.AbstractFacade;
import com.fenoreste.rest.entidades.Amortizaciones;
import com.fenoreste.rest.entidades.AmortizacionesPK;
import com.fenoreste.rest.entidades.Auxiliares;
import com.fenoreste.rest.entidades.Catalogo_Cuenta_Bankingly;
import com.fenoreste.rest.entidades.CuentasSiscoop;
import com.fenoreste.rest.entidades.Productos;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

public abstract class FacadeProductos<T> {

    private static EntityManagerFactory emf;
    private static final String PERSISTENCE_UNIT_NAME = "conexion";

    public FacadeProductos(Class<T> entityClass) {
        emf = AbstractFacade.conexion();//Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);    
    }

    public List<ProductsDTO> getProductos() {
        List<ProductsDTO> ListagetP = new ArrayList<ProductsDTO>();
        EntityManager em=emf.createEntityManager();
        try {
            String consulta="SELECT * FROM tipos_cuenta_siscoop";
            Query query=em.createNativeQuery(consulta,CuentasSiscoop.class);
            List<CuentasSiscoop>Lista=query.getResultList();
            for(int i=0;i<Lista.size();i++){
                CuentasSiscoop model=Lista.get(i);
                ProductsDTO dto=new ProductsDTO(
                model.getIdproducto(),
                model.getProducttypeid(),
                model.getProducttypename(),
                model.getDescripcion());
                ListagetP.add(dto);
            }
            System.out.println("ListaProd:"+ListagetP.size());
        } catch (Exception e) {
            cerrar();
        }
        return ListagetP;     
       
    }

    public void cerrar() {
        emf.close();
    }

}
