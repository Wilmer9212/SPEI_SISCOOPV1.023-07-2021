package com.fenoreste.rest.Dao;

import com.fenoreste.rest.Entidades.Productos;
import com.fenoreste.rest.ResponseDTO.ProductsDTO;
import java.util.List;

public class ProductsDAO extends FacadeProductos<Productos> {

    public ProductsDAO() {
        super(Productos.class);
    }

    public List<ProductsDTO> getProductos(String accountType) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void cerrar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public List<String> Rates(String trim, int amount, String customerId, String productCode) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
