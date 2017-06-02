/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orumjek.managed_bean;

import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author omerfaruk
 */
@Named(value = "clientInfo")
@Dependent
public class ClientInfo {

    /**
     * Creates a new instance of ClientInfo
     */
    public ClientInfo() {
    }

    public String getInfo() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }
        return "Cihaz bilgileriniz -> "+ request.getHeader("user-agent").substring(request.getHeader("user-agent").indexOf("(")+1,request.getHeader("user-agent").lastIndexOf(")"));
    }
}
