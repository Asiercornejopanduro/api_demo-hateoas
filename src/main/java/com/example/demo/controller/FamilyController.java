package com.example.demo.controller;

import com.example.demo.pojo.Family;
import com.example.demo.pojo.Person;
import com.example.demo.pojo.ResponseMensaje;
import com.example.demo.service.FamilyService;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.net.UnknownHostException;
import java.util.ArrayList;

import static org.springframework.hateoas.core.DummyInvocationUtils.methodOn;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

/**
 * Controller to manage the familias collection.
 */
@Controller
@RequestMapping(value = "/publicaciones/family")
public class FamilyController {
    private static ArrayList<Family> familias;
    private FamilyService serviciofamily;

    public FamilyController() {
        super();
        serviciofamily = FamilyService.getInstance();
    }

    /**
     * Gets a list of the families in JSON format, on the familias collection.
     *
     * @return ResponseEntity object with all the families documents on the collection, and the http status code,Ok if the operation was succesfully or CONFLICT if not.
     */
    @RequestMapping(method = RequestMethod.GET, produces = "application/hal+json")

    public ResponseEntity<Object> listAll() {

        ResponseEntity<Object> response = new ResponseEntity<>(familias, HttpStatus.INTERNAL_SERVER_ERROR);
        ResponseMensaje rm = new ResponseMensaje();
        try {
            familias = serviciofamily.listar();

            if (familias.size() > 0) {
                System.out.println("*************Pasamos por PersonController-get*************");
                for (Family f : familias) {
                    Link selfLink = linkTo(FamilyController.class).slash(f.getFamilyId()).withSelfRel();
                    Link familiesLink = linkTo(methodOn(FamilyController.class).listAll()).withRel("Listado Familias");
                    f.add(selfLink);
                    f.add(familiesLink);

                    for (Person p : f.getPersonas()) {
                        Link personfLink = linkTo(PersonController.class).slash(p.getPersonId()).withSelfRel();
                        p.add(personfLink);
                    }
                }
                response = new ResponseEntity<>(familias, HttpStatus.OK);
            } else {
                response = new ResponseEntity<>(familias, HttpStatus.CONFLICT);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;

    }

    /**
     * Gets the Family object in JSON format, specified by the id parameter.
     *
     * @param id Family id wich indicates the location of the resource.
     * @return ResponseEntity object with the requested resource and the http status code, FOUND if exist and NOT_FOUND if not, and the requested resource.
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/hal+json")
    public ResponseEntity<Object> detail(@PathVariable int id) {
        Family family;

        ResponseEntity<Object> response = new ResponseEntity<>(familias, HttpStatus.INTERNAL_SERVER_ERROR);
        ResponseMensaje rm = new ResponseMensaje();
        try {

            family = serviciofamily.obtenerPorId(id);
            if (family != null) {

                Link selfLink = linkTo(FamilyController.class).slash(family.getFamilyId()).withSelfRel();
                family.add(selfLink);


                for (Person p : family.getPersonas()) {
                    Link personfLink = linkTo(PersonController.class).slash(p.getPersonId()).withSelfRel();
                    p.add(personfLink);
                }
                response = new ResponseEntity<>(family, HttpStatus.FOUND);
            } else {
                response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


        System.out.println(id);


        return response;

    }

    /**
     * Insert a new document on the "familias" collection.
     *
     * @param familia Family object in JSON format, wich want to insert.
     * @return ResponseEntity object with the http status code, CREATED if the operation was succesfully,BAD_REQUEST if not, and the inserted object.
     */
    @RequestMapping(method = RequestMethod.POST, produces = "application/hal+json")
    public ResponseEntity<Object> crear(@RequestBody Family familia) {
        ResponseEntity<Object> response = new ResponseEntity<>(familias, HttpStatus.INTERNAL_SERVER_ERROR);

        try {

            if (serviciofamily.crear(familia)) {
                int id = familia.getFamilyId();
                familia = serviciofamily.obtenerPorId(id);
                Link selfLink = linkTo(PersonController.class).slash(familia.getFamilyId()).withSelfRel();
                familia.add(selfLink);
                response = new ResponseEntity<>(familia, HttpStatus.CREATED);
            } else {
                response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

        } catch (UnknownHostException e) {
            e.printStackTrace();

        }
        return response;
    }

    /**
     * Delete the document specified by the id parameter.
     *
     * @param id family id wich indicates the location of the resource to delete.
     * @return ResponseEntity object with the http status code, Ok if the operation was succesfully and NOT_FOUND if not.
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> delete(@PathVariable int id) {

        ResponseEntity<Object> response = new ResponseEntity<>(familias, HttpStatus.INTERNAL_SERVER_ERROR);
        ResponseMensaje rm = new ResponseMensaje();
        try {

            if (serviciofamily.eliminar(id)) {

                response = new ResponseEntity<>(HttpStatus.OK);

            } else {
                rm.setMensaje("Error Eliminando persona");

                response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    /**
     * Update the document on the "familias" collection.
     *
     * @param id      Family id wich want to update.
     * @param familia Family object with the updated values.
     * @return ResponseEntity object with the http status code, Ok if the operation was succesfully,BAD_REQUEST if not, and the family object in JSON format.
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Object> moodificar(@PathVariable int id, @RequestBody Family familia) {

        ResponseEntity<Object> response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        try {

            if (serviciofamily.modificar(id, familia)) {
                familia = serviciofamily.obtenerPorId(id);
                Link selfLink = linkTo(PersonController.class).slash(familia.getFamilyId()).withSelfRel();
                familia.add(selfLink);
                response = new ResponseEntity<>(familia, HttpStatus.OK);

            } else {


                response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

}
