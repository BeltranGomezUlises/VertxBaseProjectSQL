/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service.examples;

import static database.commons.ErrorCodes.DB_ERROR;
import database.examples.PersonDBV;
import io.vertx.core.Future;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import static service.commons.Constants.ACTION;
import static service.commons.Constants.CREATED_BY;
import static service.commons.Constants.INVALID_DATA;
import static service.commons.Constants.INVALID_DATA_MESSAGE;
import service.commons.ServiceVerticle;
import utils.UtilsJWT;
import utils.UtilsResponse;
import static utils.UtilsResponse.responseError;
import static utils.UtilsResponse.responseOk;
import static utils.UtilsResponse.responsePropertyValue;
import static utils.UtilsResponse.responseWarning;
import static utils.UtilsValidation.*;

/**
 *
 * @author ulises
 */
public class PersonSV extends ServiceVerticle {

    @Override
    protected String getDBAddress() {
        return PersonDBV.class.getSimpleName();
    }

    @Override
    protected String getEndpointAddress() {
        return "/persons";
    }

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        super.start(startFuture);
        this.router.post("/action/register").handler(BodyHandler.create());
        this.router.post("/action/register").handler(this::register);
    }

    @Override
    protected boolean isValidUpdateData(RoutingContext context) {
        JsonObject body = context.getBodyAsJson();
        try {
            isMail(body, "email");
            isPhoneNumber(body, "phone");
            isName(body, "name");
        } catch (PropertyValueException ex) {
            return responsePropertyValue(context, ex);
        }
        return super.isValidUpdateData(context);
    }

    @Override
    protected boolean isValidCreateData(RoutingContext context) {
        JsonObject body = context.getBodyAsJson();
        try {
            isMailAndNotNull(body, "email");
            isPhoneNumber(body, "phone");
            isNameAndNotNull(body, "name");
        } catch (PropertyValueException ex) {
            return responsePropertyValue(context, ex);
        }
        return super.isValidCreateData(context);
    }

    private void register(RoutingContext context) {
        String jwt = context.request().getHeader("Authorization");
        if (UtilsJWT.isTokenValid(jwt)) {
            JsonObject body = context.getBodyAsJson();
            final int userId = UtilsJWT.getUserIdFrom(jwt);
            body.put(CREATED_BY, userId);
            DeliveryOptions options = new DeliveryOptions().addHeader(ACTION, PersonDBV.REGISTER);
            try {
                isMailAndNotNull(body, "email");
                isPhoneNumber(body, "phone");
                isNameAndNotNull(body, "name");

                //validate pets
                JsonArray pets = body.getJsonArray("pets");
                if (pets != null) {
                    for (int i = 0; i < pets.size(); i++) {
                        JsonObject pet = pets.getJsonObject(i);
                        isName(pet, "name", "pets");
                        pet.put(CREATED_BY, userId);
                    }
                }
                this.vertx.eventBus().send(
                        PersonDBV.class.getSimpleName(), body, options,
                        reply -> {
                            if (reply.succeeded()) {
                                if (reply.result().headers().contains(DB_ERROR.toString())) {
                                    responseWarning(
                                            context,
                                            INVALID_DATA,
                                            INVALID_DATA_MESSAGE,
                                            reply.result().body()
                                    );
                                } else {
                                    responseOk(
                                            context,
                                            reply.result().body(),
                                            "Created"
                                    );
                                }
                            } else {
                                responseError(context, reply.cause().getMessage());
                            }
                        }
                );
            } catch (PropertyValueException ex) {
                UtilsResponse.responsePropertyValue(context, ex);
            }
        } else {
            UtilsResponse.responseInvalidToken(context);
        }
    }
}
