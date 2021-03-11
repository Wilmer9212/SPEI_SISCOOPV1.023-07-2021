/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fenoreste.rest.ResponseDTO;

/**
 *
 * @author Elliot
 */
public class ClearingCodeRulesDTO {
     
    private String code;
    private String charsetType;

    public ClearingCodeRulesDTO() {
    }

    public ClearingCodeRulesDTO(String code, String charsetType) {
        this.code = code;
        this.charsetType = charsetType;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCharsetType() {
        return charsetType;
    }

    public void setCharsetType(String charsetType) {
        this.charsetType = charsetType;
    }

    @Override
    public String toString() {
        return "ClearingCodeRulesDTO{" + "code=" + code + ", charsetType=" + charsetType + '}';
    }
    
    
    
}
