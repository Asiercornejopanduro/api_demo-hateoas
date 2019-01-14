package com.example.demo.pojo;


import java.util.Arrays;

public class ResponseMensaje {
    //Atributes
    private String mensaje;
    private String[] errores;

    //Constructors
    public ResponseMensaje() {
        super();
        this.mensaje = "Ha ocurrido un error.";
    }

    //Getters & Setters
    public String[] getErrores() {
        return errores;
    }

    public void setErrores(String[] errores) {
        this.errores = errores;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    //Methods
    @Override
    public String toString() {
        return "ResponseMensaje [mensaje=" + mensaje + ", errores=" + Arrays.toString(errores) + "]";
    }

}
