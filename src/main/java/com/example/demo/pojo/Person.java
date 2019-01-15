package com.example.demo.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.hateoas.ResourceSupport;

public class Person extends ResourceSupport {

    //Atributes
    @JsonIgnore
    private String _id;
    private int personId;
    private int familyId;
    private String nombre;


    //Constructors

    /**
     * Class cosntructor.
     */
    public Person() {
        super();
        this.nombre = "default";
        this.personId = -1;
        this.familyId = -1;

    }

    /**
     * Class cosntructor specifying ObjectId, person id, person name, and person family id.
     *
     * @param _id      ObjectId of the document.
     * @param personId int person self id.
     * @param nombre   String person name.
     * @param familyId int id of the person family.
     */
    public Person(String _id, int personId, String nombre, int familyId) {
        this();
        this._id = _id;
        this.personId = personId;
        this.nombre = nombre;
        this.familyId = familyId;
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
     * @param _id String the person ObjectId
     */
    public void set_id(String _id) {
        this._id = _id;
    }

    /**
     * Gets the person self id.
     *
     * @return person self id.
     */

    public int getPersonId() {
        return personId;
    }

    /**
     * Sets the person self id.
     *
     * @param personId int Person self id.
     */
    public void setPersonId(int personId) {
        this.personId = personId;
    }

    /**
     * Gets person name.
     *
     * @return String person name.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Sets the name of the person.
     *
     * @param nombre String name of the person.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Gets the family id of the person family.
     *
     * @return int family id.
     */
    public int getFamilyId() {
        return familyId;
    }

    /**
     * Sets the id of the person family.
     *
     * @param familyId int family id.
     */
    public void setFamilyId(int familyId) {
        this.familyId = familyId;
    }


    //Methods

    /**
     * Shows entire family object.
     *
     * @return String description of the Person object.
     */
    @Override
    public String toString() {
        return "Person{" +
                "_id='" + _id + '\'' +
                ", personId=" + personId +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}
