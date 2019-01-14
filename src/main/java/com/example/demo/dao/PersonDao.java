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
    private ArrayList<Person> personas = null;
    private static PersonDao personaDao = null;


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


    public ArrayList<Person> listar() throws UnknownHostException {

        personas = new ArrayList<>();
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

    public boolean eliminar(int id) throws UnknownHostException {
        boolean result = false;
        DBCollection collection = getConnectionDbAndCollection(DB, COLLECTION);
        JacksonDBCollection<Person, String> coll = JacksonDBCollection.wrap(collection, Person.class, String.class);

        DBObject findDoc = new BasicDBObject("personId", id);
        com.mongodb.WriteResult res = collection.remove(findDoc);
        if (res.getN() == 1) {
            result = true;
        }
        return result;

    }

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
