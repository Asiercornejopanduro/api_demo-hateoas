package com.example.demo.service;

import com.example.demo.dao.CommentDao;
import com.example.demo.pojo.Comment;

import java.net.UnknownHostException;
import java.util.ArrayList;

public class CommentService {

    private static CommentService INSTANCE = null;

    private static CommentDao commentDao = null;


    private CommentService() {
        super();
        commentDao = CommentDao.getInstance();
    }

    public static synchronized CommentService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CommentService();
        }
        return INSTANCE;
    }


    public ArrayList<Comment> listar() throws UnknownHostException {
        ArrayList<Comment> comments = new ArrayList<Comment>();
        comments = commentDao.listar();

        return comments;
    }

    public Comment obtenerPorId(int id) throws UnknownHostException {

        return commentDao.obtenerPorId(id);
    }

    public boolean eliminar(int id) throws  UnknownHostException {
        boolean resul = false;
        if(commentDao.eliminar(id)){
            resul = true;
        }

        return resul;
    }

    public boolean crear(Comment comment) throws  UnknownHostException {
        boolean resul = false;
        if(commentDao.crear(comment)){
            resul = true;
        }

        return resul;
    }

    public boolean modificar(int id,Comment comment) throws  UnknownHostException {
        boolean resul = false;
        if(commentDao.modificar(id,comment)){
            resul = true;
        }

        return resul;
    }

    public ArrayList<Comment> obtenerTodosPorAutor(int id) throws UnknownHostException {
        ArrayList<Comment> comments =commentDao.obtenerTodosPorAutor(id);
        return comments;

    }
}
