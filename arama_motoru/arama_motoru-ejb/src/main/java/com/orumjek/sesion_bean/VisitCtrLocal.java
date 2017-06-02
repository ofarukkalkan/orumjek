/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orumjek.sesion_bean;

import com.orumjek.entity.Page;
import javax.ejb.Local;

/**
 *
 * @author omerfaruk
 */
@Local
public interface VisitCtrLocal {

    public void createVisit(Page startPage, short depth);
}
