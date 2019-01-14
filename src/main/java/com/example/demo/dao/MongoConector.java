package com.example.demo.dao;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

import java.net.UnknownHostException;

public class MongoConector {


    public MongoConector() {
        super();
    }

    /**
     * @param dataBase  Database name
     * @param coleccion Collection from the database
     * @return DBCollection Collection from the database specified by the coleccion parameter
     */
    public static DBCollection getConnectionDbAndCollection(String dataBase, String coleccion) throws UnknownHostException {

        // PASO 1: Conexión al Server de MongoDB Pasandole el host y el puerto
        MongoClient mongoClient = new MongoClient("localhost", 27017);

        // PASO 2: Conexión a la base de datos
        DB db = mongoClient.getDB(dataBase);

        return db.getCollection(coleccion);

    }
}
