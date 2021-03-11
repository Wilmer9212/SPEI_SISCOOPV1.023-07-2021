 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fenoreste.rest.ResponseDTO;

/**
 *
 * @author wilmer
 */
public class CustomerDetailsDTO {
      
      private String customerId;
      private String name;
      private String customerType;

    public CustomerDetailsDTO() {
    }

    public CustomerDetailsDTO(String customerId, String name, String customerType) {
        this.customerId = customerId;
        this.name = name;
        this.customerType = customerType;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getcustomerType() {
        return customerType;
    }

    public void setcustomerType(String customerType) {
        this.customerType = customerType;
    }

    @Override
    public String toString() {
        return "CustomerDetailsDTO{" + "customerId=" + customerId + ", name=" + name + ", customerType=" + customerType + '}';
    }
      
    
}
