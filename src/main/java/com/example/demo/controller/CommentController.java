package com.example.demo.controller;

import com.example.demo.pojo.Comment;
import com.example.demo.pojo.Family;
import com.example.demo.pojo.Person;
import com.example.demo.pojo.ResponseMensaje;
import com.example.demo.service.CommentService;
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
 * Controller to manage comments
 */
@Controller
@RequestMapping("/publicaciones/comments")
public class CommentController {

    private static ArrayList<Comment> comments;
    private static CommentService servicioComment = null;

    public CommentController() {
        super();
        servicioComment = CommentService.getInstance();
    }

    /**
     * Gets a list of the comments in JSON format, on the comments collection.
     *
     * @return ResponseEntity object with all the comments documents on the collection, and the http status code,Ok if the operation was succesfully or CONFLICT if not.
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Object> listAll() {

        ResponseEntity<Object> response = new ResponseEntity<>(comments, HttpStatus.INTERNAL_SERVER_ERROR);
        ResponseMensaje rm = new ResponseMensaje();
        try {
            comments = servicioComment.listar();


            if (comments.size() > 0) {
                System.out.println("*************Pasamos por PersonController-get*************");
                for (Comment c : comments) {
                    Person p = c.getPersona();
                    Family f = c.getFamilia();
                    Link selfLink = linkTo(CommentController.class).slash(c.getCommentId()).withSelfRel();
                    c.add(selfLink);
                    Link personLink = linkTo(PersonController.class).slash(c.getPersona().getPersonId()).withRel("Autor");
                    c.add(personLink);
                    Link familyLink = linkTo(methodOn(FamilyController.class).detail(f.getFamilyId())).withRel("Familia");
                    c.add(familyLink);
                    Link commentsLink = linkTo(methodOn(CommentController.class).listAll()).withRel("Listado Comentarios");
                    c.add(commentsLink);

                }


                response = new ResponseEntity<>(comments, HttpStatus.OK);
            } else {
                response = new ResponseEntity<>(comments, HttpStatus.CONFLICT);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;

    }

    /**
     * Gets the Comments object in JSON format, specified by the id parameter.
     *
     * @param id Comment id wich indicates the location of the resource.
     * @return ResponseEntity object with the requested resource and the http status code, FOUND if exist and NOT_FOUND if not.
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Object> detail(@PathVariable int id) {
        Comment comment;

        ResponseEntity<Object> response = new ResponseEntity<>(comments, HttpStatus.INTERNAL_SERVER_ERROR);
        ResponseMensaje rm = new ResponseMensaje();
        try {

            comment = servicioComment.obtenerPorId(id);
            if (comment != null) {
                response = new ResponseEntity<>(setLinks(comment), HttpStatus.FOUND);
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
     * Delete the document specified by the id parameter.
     *
     * @param id Comment id wich indicates the location of the resource to delete.
     * @return ResponseEntity object with the http status code, Ok if the operation was succesfully and NOT_FOUND if not.
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> delete(@PathVariable int id) {

        ResponseEntity<Object> response = new ResponseEntity<>(comments, HttpStatus.INTERNAL_SERVER_ERROR);
        ResponseMensaje rm = new ResponseMensaje();
        try {

            if (servicioComment.eliminar(id)) {

                response = new ResponseEntity<>(HttpStatus.OK);

            } else {
                rm.setMensaje("Error Eliminando Comment");

                response = new ResponseEntity<>(rm, HttpStatus.NOT_FOUND);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    /**
     * Insert a new document on the "comments" collection.
     *
     * @param comment Comment object in JSON format, wich want to insert.
     * @return ResponseEntity object with the http status code, CREATED if the operation was succesfully and BAD_REQUEST if not.
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Object> crear(@RequestBody Comment comment) {
        ResponseEntity<Object> response = new ResponseEntity<>(comments, HttpStatus.INTERNAL_SERVER_ERROR);

        try {
            comments = servicioComment.listar();
            if (servicioComment.crear(comment)) {
                Comment newCommen = servicioComment.obtenerPorId(comment.getCommentId());

                response = new ResponseEntity<>(newCommen, HttpStatus.CREATED);
            } else {
                response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

        } catch (UnknownHostException e) {
            e.printStackTrace();

        }
        return response;
    }

    /**
     * Update the document on the "comments" collection.
     *
     * @param id      Comment id wich want to update.
     * @param comment Comment object with the updated values.
     * @return ResponseEntity object with the http status code, Ok if the operation was succesfully and BAD_REQUEST if not and the updated document JSON formatted.
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Object> moodificar(@PathVariable int id, @RequestBody Comment comment) {

        ResponseEntity<Object> response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        try {

            if (servicioComment.modificar(id, comment)) {

                response = new ResponseEntity<>(setLinks(comment), HttpStatus.OK);

            } else {


                response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    /**
     * Adds all the link resources of the object.
     *
     * @param comment The Comment object wich want to add the links.
     * @return Comment object ipdated with the link resources.
     */
    private Comment setLinks(Comment comment) {
        Link selfLink = linkTo(CommentController.class).slash(comment.getCommentId()).withSelfRel();
        comment.add(selfLink);


        Link familyLink = linkTo(methodOn(FamilyController.class).detail(comment.getFamilia().getFamilyId())).withRel("Familia");
        comment.add(familyLink);

        Link personLink = linkTo(PersonController.class).slash(comment.getPersona().getPersonId()).withRel("Autor");
        comment.add(personLink);

        Link commentLink = linkTo(methodOn(CommentController.class).listAll()).withRel("Listado comments");
        comment.add(commentLink);
        return comment;
    }


}
