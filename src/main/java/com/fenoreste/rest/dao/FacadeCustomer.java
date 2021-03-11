package com.fenoreste.rest.dao;

import com.fenoreste.rest.ResponseDTO.CustomerAccountDTO;
import com.fenoreste.rest.ResponseDTO.CustomerContactDetailsDTO;
import com.fenoreste.rest.ResponseDTO.CustomerDetailsDTO;
import com.fenoreste.rest.ResponseDTO.CustomerSearchDTO;
import com.fenoreste.rest.Util.AbstractFacade;
import com.fenoreste.rest.entidades.Auxiliares;
import com.fenoreste.rest.entidades.CuentasSiscoop;
import com.fenoreste.rest.entidades.Persona;
import com.fenoreste.rest.entidades.PersonasPK;
import java.text.SimpleDateFormat;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import java.util.ArrayList;
import java.util.Date;

public abstract class FacadeCustomer<T> {

    private static EntityManagerFactory emf;

    List<Object[]> lista = null;

    public FacadeCustomer(Class<T> entityClass) {
        emf = AbstractFacade.conexion();
    }

    public List<CustomerSearchDTO> search(String ogs) {
        EntityManager em = emf.createEntityManager();
        System.out.println("Lllego a buscar");
        List<CustomerSearchDTO> listaC = new ArrayList<CustomerSearchDTO>();
        CustomerSearchDTO client = null;
        try {
            int o = Integer.parseInt(ogs.substring(0, 6));
            int g = Integer.parseInt(ogs.substring(6, 8));
            int s = Integer.parseInt(ogs.substring(8, 14));
            System.out.println("ogs:" + o + "" + g + "" + s);
            PersonasPK pk = new PersonasPK(o, g, s);
            Persona p = em.find(Persona.class, pk);

            String customerId = ogs;
            String name = "", curp = "", taxId = "", customerType = "";
            Date birthDate = null;
            name = p.getNombre() + " " + p.getAppaterno() + " " + p.getApmaterno();
            taxId = p.getCurp();
            birthDate = p.getFechanacimiento();
            if (p.getRazonSocial()==null) {
                customerType = "INDIVIDUAL";
            } else {
                customerType = "GRUPAL";
            }

            client = new CustomerSearchDTO(
                    customerId,
                    name,
                    taxId,
                    dateToString(birthDate),
                    customerType);
            listaC.add(client);

            return listaC;
        } catch (Exception e) {
            System.out.println("Error al buscar cliente:" + e.getMessage());
            cerrar();
        }

        return null;
    }

    public CustomerDetailsDTO details(String ogs) {
        EntityManager em = emf.createEntityManager();
        List<CustomerDetailsDTO> listaC = new ArrayList<CustomerDetailsDTO>();
        CustomerDetailsDTO client = null;
        try {
            int o = Integer.parseInt(ogs.substring(0, 6));
            int g = Integer.parseInt(ogs.substring(6, 8));
            int s = Integer.parseInt(ogs.substring(8, 14));
            System.out.println("ogs:" + o + "" + g + "" + s);
            PersonasPK pk = new PersonasPK(o, g, s);
            Persona p = em.find(Persona.class, pk);

            String customerId = ogs;
            String name = "", customerType = "";
            
            name = p.getNombre() + " " + p.getAppaterno() + " " + p.getApmaterno();
            

            if (p.getRazonSocial()==null) {
                customerType = "INDIVIDUAL";
            } else {
                customerType = "GRUPAL";
            }
            client = new CustomerDetailsDTO(
                    customerId,
                    name,
                    customerType);
            
            
            
            System.out.println("cliente:"+client);
            return client;
        } catch (Exception e) {
            System.out.println("Error al buscar cliente:" + e.getMessage());
            cerrar();
        }

        return null;
    }

    public List<CustomerContactDetailsDTO> ContactDetails(String ogs) {
        EntityManager em = emf.createEntityManager();
        Query query = null;
        List<Object[]> ListaObjetos = null;
        String consulta = "SELECT CASE WHEN p.telefono != '' THEN p.telefono ELSE '0' END as phone,"
                + " CASE WHEN p.celular != '' THEN p.celular ELSE '0' END as cellphone,"
                + " CASE WHEN p.email != '' THEN  p.email ELSE '0' END as email FROM personas p WHERE replace(to_char(p.idorigen,'099999')||to_char(p.idgrupo,'09')||to_char(p.idsocio,'099999'),' ','')='" + ogs + "'";
        CustomerContactDetailsDTO contactsPhone = new CustomerContactDetailsDTO();
        CustomerContactDetailsDTO contactsCellphone = new CustomerContactDetailsDTO();
        CustomerContactDetailsDTO contactsEmail = new CustomerContactDetailsDTO();
        List<CustomerContactDetailsDTO> ListaContactos = new ArrayList<CustomerContactDetailsDTO>();
       
        try {
            int o = Integer.parseInt(ogs.substring(0, 6));
            int g = Integer.parseInt(ogs.substring(6, 8));
            int s = Integer.parseInt(ogs.substring(8, 14));
            System.out.println("ogs:" + o + "" + g + "" + s);
            PersonasPK pk = new PersonasPK(o, g, s);
            Persona p = em.find(Persona.class, pk);
            
            /*query = em.createNativeQuery(consulta);
            ListaObjetos = query.getResultList();
            System.out.println("Size:" + ListaObjetos.size());
            for (Object[] obj : ListaObjetos) {
                System.out.println("Ok:" + obj[0].toString());
                if (!obj[0].toString().equals("0")) {*/
            if(p.getTelefono()!=null){
                    System.out.println("Entro");
                    contactsPhone.setCustomerContactId(ogs);
                    contactsPhone.setCustomerContactType("phone");
                    contactsPhone.setPhoneNumber(p.getTelefono());
                    ListaContactos.add(contactsPhone);

                }
                //if (!obj[1].toString().equals("0")) {
                  if(p.getCelular()!=null){    
                    contactsCellphone.setCustomerContactId(ogs);
                    contactsCellphone.setCustomerContactType("cellphone");
                    contactsCellphone.setCellphoneNumber(p.getCelular());
                    ListaContactos.add(contactsCellphone);
                }
                if (p.getEmail()!=null) {
                    contactsEmail.setCustomerContactId(ogs);
                    contactsEmail.setCustomerContactType("email");
                    contactsEmail.setEmail(p.getEmail());
                    
                    ListaContactos.add(contactsEmail);
                }

            
            System.out.println("ListaContactos:" + ListaContactos);

            cerrar();
        } catch (Exception e) {
            System.out.println("Error al obtener detalles del socio:" + e.getMessage());
            cerrar();
        }

        return ListaContactos;
    }

    public List<CustomerAccountDTO> Accounts(String customerId) {
        EntityManager em = emf.createEntityManager();
        Query query = null;
        List<CustomerAccountDTO> ListaProductos = new ArrayList<CustomerAccountDTO>();
        String consulta = "SELECT * FROM auxiliares a WHERE replace((to_char(idorigen,'099999')||to_char(idgrupo,'09')||to_char(idsocio,'099999')),' ','')='" + customerId + "'";
        CustomerAccountDTO producto = new CustomerAccountDTO();
        try {

            query = em.createNativeQuery(consulta, Auxiliares.class);
            List<Auxiliares> ListaProd = query.getResultList();
            String status = "";
            String accountType = "";
            Object[] arr = {};
            Object[] arr1 = {"relationCode", "SOW"};
            List<CustomerAccountDTO> listaDeCuentas = new ArrayList<CustomerAccountDTO>();
           
            int idproducto = 0;
            int prod = 0;
            for (int k = 0; k < 1; k++) {
                for (int i = 0; i < ListaProd.size(); i++) {
                    Auxiliares a = ListaProd.get(i);
                    System.out.println("IdproductoA:" + a.getAuxiliaresPK().getIdproducto());
                    try {
                        CuentasSiscoop tp = em.find(CuentasSiscoop.class, a.getAuxiliaresPK().getIdproducto());
                        accountType = String.valueOf(tp.getProducttypename().toUpperCase());
                    } catch (Exception e) {
                    }

                    if (a.getEstatus() == 2) {
                        status = "OPEN";
                    } else if (a.getEstatus() == 3) {
                        status = "CLOSED";
                    } else {
                        status = "INACTIVE";
                    }

                    String og = String.format("%06d", a.getIdorigen()) + String.format("%02d", a.getIdgrupo());
                    String s = String.format("%06d", a.getIdsocio());

                    String op = String.format("%06d", a.getAuxiliaresPK().getIdorigenp()) + String.format("%05d", a.getAuxiliaresPK().getIdproducto());
                    String aa = String.format("%08d", a.getAuxiliaresPK().getIdauxiliar());
                    producto = new CustomerAccountDTO(
                            op+aa,
                            op + aa,
                            false,
                            accountType,
                            "MXN",
                            String.valueOf(a.getAuxiliaresPK().getIdproducto().toString()),
                            status,
                            arr,
                            arr1);
                            listaDeCuentas.add(producto);
                            accountType = "";
                }
            }
            System.out.println("Lista de cuentas:" + listaDeCuentas);
            return listaDeCuentas;

        } catch (Exception e) {

            System.out.println("Error al obtener cuentas:" + e.getMessage());
        } finally {
            em.clear();
        }
        return null;
    }

    public boolean findCustomer(String ogs) {
        boolean bandera = false;
        EntityManager em = emf.createEntityManager();
        try {
            Query query = em.createNativeQuery("SELECT * FROM personas WHERE replace(to_char(p.idorigen,'099999')||to_char(p.idgrupo,'09')||to_char(p.idsocio,'099999'),' ','')='" + ogs + "'");
            if (query != null) {
                bandera = true;
            }
        } catch (Exception e) {
            cerrar();
        }
        return bandera;
    }

    public String dateToString(Date cadena) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String cadenaStr = sdf.format(cadena);
        return cadenaStr;
    }

    public Date stringToDate(String cadena) {
        return null;
    }

    public void cerrar() {
        emf.close();
    }

}
