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

    public ArrayList<Comment> listar() throws UnknownHostException {
        ArrayList<Comment> comments = new ArrayList<Comment>();
        DBCollection collection= getConnectionDbAndCollection(DB, COLLECTION);

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

    public Comment obtenerPorId(int id) {
        Comment comment = new Comment();

        DBCollection collection = null;
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


    public ArrayList<Comment> obtenerTodosPorAutor(int id) throws UnknownHostException {
        ArrayList<Comment> allComments=listar();
        ArrayList<Comment> comments=new ArrayList<Comment>();

        DBCollection collection = null;
        try {
            collection = getConnectionDbAndCollection(DB, COLLECTION);
            JacksonDBCollection<Comment, String> coll = JacksonDBCollection.wrap(collection, Comment.class, String.class);

           /* BasicDBObject query = new BasicDBObject();
            query.put("person.personId", id);

            DBCursor<Comment> cursor = coll.find(query);*/

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


     WriteResult<Comment,String> res=coll.save(comment);

        if (res.getUpsertedId()!="") {

            result = true;
        }


        return result;
    }

    public boolean modificar(int id, Comment comment) throws UnknownHostException {
        boolean result = false;
        DBCollection collection = getConnectionDbAndCollection(DB, COLLECTION);
        JacksonDBCollection<Comment, String> coll = JacksonDBCollection.wrap(collection, Comment.class, String.class);
        BasicDBObject query = new BasicDBObject();
        BasicDBObject obj= new BasicDBObject();
        BasicDBObject fam= new BasicDBObject();
        BasicDBObject per= new BasicDBObject();



        obj.append("commentId",comment.getCommentId());
        obj.append("texto",comment.getTexto());

        per.append("_id",comment.getPersona().get_id());
        per.append("personId",comment.getPersona().getPersonId());
        per.append("nombre",comment.getPersona().getNombre());
        per.append("familyId",comment.getPersona().getFamilyId());


        fam.append("_id",comment.getFamilia().get_id());
        fam.append("familyId",comment.getFamilia().getFamilyId());
        fam.append("nombre",comment.getFamilia().getNombre());



        obj.append("familia",fam);
        obj.append("persona",per);



        query.put("commentId", id);
        WriteResult<Comment,String> res = coll.update(query,obj);


        if (res.getN()==1) {

            result = true;
        }
        return result;
    }
}
