
import com.fenoreste.rest.Util.AbstractFacade;
import com.fenoreste.rest.entidades.Auxiliares;
import com.fenoreste.rest.entidades.AuxiliaresPK;
import com.fenoreste.rest.entidades.ReferenciasP;
import com.fenoreste.rest.entidades.ReferenciasPPK;
import com.fenoreste.rest.entidades.V_auxiliares;
import com.fenoreste.rest.entidades.v_auxiliaresPK;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Elliot
 */
public class Testing {
    public static void main(String[] args) {
        EntityManagerFactory emf=AbstractFacade.conexion();
        EntityManager em=emf.createEntityManager();
        try{
       /* ReferenciasPPK rfpk=new ReferenciasPPK(30503,110,4857,4);
        ReferenciasP rf=em.find(ReferenciasP.class, rfpk);
        System.out.println("rf:"+rf.getReferencia());
        */
            v_auxiliaresPK auxpk=new v_auxiliaresPK(30506,30303,534);                  
            V_auxiliares aa=em.find(V_auxiliares.class,auxpk);
                  System.out.println("aa:"+aa);
       em.close();
        }catch(Exception e){
            System.out.println("Error:"+e.getMessage());
        }
       
    }
}
