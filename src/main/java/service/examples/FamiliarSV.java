/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service.examples;

import database.examples.FamiliarDBV;
import service.commons.ServiceVerticle;

/**
 *
 * @author ulises
 */
public class FamiliarSV extends ServiceVerticle{

    @Override
    protected String getDBAddress() {
        return FamiliarDBV.class.getSimpleName();
    }

    @Override
    protected String getEndpointAddress() {
        return "/familiars";
    }
    
}
