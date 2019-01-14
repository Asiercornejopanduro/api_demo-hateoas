package com.example.demo.service;

import com.example.demo.dao.FamilyDao;
import com.example.demo.pojo.Family;

import java.net.UnknownHostException;
import java.util.ArrayList;

public class FamilyService {
    private static FamilyService INSTANCE = null;

    private static FamilyDao familyDao = null;


    public FamilyService() {
        super();
        familyDao = FamilyDao.getInstance();
    }

    public static synchronized FamilyService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FamilyService();
        }
        return INSTANCE;
    }


    public ArrayList<Family> listar() throws UnknownHostException {
        ArrayList<Family> familias = new ArrayList<Family>();
        familias = familyDao.listar();

        return familias;
    }

    public Family obtenerPorId(int id) throws UnknownHostException {

        return familyDao.obtenerPorId(id);
    }

    public boolean eliminar(int id) throws  UnknownHostException {
        boolean resul = false;
        if(familyDao.eliminar(id)){
            resul = true;
        }

        return resul;
    }

    public boolean crear(Family familia) throws  UnknownHostException {
        boolean resul = false;
        if(familyDao.crear(familia)){
            resul = true;
        }

        return resul;
    }

    public boolean modificar(int id,Family familia) throws  UnknownHostException {
        boolean resul = false;
        if(familyDao.modificar(id,familia)){
            resul = true;
        }

        return resul;
    }
}


