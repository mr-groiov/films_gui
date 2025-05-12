package by.bsuir.films_gui;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MovieServiceTest {

    @Mock
    private CloseableHttpClient httpClient;

    @Mock
    private CloseableHttpResponse httpResponse;

    @Mock
    private HttpEntity httpEntity;

    @Mock
    private StatusLine statusLine;

    private MovieService movieService;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        movieService = new MovieService();
        objectMapper = new ObjectMapper();
        // Заменяем реальный HTTP-клиент моканным
        movieService = new MovieService() {
            @Override
            public List<Movie> getAllMovies() throws IOException {
                when(httpClient.execute(any(HttpGet.class))).thenReturn(httpResponse);
                when(httpResponse.getEntity()).thenReturn(httpEntity);
                return super.getAllMovies();
            }

            @Override
            public Movie addMovie(Movie movie) throws IOException {
                when(httpClient.execute(any(HttpPost.class))).thenReturn(httpResponse);
                when(httpResponse.getEntity()).thenReturn(httpEntity);
                return super.addMovie(movie);
            }

            @Override
            public Movie updateMovie(int id, Movie movie) throws IOException {
                when(httpClient.execute(any(HttpPut.class))).thenReturn(httpResponse);
                when(httpResponse.getEntity()).thenReturn(httpEntity);
                return super.updateMovie(id, movie);
            }

            @Override
            public void deleteMovie(int id) throws IOException {
                when(httpClient.execute(any(HttpDelete.class))).thenReturn(httpResponse);
                super.deleteMovie(id);
            }
        };
    }

    @Test
    void testGetAllMovies() throws IOException {
        // Arrange
        Movie[] movies = {
                new Movie(1, "Интерстеллар", "Фантастический фильм", "фантастика", 2014),
                new Movie(2, "Побег из Шоушенка", "Драма", "драма", 1994)
        };
        String jsonResponse = objectMapper.writeValueAsString(movies);
        when(httpEntity.getContent()).thenReturn(new java.io.ByteArrayInputStream(jsonResponse.getBytes()));
        when(EntityUtils.toString(httpEntity)).thenReturn(jsonResponse);

        // Act
        List<Movie> result = movieService.getAllMovies();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Интерстеллар", result.get(0).getTitle());
        assertEquals("Побег из Шоушенка", result.get(1).getTitle());
        verify(httpClient, times(1)).execute(any(HttpGet.class));
    }

    @Test
    void testAddMovie() throws IOException {
        // Arrange
        Movie movie = new Movie(3, "Остров проклятых", "Триллер", "триллер", 2009);
        String jsonResponse = objectMapper.writeValueAsString(movie);
        when(httpEntity.getContent()).thenReturn(new java.io.ByteArrayInputStream(jsonResponse.getBytes()));
        when(EntityUtils.toString(httpEntity)).thenReturn(jsonResponse);

        // Act
        Movie result = movieService.addMovie(movie);

        // Assert
        assertNotNull(result);
        assertEquals("Остров проклятых", result.getTitle());
        assertEquals("триллер", result.getGenre());
        verify(httpClient, times(1)).execute(any(HttpPost.class));
    }

    @Test
    void testUpdateMovie() throws IOException {
        // Arrange
        Movie movie = new Movie(1, "Интерстеллар Обновленный", "Обновленный фильм", "фантастика", 2014);
        String jsonResponse = objectMapper.writeValueAsString(movie);
        when(httpEntity.getContent()).thenReturn(new java.io.ByteArrayInputStream(jsonResponse.getBytes()));
        when(EntityUtils.toString(httpEntity)).thenReturn(jsonResponse);

        // Act
        Movie result = movieService.updateMovie(1, movie);

        // Assert
        assertNotNull(result);
        assertEquals("Интерстеллар Обновленный", result.getTitle());
        verify(httpClient, times(1)).execute(any(HttpPut.class));
    }

    @Test
    void testDeleteMovie() throws IOException {
        // Act
        movieService.deleteMovie(1);

        // Assert
        verify(httpClient, times(1)).execute(any(HttpDelete.class));
    }
}