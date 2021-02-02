package ru.kryuchkova.spring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kryuchkova.spring.dao.MovieDAO;
import ru.kryuchkova.spring.models.Movie;

import java.io.IOException;
import java.sql.SQLException;

@Controller
@RequestMapping("/movies")
public class MovieController {

    private final MovieDAO movieDAO;

    @Autowired
    public MovieController(MovieDAO movieDAO) {
        this.movieDAO = movieDAO;
    }

    @GetMapping()
    public String index(Model model) throws SQLException {
        model.addAttribute("movies", movieDAO.index());
        return "movies/index";

    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) throws SQLException {
        model.addAttribute("movie", movieDAO.show(id));
        return "movies/show";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        movieDAO.delete(id);
        return "redirect:/movies";
    }

    @GetMapping("/new")
    public String newMovie(@ModelAttribute("movie") Movie movie) {
        return "movies/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("movie") Movie movie) throws IOException {
        movieDAO.save(movie.getId(), movie);
        return "redirect:/movies";
    }
}
