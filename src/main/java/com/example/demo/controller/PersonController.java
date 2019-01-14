package com.example.demo.controller;

import com.example.demo.pojo.Comment;
import com.example.demo.pojo.Person;
import com.example.demo.pojo.ResponseMensaje;
import com.example.demo.service.CommentService;
import com.example.demo.service.PersonService;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;
import java.util.ArrayList;

import static org.springframework.hateoas.core.DummyInvocationUtils.methodOn;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@Controller
@CrossOrigin(origins = "*")
@RequestMapping("/publicaciones/person")
public class PersonController {

    private static ArrayList<Person> people;
    private static ArrayList<Comment> comments;
    private static PersonService servicioPerson = null;
    private static CommentService servicioComment = null;

    public PersonController() {
        super();
        servicioPerson = PersonService.getInstance();
        servicioComment = CommentService.getInstance();
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Object> listAll() {

        ResponseEntity<Object> response = new ResponseEntity<>(people, HttpStatus.INTERNAL_SERVER_ERROR);
        ResponseMensaje rm = new ResponseMensaje();
        try {

            people = servicioPerson.listar();
            Person person = new Person();

            if (people.size() > 0) {
                System.out.println("*************Pasamos por PersonController-get*************");
                for (Person p : people) {

                    Link selfLink = linkTo(PersonController.class).slash(p.getPersonId()).withSelfRel();
                    p.add(selfLink);

                    Link familyLink = linkTo(methodOn(FamilyController.class).detail(p.getFamilyId())).withRel("Familia");
                    p.add(familyLink);

                    Link peopleLink = linkTo(methodOn(PersonController.class).listAll()).withRel("Listado personas");
                    p.add(peopleLink);

                    comments = servicioComment.obtenerTodosPorAutor(p.getPersonId());
                    if (!comments.isEmpty()) {
                        for (Comment c : comments) {

                            Link commentLink = linkTo(CommentController.class).slash(c.getCommentId()).withRel("Comentarios");
                            p.add(commentLink);
                        }
                    }
                }

                response = new ResponseEntity<>(people, HttpStatus.OK);
            } else {
                response = new ResponseEntity<>(people, HttpStatus.CONFLICT);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;

    }


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Object> detail(@PathVariable int id) {
        Person persona;

        ResponseEntity<Object> response = new ResponseEntity<>(people, HttpStatus.INTERNAL_SERVER_ERROR);
        ResponseMensaje rm = new ResponseMensaje();
        try {

            persona = servicioPerson.obtenerPorId(id);
            if (persona != null) {

                Link selfLink = linkTo(PersonController.class).slash(persona.getPersonId()).withSelfRel();
                persona.add(selfLink);
                Link familyLink = linkTo(methodOn(FamilyController.class).detail(persona.getFamilyId())).withRel("Familia");
                persona.add(familyLink);
                Link peopleLink = linkTo(methodOn(PersonController.class).listAll()).withRel("Listado personas");
                persona.add(peopleLink);
                response = new ResponseEntity<>(persona, HttpStatus.FOUND);
            } else {
                response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


        System.out.println(id);


        return response;

    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> delete(@PathVariable int id) {

        ResponseEntity<Object> response = new ResponseEntity<>(people, HttpStatus.INTERNAL_SERVER_ERROR);
        ResponseMensaje rm = new ResponseMensaje();
        try {

            if (servicioPerson.eliminar(id)) {

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

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Object> crear(@RequestBody Person persona) {
        ResponseEntity<Object> response = new ResponseEntity<>(people, HttpStatus.INTERNAL_SERVER_ERROR);

        try {

            if (servicioPerson.crear(persona)) {
                int id = persona.getPersonId();
                persona = servicioPerson.obtenerPorId(id);
                Link selfLink = linkTo(PersonController.class).slash(persona.getPersonId()).withSelfRel();
                persona.add(selfLink);
                response = new ResponseEntity<>(persona, HttpStatus.CREATED);
            } else {
                response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

        } catch (UnknownHostException e) {
            e.printStackTrace();

        }
        return response;
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Object> moodificar(@PathVariable int id, @RequestBody Person p) {

        ResponseEntity<Object> response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        try {

            if (servicioPerson.modificar(id, p)) {
                Link selfLink = linkTo(PersonController.class).slash(p.getPersonId()).withSelfRel();
                p.add(selfLink);
                response = new ResponseEntity<>(HttpStatus.OK);

            } else {


                response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

}



