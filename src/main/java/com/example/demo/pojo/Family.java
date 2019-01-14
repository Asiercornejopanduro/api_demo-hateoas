package com.example.demo.pojo;

import org.springframework.hateoas.ResourceSupport;

import java.util.Arrays;

public class Family extends ResourceSupport {

    //Atributes
    private String _id;
    private int familyId;
    private String nombre;
    private Person[] personas;

    //Constructors
    public Family() {
        super();
        this.familyId = -1;
        this.nombre = "default";
        this.personas = new Person[0];
    }

    public Family(String _id, int familyId, String nombre) {
        this();
        this._id = _id;
        this.familyId = familyId;
        this.nombre = nombre;
    }

    //Getters & Setters

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public int getFamilyId() {
        return familyId;
    }

    public void setFamilyId(int familyId) {
        this.familyId = familyId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Person[] getPersonas() {
        return personas;
    }

    public void setPersonas(Person[] personas) {
        this.personas = personas;
    }

    //Methods

    @Override
    public String toString() {
        return "Family{" +
                "_id='" + _id + '\'' +
                ", familyId=" + familyId +
                ", nombre='" + nombre + '\'' +
                ", personas=" + Arrays.toString(personas) +
                '}';
    }
}
