package ru.kryuchkova.spring.dao;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.kryuchkova.spring.models.Movie;

import java.io.IOException;
import java.sql.*;
import java.util.List;

import static ru.kryuchkova.spring.config.JsonReader.readJsonFromUrl;

@Component
public class MovieDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MovieDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Movie> index() {
        return jdbcTemplate.query("SELECT * FROM Movies", new BeanPropertyRowMapper<>(Movie.class));
    }


    public Movie show(int id) {
        return jdbcTemplate.query("SELECT * FROM Movies WHERE id=?", new Object[]{id}, new BeanPropertyRowMapper<>(Movie.class))
                .stream().findAny().orElse(null);
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM Movies WHERE id=?", id);
    }

    public void save(int id, Movie movie) throws IOException {
        final String URL = "https://api.kinopoisk.cloud/movies/";
        final String TOKEN = "/token/6d9e14a83eed656d2de8c2d007cc6f78";

        String fullUrl = URL + id + TOKEN;

        JSONObject json = readJsonFromUrl(fullUrl);
        System.out.println(json.toString());
        String name = json.get("title").toString();
        int year = (int) json.get("year");
        String posterUrl = json.get("poster").toString();
        String genres = json.get("genres").toString();
        String countries = json.get("countries").toString();
        System.out.println(name + genres + year);
        jdbcTemplate.update("INSERT INTO Movies VALUES(?, ?, ?, ?, ?, ?)", id, name,
                posterUrl, year, genres, countries);
    }
}
