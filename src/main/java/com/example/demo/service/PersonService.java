package com.example.demo.service;

import com.example.demo.dao.PersonDao;
import com.example.demo.pojo.Person;

import java.net.UnknownHostException;
import java.util.ArrayList;

public class PersonService {

    private static PersonService INSTANCE = null;

    private static PersonDao personDao = null;


    public PersonService() {
        super();
        personDao = PersonDao.getInstance();
    }

    public static synchronized PersonService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PersonService();
        }
        return INSTANCE;
    }


    public ArrayList<Person> listar() throws UnknownHostException {
        ArrayList<Person> persons = new ArrayList<Person>();
        persons = personDao.listar();

        return persons;
    }

    public Person obtenerPorId(int id) throws UnknownHostException {

        return personDao.obtenerPorId(id);
    }

    public boolean eliminar(int id) throws  UnknownHostException {
        boolean resul = false;
        if(personDao.eliminar(id)){
            resul = true;
        }

        return resul;
    }

    public boolean crear(Person persona) throws  UnknownHostException {
        boolean resul = false;
        if(personDao.crear(persona)){
            resul = true;
        }

        return resul;
    }

    public boolean modificar(int id,Person persona) throws  UnknownHostException {
        boolean resul = false;
        if(personDao.modificar(id,persona)){
            resul = true;
        }

        return resul;
    }
}
