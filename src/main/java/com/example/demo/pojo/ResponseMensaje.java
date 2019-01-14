package com.example.demo.pojo;


import java.util.Arrays;

public class ResponseMensaje {
    //Atributes
    private String mensaje;
    private String[] errores;

    //Constructors

    /**
     * Class constructor.
     */
    public ResponseMensaje() {
        super();
        this.mensaje = "Ha ocurrido un error.";
    }

    //Getters & Setters

    /**
     * Gets the error messages.
     *
     * @return String Array with the error messages.
     */
    public String[] getErrores() {
        return errores;
    }

    /**
     * Sets the String Array the specified messages.
     *
     * @param errores String Array error messages.
     */
    public void setErrores(String[] errores) {
        this.errores = errores;
    }

    /**
     * Gets the message to show.
     *
     * @return
     */
    public String getMensaje() {
        return mensaje;
    }

    /**
     * Sets the message to show.
     *
     * @param mensaje String message to show.
     */
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    //Methods

    /**
     * Shows entire ResponseMensaje object.
     *
     * @return String description of the ResponseMensaje object.
     */
    @Override
    public String toString() {
        return "ResponseMensaje [mensaje=" + mensaje + ", errores=" + Arrays.toString(errores) + "]";
    }

}
