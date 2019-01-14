package com.example.demo.dao;

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

public class PersonDao {

    private static PersonDao INSTANCE = null;
    private static final String DB = "publicaciones-familias";
    private static final String COLLECTION = "personas";


    private PersonDao() {
        super();
    }

    public static synchronized PersonDao getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PersonDao();
        }
        return INSTANCE;
    }

    /**
     * Reads all the documents storaged on the "personas" collection, from the "publicaciones-familias" database,and puts them on an ArrayList
     *
     * @return an ArrayList of Person objects
     * @throws UnknownHostException if the personas collection is empty.
     * @see Person
     */
    public ArrayList<Person> listar() throws UnknownHostException {

        ArrayList<Person> personas = new ArrayList<>();
        DBCollection collection = getConnectionDbAndCollection(DB, COLLECTION);

        JacksonDBCollection<Person, String> coll = JacksonDBCollection.wrap(collection, Person.class, String.class);
        // Busco todos los documentos de la colección y los imprimo
        try (DBCursor<Person> cursor = coll.find()) {
            while (cursor.hasNext()) {
                personas.add(cursor.next());

            }
        }
        System.out.println(personas);

        return personas;


    }

    /**
     * Gets a Comment object specified by the id parameter.
     *
     * @param id the id number of the person which want to see.
     * @return the person which have the specified id if exist on the collection.
     * @see Person
     */
    public Person obtenerPorId(int id) throws UnknownHostException {
        Person p = new Person();

        DBCollection collection = getConnectionDbAndCollection(DB, COLLECTION);
        JacksonDBCollection<Person, String> coll = JacksonDBCollection.wrap(collection, Person.class, String.class);

        BasicDBObject query = new BasicDBObject();
        query.put("personId", id);

        try (DBCursor<Person> cursor = coll.find(query)) {
            while (cursor.hasNext()) {

                p = cursor.next();

            }
        }


        return p;
    }

    /**
     * Delete the person document specified  by the id parameter,from the "personas" collection on the "publicaciones-familias" database.
     *
     * @param id Person id atribute which want to delete.
     * @return true if the delete operation was succesfully, false if not.
     * @throws UnknownHostException if the database publicaciones-familias,the personas collection or both don´t exist.
     */
    public boolean eliminar(int id) throws UnknownHostException {
        boolean result = false;
        DBCollection collection = getConnectionDbAndCollection(DB, COLLECTION);

        DBObject findDoc = new BasicDBObject("personId", id);
        com.mongodb.WriteResult res = collection.remove(findDoc);
        if (res.getN() == 1) {
            result = true;
        }
        return result;

    }

    /**
     * Insert on the "personas" collection from the "publicaciones-familias" database,the Person object parameter.
     *
     * @param p Person who want to insert on the collection.
     * @return TRUE if the insert opration was succesfully, FALSE if not.
     * @throws UnknownHostException if the database publicaciones-familias,the personas collection or both don´t exist.
     */
    public boolean crear(Person p) throws UnknownHostException {

        boolean result = false;
        DBCollection collection = getConnectionDbAndCollection(DB, COLLECTION);
        JacksonDBCollection<Person, String> coll = JacksonDBCollection.wrap(collection, Person.class, String.class);

        long numDoc = collection.getCount() + 1;
        p.setPersonId((int) numDoc);


        WriteResult<Person, String> res = coll.insert(p);
        Person dObj = res.getSavedObject();
        p.set_id(dObj.get_id());
        System.out.println(dObj.toString());

        if (dObj != null) {

            result = true;
        }


        return result;
    }

    /**
     * Updates the comment specified by id parameter.
     *
     * @param id Id atribute of the Person object that want to update
     * @param p  Person object with the updated values.
     * @return true if the update operation was succesfully,false if not.
     * @throws UnknownHostException if the database publicaciones-familias,the personas collection or both don´t exist.
     */
    public boolean modificar(int id, Person p) throws UnknownHostException {
        boolean result = false;
        DBCollection collection = getConnectionDbAndCollection(DB, COLLECTION);
        JacksonDBCollection<Person, String> coll = JacksonDBCollection.wrap(collection, Person.class, String.class);
        BasicDBObject newDocument = new BasicDBObject();
        newDocument.put("_id", p.get_id());
        newDocument.put("personId", p.getPersonId());
        newDocument.put("familyId", p.getFamilyId());
        newDocument.put("nombre", p.getNombre());
        BasicDBObject searchQuery = new BasicDBObject().append("personId", p.getPersonId());

        coll.update(searchQuery, newDocument);


        if (!searchQuery.isEmpty()) {

            result = true;
        }
        return result;


    }

}
