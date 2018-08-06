/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database.examples;

import database.commons.DBVerticle;
import database.commons.GenericCreate;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import java.util.ArrayList;
import java.util.List;
import static service.commons.Constants.ACTION;
import static service.commons.Constants.CREATED_BY;

/**
 *
 * @author ulises
 */
public class PersonDBV extends DBVerticle {

    public static final String REGISTER = "PersonDBV.register";

    @Override
    public String getTableName() {
        return "person";
    }

    @Override
    protected void onMessage(Message<JsonObject> message) {
        super.onMessage(message);
        String action = message.headers().get(ACTION);
        switch (action) {
            case REGISTER:
                this.register(message);
                break;
        }
    }

    private void register(Message<JsonObject> message) {
        this.startTransaction(message, con -> {
            JsonObject body = message.body();
            JsonObject person = body.copy();
            person.remove("pets");

            GenericCreate create = this.generateGenericCreate(person);
            con.updateWithParams(create.getQuery(), create.getParams(), reply -> {
                if (reply.succeeded()) {
                    final int personId = reply.result().getKeys().getInteger(0);
                    JsonArray pets = body.getJsonArray("pets");
                    if (pets != null && !pets.isEmpty()) {
                        List<String> batch = new ArrayList<>();
                        for (int i = 0; i < pets.size(); i++) {
                            JsonObject pet = pets.getJsonObject(i);
                            pet.put("person_id", personId);
                            batch.add(this.generateGenericCreate("pet", pet));
                        }
                        con.batch(batch, finalReply -> {
                            if (finalReply.succeeded()) {
                                this.commit(con, message, new JsonObject().put("id", personId));
                            } else {
                                this.rollback(con, finalReply.cause(), message);
                            }
                        });
                    } else {
                        this.commit(con, message, person);
                    }
                } else {
                    this.rollback(con, reply.cause(), message);
                }
            });
        });
    }

}
