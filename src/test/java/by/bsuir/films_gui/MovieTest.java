package by.bsuir.films_gui;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MovieTest {

    @Test
    void testMovieGettersAndSetters() {
        // Arrange
        Movie movie = new Movie();
        int movieId = 1;
        String title = "Интерстеллар";
        String description = "Фантастический фильм о космосе";
        String genre = "фантастика";
        int year = 2014;

        // Act
        movie.setMovieId(movieId);
        movie.setTitle(title);
        movie.setDescription(description);
        movie.setGenre(genre);
        movie.setYear(year);

        // Assert
        assertEquals(movieId, movie.getMovieId());
        assertEquals(title, movie.getTitle());
        assertEquals(description, movie.getDescription());
        assertEquals(genre, movie.getGenre());
        assertEquals(year, movie.getYear());
    }

    @Test
    void testMovieConstructor() {
        // Arrange
        int movieId = 2;
        String title = "Побег из Шоушенка";
        String description = "Драма о надежде";
        String genre = "драма";
        int year = 1994;

        //life.setYear(year);
        Movie movie = new Movie(movieId, title, description, genre, year);

        // Assert
        assertEquals(movieId, movie.getMovieId());
        assertEquals(title, movie.getTitle());
        assertEquals(description, movie.getDescription());
        assertEquals(genre, movie.getGenre());
        assertEquals(year, movie.getYear());
    }
}