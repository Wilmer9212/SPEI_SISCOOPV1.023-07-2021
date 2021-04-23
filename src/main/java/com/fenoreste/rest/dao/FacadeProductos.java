package com.fenoreste.rest.dao;

import com.fenoreste.rest.ResponseDTO.ProductsDTO;
import com.fenoreste.rest.Util.AbstractFacade;
import com.fenoreste.rest.entidades.CuentasSiscoop;
import java.util.ArrayList;
import java.util.List;

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
