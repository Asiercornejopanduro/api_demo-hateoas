package com.example.demo.controller;

import com.example.demo.dao.FamilyDao;
import com.example.demo.dao.PersonDao;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.core.DummyInvocationUtils.methodOn;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

/**
 * Main controller of the API
 */
@Controller
@CrossOrigin(origins = "*")
@RequestMapping("/")
public class HelloControler {

    private static FamilyDao familyDao = null;
    private static PersonDao personDao = null;
    private static List<Link> listaLinks;


    public HelloControler() {
        super();
        personDao = PersonDao.getInstance();
        familyDao = FamilyDao.getInstance();

    }

    /**
     * Shows a resume of links to the collection storaged on the database.
     *
     * @return ResponseEntity with an ArrayList of Link objects to the collections on the database.
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Object> listado() {
        ArrayList<Link> listado = new ArrayList<Link>();
        Link peopleLink = linkTo(methodOn(PersonController.class).listAll()).withRel("Personas");
        listado.add(peopleLink);

        Link familyLink = linkTo(methodOn(FamilyController.class).listAll()).withRel("Familias");
        listado.add(familyLink);

        Link commentsLink = linkTo(methodOn(CommentController.class).listAll()).withRel("Comments");
        listado.add(commentsLink);


        ResponseEntity<Object> response = new ResponseEntity<Object>(listado, HttpStatus.INTERNAL_SERVER_ERROR);
        if (!listado.isEmpty()) {
            response = new ResponseEntity<>(listado, HttpStatus.OK);
        }
        return response;
    }


}
