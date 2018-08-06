/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service.examples;

import database.examples.PetDBV;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import service.commons.ServiceVerticle;
import static utils.UtilsResponse.responsePropertyValue;
import utils.UtilsValidation;
import static utils.UtilsValidation.*;

/**
 *
 * @author ulises
 */
public class PetSV extends ServiceVerticle {

    @Override
    protected String getDBAddress() {
        return PetDBV.class.getSimpleName();
    }

    @Override
    protected String getEndpointAddress() {
        return "/pets";
    }

    @Override
    protected boolean isValidUpdateData(RoutingContext context) {
        JsonObject body = context.getBodyAsJson();
        try {           
            isName(body, "name");
            isGrater(body, "person_id", 0);
        } catch (UtilsValidation.PropertyValueException ex) {
            return responsePropertyValue(context, ex);
        }
        return super.isValidUpdateData(context); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected boolean isValidCreateData(RoutingContext context) {
        JsonObject body = context.getBodyAsJson();
        try {           
            isNameAndNotNull(body, "name");
            isGraterAndNotNull(body, "person_id", 0);
        } catch (UtilsValidation.PropertyValueException ex) {
            return responsePropertyValue(context, ex);
        }
        return super.isValidCreateData(context); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
}
