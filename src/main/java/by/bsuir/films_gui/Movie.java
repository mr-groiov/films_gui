package by.bsuir.films_gui;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Movie {
    @JsonProperty("movieId")
    private int movieId;

    @JsonProperty("title")
    private String title;

    @JsonProperty("description")
    private String description;

    @JsonProperty("genre")
    private String genre;

    @JsonProperty("year")
    private int year;

    // Конструкторы
    public Movie() {
    }

    public Movie(int movieId, String title, String description, String genre, int year) {
        this.movieId = movieId;
        this.title = title;
        this.description = description;
        this.genre = genre;
        this.year = year;
    }

    // Геттеры и сеттеры
    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}