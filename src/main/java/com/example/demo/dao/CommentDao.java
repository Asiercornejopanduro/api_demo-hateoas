package com.example.demo.dao;

import com.example.demo.pojo.Comment;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import org.mongojack.DBCursor;
import org.mongojack.JacksonDBCollection;
import org.mongojack.WriteResult;

import java.net.UnknownHostException;
import java.util.ArrayList;

import static com.example.demo.dao.MongoConector.getConnectionDbAndCollection;

public class CommentDao {


    private static CommentDao INSTANCE = null;
    private static final String DB = "publicaciones-familias";
    private static final String COLLECTION = "comments";


    private CommentDao() {
        super();
    }

    public static synchronized CommentDao getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CommentDao();
        }
        return INSTANCE;
    }

    /**
     * Reads all the documents storaged on the "comments" collection, from the "publicaciones-familias" database,and puts them on an ArrayList
     *
     * @return an ArrayList of Comment objects
     * @throws UnknownHostException if the comments collection is empty.
     * @see Comment
     */
    public ArrayList<Comment> listar() throws UnknownHostException {
        ArrayList<Comment> comments = new ArrayList<>();
        DBCollection collection = getConnectionDbAndCollection(DB, COLLECTION);

        JacksonDBCollection<Comment, String> coll = JacksonDBCollection.wrap(collection, Comment.class, String.class);
        // Busco todos los documentos de la colección y los imprimo
        try (DBCursor<Comment> cursor = coll.find()) {
            while (cursor.hasNext()) {
                comments.add(cursor.next());

            }
        }

        System.out.println(comments);


        return comments;
    }

    /**
     * Gets a Comment object specified by the id parameter.
     *
     * @param id the id number of the comment which want to see.
     * @return the comment which have the specified id if exist on the collection.
     * @see Comment
     */
    public Comment obtenerPorId(int id) {
        Comment comment = new Comment();

        DBCollection collection;
        try {
            collection = getConnectionDbAndCollection(DB, COLLECTION);
            JacksonDBCollection<Comment, String> coll = JacksonDBCollection.wrap(collection, Comment.class, String.class);

            BasicDBObject query = new BasicDBObject();
            query.put("commentId", id);

            DBCursor<Comment> cursor = coll.find(query);
            while (cursor.hasNext()) {

                comment = cursor.next();

            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        return comment;
    }

    /**
     * Gets an ArrayList of Comment Objects whith the same author.
     *
     * @param id Person id atribute, which indicates the auhor of the comments.
     * @return an Arraylist of Comment objects writed by the same person specified by the id parameter if exist.
     * @see Comment,com.example.demo.pojo.Person
     */
    public ArrayList<Comment> obtenerTodosPorAutor(int id) {
        ArrayList<Comment> comments = new ArrayList<Comment>();

        DBCollection collection;
        try {
            collection = getConnectionDbAndCollection(DB, COLLECTION);
            JacksonDBCollection<Comment, String> coll = JacksonDBCollection.wrap(collection, Comment.class, String.class);
            DBObject findComments = new BasicDBObject("persona.personId", id);
            DBCursor<Comment> cursor = coll.find(findComments);
            while (cursor.hasNext()) {

                comments.add(cursor.next());

            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        return comments;
    }

    /**
     * Delete the specified comment by the id parameter,from the "comments" collection on the "publicaciones-familias" database.
     *
     * @param id comment id atribute which want to delete.
     * @return true if the delete operation was succesfully, false if not.
     */
    public boolean eliminar(int id) {
        boolean result = false;
        DBCollection collection = null;
        try {
            collection = getConnectionDbAndCollection(DB, COLLECTION);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        JacksonDBCollection<Comment, String> coll = JacksonDBCollection.wrap(collection, Comment.class, String.class);

        DBObject findDoc = new BasicDBObject("commentId", id);
        com.mongodb.WriteResult res = collection.remove(findDoc);
        if (res.getN() == 1) {
            result = true;
        }
        return result;
    }

    /**
     * Insert on the "comments" collection from the "publicaciones-familias" database,the comment object parameter.
     *
     * @param comment A comment object wich want to insert on the collection.
     * @return true if the object was inserted succesfully, false if not.
     * @see Comment
     */
    public boolean crear(Comment comment) {
        boolean result = false;
        DBCollection collection = null;
        try {
            collection = getConnectionDbAndCollection(DB, COLLECTION);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        JacksonDBCollection<Comment, String> coll = JacksonDBCollection.wrap(collection, Comment.class, String.class);

        long numDoc = collection.getCount() + 1;
        comment.setCommentId((int) numDoc);


        WriteResult<Comment, String> res = coll.save(comment);

        if (res.getUpsertedId() != "") {

            result = true;
        }


        return result;
    }

    /**
     * Updates the comment specified by id parameter.
     *
     * @param id      Id atribute of the comment object that want to update
     * @param comment Comment object with the updated values.
     * @return true if the update operation was succesfully,false if not.
     * @throws UnknownHostException if the database publicaciones-familias,the comment collection or both don´t exist.
     */
    public boolean modificar(int id, Comment comment) throws UnknownHostException {
        boolean result = false;
        DBCollection collection = getConnectionDbAndCollection(DB, COLLECTION);
        JacksonDBCollection<Comment, String> coll = JacksonDBCollection.wrap(collection, Comment.class, String.class);
        BasicDBObject query = new BasicDBObject();
        BasicDBObject obj = new BasicDBObject();
        BasicDBObject fam = new BasicDBObject();
        BasicDBObject per = new BasicDBObject();


        obj.append("commentId", comment.getCommentId());
        obj.append("texto", comment.getTexto());

        per.append("_id", comment.getPersona().get_id());
        per.append("personId", comment.getPersona().getPersonId());
        per.append("nombre", comment.getPersona().getNombre());
        per.append("familyId", comment.getPersona().getFamilyId());


        fam.append("_id", comment.getFamilia().get_id());
        fam.append("familyId", comment.getFamilia().getFamilyId());
        fam.append("nombre", comment.getFamilia().getNombre());


        obj.append("familia", fam);
        obj.append("persona", per);


        query.put("commentId", id);
        WriteResult<Comment, String> res = coll.update(query, obj);


        if (res.getN() == 1) {

            result = true;
        }
        return result;
    }
}
