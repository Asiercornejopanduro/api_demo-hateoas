package com.example.demo.pojo;

import org.springframework.hateoas.ResourceSupport;

public class Comment extends ResourceSupport {

    //Atributes
    private String _id;
    private Family familia;
    private String texto;
    private Person persona;
    private int commentId;

    //Contructors

    public Comment() {
        super();
        this.commentId = -1;
        this.familia = new Family();
        this.persona = new Person();
        this.texto = "";

    }


    public Comment(Family familia, String texto, Person persona, int commentId) {
        this();
        this.familia = familia;
        this.texto = texto;
        this.persona = persona;
        this.commentId = commentId;
    }

    //Getters & Setters

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public Family getFamilia() {
        return familia;
    }

    public void setFamilia(Family familia) {
        this.familia = familia;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public Person getPersona() {
        return persona;
    }

    public void setPersona(Person persona) {
        this.persona = persona;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }


}
