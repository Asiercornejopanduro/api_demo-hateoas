package com.example.demo.pojo;

import org.springframework.hateoas.ResourceSupport;

public class Person extends ResourceSupport {

    //Atributes

    private String _id;
    private int personId;
    private int familyId;
    private String nombre;


    //Constructors
    public Person() {
        super();
        this.nombre = "default";
        this.personId = -1;
        this.familyId = -1;

    }

    public Person(String _id, int personId, String nombre, int familyId) {
        this();
        this._id = _id;
        this.personId = personId;
        this.nombre = nombre;
        this.familyId = familyId;
    }

    //Getters & Setters

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getFamilyId() {
        return familyId;
    }

    public void setFamilyId(int familyId) {
        this.familyId = familyId;
    }


    //Methods

    @Override
    public String toString() {
        return "Person{" +
                "_id='" + _id + '\'' +
                ", personId=" + personId +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}
