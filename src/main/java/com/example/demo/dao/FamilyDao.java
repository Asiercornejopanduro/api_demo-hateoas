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

public class FamilyDao {
    private static FamilyDao INSTANCE = null;
    private static final String DB = "publicaciones-familias";
    private static final String COLLECTION = "familias";
    private static MongoConector conector;
    private static DBCollection collection;


    public FamilyDao() {
        super();
        conector = MongoConector.getInstance();

    }

    public static synchronized FamilyDao getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FamilyDao();
        }
        return INSTANCE;
    }


    /**
     * Reads all the documents storaged on the "familias" collection, from the "publicaciones-familias" database,and puts them on an ArrayList
     *
     * @return an ArrayList of Family objects
     * @throws UnknownHostException if the familias collection is empty.
     * @see Family
     */
    public ArrayList<Family> listar() throws UnknownHostException {
        ArrayList<Family> familias = new ArrayList<>();
        collection = conector.getConnectionDbAndCollection(DB, COLLECTION);
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

    /**
     * Gets a Family object specified by the id parameter.
     *
     * @param id the id number of the family which want to see.
     * @return the family object  which have the specified id if exist on the collection.
     * @see Family
     */

    public Family obtenerPorId(int id) {
        Family familia = new Family();


        try {
            collection = conector.getConnectionDbAndCollection(DB, COLLECTION);
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

    /**
     * Delete the specified family document by the id parameter,from the "familias" collection on the "publicaciones-familias" database.
     *
     * @param id family id atribute which want to delete.
     * @return true if the delete operation was succesfully, false if not.
     */

    public boolean eliminar(int id) {
        boolean result = false;

        try {
            collection = conector.getConnectionDbAndCollection(DB, COLLECTION);
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

    /**
     * Insert on the "familias" collection from the "publicaciones-familias" database,the family object parameter.
     *
     * @param familia A Family object wich want to insert on the collection.
     * @return true if the object was inserted succesfully, false if not.
     * @see Family
     */

    public boolean crear(Family familia) {
        boolean result = false;

        try {
            collection = conector.getConnectionDbAndCollection(DB, COLLECTION);
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

    /**
     * Updates the family specified by id parameter.
     *
     * @param id      Id atribute of the family object that want to update
     * @param familia family object with the updated values.
     * @return true if the update operation was succesfully,false if not.
     * @throws UnknownHostException if the collection doesn´t exist.
     * @see Family
     */
    public boolean modificar(int id, Family familia) throws UnknownHostException {
        boolean result = false;
        int famMembers = familia.getPersonas().length;
        Person[] people = familia.getPersonas();
        collection = conector.getConnectionDbAndCollection(DB, COLLECTION);
        JacksonDBCollection<Family, String> coll = JacksonDBCollection.wrap(collection, Family.class, String.class);
        BasicDBObject newDocument = new BasicDBObject();
        BasicDBObject[] per = new BasicDBObject[0];

        for (int x = 0; x < famMembers; x++) {

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