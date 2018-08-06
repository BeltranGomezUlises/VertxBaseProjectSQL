package main;

import database.commons.*;
import database.examples.PersonDBV;
import database.examples.PetDBV;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.function.Function;
import service.commons.*;
import service.examples.PersonSV;
import service.examples.PetSV;
import utils.UtilsRouter;

/**
 * Main class to start all verticles
 *
 * @author Ulises Beltrán Gómez - beltrangomezulises@gmail.com
 */
public class MainVerticle extends AbstractVerticle {

    private static final String CONFIG_FILE_PATH = "./config.json";

    private String configFilePath;

    public MainVerticle(String configFilePath) {
        this.configFilePath = configFilePath;
    }

    public static void main(String[] args) {
        MainVerticle main;
        if (args.length > 0) {
            main = new MainVerticle(args[0]);
        } else {
            main = new MainVerticle(null);
        }
        Vertx v = Vertx.vertx();
        v.deployVerticle(main);
    }

    @Override
    public void start() throws Exception {
        //!important!//
        //initialize main router
        UtilsRouter.getInstance(vertx);

        //load config
        JsonObject config = this.loadConfigFromFile();
        //run verticles
        initializeVerticle(new PersonDBV(), new PersonSV(), config);
        initializeVerticle(new PetDBV(), new PetSV(), config);
    }

    /**
     * Deploy dbVerticle and if and only if the dbVerticle is successful, the serviceVerticle starts, both of them runs with the object conf as configuration
     *
     * @param dbVerticle data base verticle with the crud operations
     * @param serviceVerticle service verticle with the crud http services
     * @param config configuration object
     */
    public void initializeVerticle(DBVerticle dbVerticle, ServiceVerticle serviceVerticle, JsonObject config) {
        Future<String> dbVerticleDeployment = Future.future();
        vertx.deployVerticle(dbVerticle, new DeploymentOptions().setConfig(config), dbVerticleDeployment);

        dbVerticleDeployment.compose(__ -> {
            Future<String> httpVerticleDeployment = Future.future();
            vertx.deployVerticle(serviceVerticle, new DeploymentOptions().setConfig(config), httpVerticleDeployment.completer());
            return httpVerticleDeployment;
        });
    }

    /**
     * Deploy dbVerticle and if and only if the dbVerticle is successful, the serviceVerticle starts, both of them runs with the object conf as configuration
     *
     * @param dbVerticle data base verticle with the crud operations
     * @param serviceVerticle service verticle with the crud http services
     * @param config configuration object
     */
    public void initializeVerticle(DBVerticle dbVerticle, ServiceVerticle serviceVerticle, JsonObject config, Function finishHandler) {
        Future<String> dbVerticleDeployment = Future.future();
        vertx.deployVerticle(dbVerticle, new DeploymentOptions().setConfig(config), dbVerticleDeployment);

        dbVerticleDeployment.compose(__ -> {
            Future<String> httpVerticleDeployment = Future.future();
            vertx.deployVerticle(serviceVerticle, new DeploymentOptions().setConfig(config), httpVerticleDeployment.completer());
            return httpVerticleDeployment;
        }).compose(finishHandler);
    }

    /**
     * loads into the JsonObject dbConfig the configuration from the file in db
     *
     * @param config the config file to set the properties
     */
    private JsonObject loadConfigFromFile() throws IOException {
        if (this.configFilePath != null) {
            return this.loadConfigToJsonObject(this.configFilePath);
        } else {
            return this.loadConfigToJsonObject(CONFIG_FILE_PATH); //load the default config file for db
        }
    }

    /**
     * Loads a config file in json format to deploy verticles
     *
     * @param filePath the path of the to load
     * @return json object with the properties in the file
     * @throws FileNotFoundException if the file does not exist
     * @throws IOException if an error occuer while reading
     */
    private JsonObject loadConfigToJsonObject(final String filePath) throws IOException {
        JsonObject result;
        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            System.out.println("Running app with file: " + filePath + ", and the configs are:");
            result = new JsonObject(sb.toString());
        } catch (FileNotFoundException e) {
            System.out.println("The file: " + filePath + ", was not found, running with default configs to develop enviroment:");
            result = new JsonObject()
                    .put("url", "jdbc:mysql://localhost:3306/example_db")
                    .put("driver_class", "com.mysql.jdbc.Driver")
                    .put("user", "mysql")
                    .put("password", "2424")
                    .put("max_pool_size", 50)
                    .put(Constants.CONFIG_HTTP_SERVER_PORT, 8480); //se default configs
        }
        System.out.println(Json.encodePrettily(result));
        return result;
    }
}
