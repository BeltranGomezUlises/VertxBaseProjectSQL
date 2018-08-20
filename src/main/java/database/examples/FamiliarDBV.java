/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database.examples;

import database.commons.DBVerticle;

/**
 *
 * @author ulises
 */
public class FamiliarDBV extends DBVerticle {

    @Override
    public String getTableName() {
        return "familiar";
    }
    
}
