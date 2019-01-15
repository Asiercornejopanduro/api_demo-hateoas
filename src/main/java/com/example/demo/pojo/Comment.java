package com.example.demo.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.hateoas.ResourceSupport;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Comment extends ResourceSupport {

    //Atributes
    @JsonIgnore
    private String _id;
    private Family familia;
    private String texto;
    private Person persona;
    private int commentId;

    //Contructors

    /**
     * Class constructor
     */
    public Comment() {
        super();
        this.commentId = -1;
        this.familia = new Family();
        this.persona = new Person();
        this.texto = "";

    }

    /**
     * Class constructor specifying the Family object, text, author of the comment and the comment id.
     *
     * @param familia   Family object which indicates the family of the author.
     * @param texto     String text of the comment.
     * @param persona   Person object wich indicates the author.
     * @param commentId Int comment identification.
     */
    public Comment(Family familia, String texto, Person persona, int commentId) {
        this();
        this.familia = familia;
        this.texto = texto;
        this.persona = persona;
        this.commentId = commentId;
    }

    //Getters & Setters

    /**
     * Gets the ObjectId atribute from the document.
     *
     * @return String of the document ObjectId.
     */
    public String get_id() {
        return _id;
    }

    /**
     * Sets the ObjectId atributte from the document.
     *
     * @param _id String the comment ObjectId
     */
    public void set_id(String _id) {
        this._id = _id;
    }

    /**
     * Gets the Family object embedded on the document.
     *
     * @return Family object embedded on the document.
     */
    public Family getFamilia() {
        return familia;
    }

    /**
     * Sets the family object embedded on the document.
     *
     * @param familia Family object wich want to embedded on the document.
     */
    public void setFamilia(Family familia) {
        this.familia = familia;
    }

    /**
     * Gets the comment text.
     *
     * @return String text of the comment.
     */
    public String getTexto() {
        return texto;
    }

    /**
     * Sets the comment text
     *
     * @param texto String text wich want to insert on the comment.
     */
    public void setTexto(String texto) {
        this.texto = texto;
    }

    /**
     * Gets Person object embedded on  the document.
     *
     * @return Person object from the comment document.
     */
    public Person getPersona() {
        return persona;
    }

    /**
     * Sets Person object embedded on the document.
     *
     * @param persona Person object wich want to insert on the document.
     */
    public void setPersona(Person persona) {
        this.persona = persona;
    }

    /**
     * Gets the comment id
     *
     * @return int comment id.
     */
    public int getCommentId() {
        return commentId;
    }

    /**
     * Sets the comment id
     *
     * @param commentId int comment id.
     */
    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    //Methods

    /**
     * Shows the entire object into a String chain.
     *
     * @return String description of the Comment object.
     */
    @Override
    public String toString() {
        return "Comment{" +
                "_id='" + _id + '\'' +
                ", familia=" + familia +
                ", texto='" + texto + '\'' +
                ", persona=" + persona +
                ", commentId=" + commentId +
                '}';
    }

}
