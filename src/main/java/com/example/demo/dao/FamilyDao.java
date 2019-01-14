package com.example.demo.dao;

import com.example.demo.pojo.Family;
import com.example.demo.pojo.Person;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import org.mongojack.DBCursor;
import org.mongojack.JacksonDBCollection;
import org.mongojack.WriteResult;

import java.net.UnknownHostException;
import java.util.ArrayList;

import static com.example.demo.dao.MongoConector.getConnectionDbAndCollection;

public class FamilyDao {
    private static FamilyDao INSTANCE = null;
    private static final String DB = "publicaciones-familias";
    private static final String COLLECTION = "familias";


    public FamilyDao() {
        super();
    }

    public static synchronized FamilyDao getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FamilyDao();
        }
        return INSTANCE;
    }

    public ArrayList<Family> listar() throws UnknownHostException {
        ArrayList<Family> familias = new ArrayList<Family>();
        DBCollection collection;

        collection = getConnectionDbAndCollection(DB, COLLECTION);


        JacksonDBCollection<Family, String> coll = JacksonDBCollection.wrap(collection, Family.class, String.class);
        // Busco todos los documentos de la colección y los imprimo
        try (DBCursor<Family> cursor = coll.find()) {
            while (cursor.hasNext()) {
                familias.add(cursor.next());

            }
        }
        System.out.println(familias);


        return familias;
    }

    public Family obtenerPorId(int id) {
        Family familia = new Family();

        DBCollection collection = null;
        try {
            collection = getConnectionDbAndCollection(DB, COLLECTION);
            JacksonDBCollection<Family, String> coll = JacksonDBCollection.wrap(collection, Family.class, String.class);

            BasicDBObject query = new BasicDBObject();
            query.put("familyId", id);

            DBCursor<Family> cursor = coll.find(query);
            while (cursor.hasNext()) {

                familia = cursor.next();

            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        return familia;
    }

    public boolean eliminar(int id) {
        boolean result = false;
        DBCollection collection = null;
        try {
            collection = getConnectionDbAndCollection(DB, COLLECTION);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        JacksonDBCollection<Family, String> coll = JacksonDBCollection.wrap(collection, Family.class, String.class);

        DBObject findDoc = new BasicDBObject("familyId", id);
        com.mongodb.WriteResult res = collection.remove(findDoc);
        if (res.getN() == 1) {
            result = true;
        }
        return result;
    }

    public boolean crear(Family familia) {
        boolean result = false;
        DBCollection collection = null;
        try {
            collection = getConnectionDbAndCollection(DB, COLLECTION);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        JacksonDBCollection<Family, String> coll = JacksonDBCollection.wrap(collection, Family.class, String.class);

        long numDoc = collection.getCount() + 1;
        familia.setFamilyId((int) numDoc);


        WriteResult<Family, String> res = coll.insert(familia);
        Family dObj = res.getSavedObject();
        familia.set_id(dObj.get_id());
        System.out.println(dObj.toString());

        if (dObj != null) {

            result = true;
        }


        return result;
    }

    public boolean modificar(int id, Family familia) throws UnknownHostException {
        boolean result = false;
        int famMembers = familia.getPersonas().length;
        Person[] people = familia.getPersonas();
        DBCollection collection = getConnectionDbAndCollection(DB, COLLECTION);
        JacksonDBCollection<Family, String> coll = JacksonDBCollection.wrap(collection, Family.class, String.class);
        BasicDBObject newDocument = new BasicDBObject();
        BasicDBObject query = new BasicDBObject();

        BasicDBObject[] per= new BasicDBObject[0];

        for (int x = 0; x < famMembers; x++) {
            newDocument.put("personas[" + x + "]._id", people[x].get_id());
            newDocument.put("personas" + x + ".nombre", people[x].getNombre());
            newDocument.put("personas" + x + ".familyId", people[x].getFamilyId());
            newDocument.put("personas" + x + ".personId", people[x].getPersonId());
        }
        BasicDBObject searchQuery = new BasicDBObject().append("familyId", familia.getFamilyId());

        coll.update(searchQuery, newDocument);


        if (!searchQuery.isEmpty()) {

            result = true;
        }

        return result;
    }
}