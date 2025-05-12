package by.bsuir.films_gui;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MovieServiceTest {
    @Mock
    private CloseableHttpClient httpClient;

    @Mock
    private CloseableHttpResponse httpResponse;

    @Mock
    private HttpEntity httpEntity;

    @Mock
    private StatusLine statusLine;

    private MovieService movieService;
    private ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        movieService = new MovieService(httpClient);
        mapper = new ObjectMapper();
    }

    @Test
    void testGetAllMovies() throws IOException {
        // Prepare test data
        Movie[] movies = {
                new Movie(1, "Интерстеллар", "Фантастический фильм", "фантастика", 2014),
                new Movie(2, "Побег из Шоушенка", "Драма о надежде", "драма", 1994)
        };
        String jsonResponse = mapper.writeValueAsString(movies);

        // Mock the HTTP GET request
        HttpGet httpGet = new HttpGet("http://localhost:8080/api/movies");
        when(httpClient.execute(any(HttpGet.class))).thenReturn(httpResponse);
        when(httpResponse.getEntity()).thenReturn(httpEntity);
        when(httpEntity.getContent()).thenReturn(new ByteArrayInputStream(jsonResponse.getBytes(StandardCharsets.UTF_8)));

        // Execute the method
        List<Movie> result = movieService.getAllMovies();

        // Verify
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Интерстеллар", result.get(0).getTitle());
        assertEquals("Побег из Шоушенка", result.get(1).getTitle());
        verify(httpClient).execute(any(HttpGet.class));
    }

    @Test
    void testAddMovie() throws IOException {
        // Prepare test data
        Movie movieToAdd = new Movie(0, "Остров проклятых", "Триллер о тайнах", "триллер", 2010);
        Movie expectedMovie = new Movie(3, "Остров проклятых", "Триллер о тайнах", "триллер", 2010);
        String jsonResponse = mapper.writeValueAsString(expectedMovie);
        System.out.println("JSON Response: " + jsonResponse); // Отладка

        // Mock the HTTP POST request
        HttpPost httpPost = new HttpPost("http://localhost:8080/api/movies");
        when(httpClient.execute(any(HttpPost.class))).thenReturn(httpResponse);
        when(httpResponse.getEntity()).thenReturn(httpEntity);
        when(httpEntity.getContent()).thenReturn(new ByteArrayInputStream(jsonResponse.getBytes(StandardCharsets.UTF_8)));

        // Execute the method
        Movie result = movieService.addMovie(movieToAdd);
        System.out.println("Result Title: " + result.getTitle()); // Отладка

        // Verify
        assertNotNull(result);
        assertEquals(3, result.getMovieId());
        assertEquals("Остров проклятых", result.getTitle());
        verify(httpClient).execute(any(HttpPost.class));
    }

    @Test
    void testUpdateMovie() throws IOException {
        // Prepare test data
        int movieId = 1;
        Movie movieToUpdate = new Movie(1, "Интерстеллар Обновленный", "Обновленный фантастический фильм", "фантастика", 2014);
        String jsonRequest = mapper.writeValueAsString(movieToUpdate);
        String jsonResponse = mapper.writeValueAsString(movieToUpdate);

        // Mock the HTTP PUT request
        HttpPut httpPut = new HttpPut("http://localhost:8080/api/movies/" + movieId);
        when(httpClient.execute(any(HttpPut.class))).thenReturn(httpResponse);
        when(httpResponse.getEntity()).thenReturn(httpEntity);
        when(httpEntity.getContent()).thenReturn(new ByteArrayInputStream(jsonResponse.getBytes(StandardCharsets.UTF_8)));

        // Execute the method
        Movie result = movieService.updateMovie(movieId, movieToUpdate);

        // Verify
        assertNotNull(result);
        assertEquals("Интерстеллар Обновленный", result.getTitle());
        assertEquals("фантастика", result.getGenre());
        verify(httpClient).execute(any(HttpPut.class));
    }

    @Test
    void testDeleteMovie() throws IOException {
        // Prepare test data
        int movieId = 1;

        // Mock the HTTP DELETE request
        HttpDelete httpDelete = new HttpDelete("http://localhost:8080/api/movies/" + movieId);
        when(httpClient.execute(any(HttpDelete.class))).thenReturn(httpResponse);

        // Execute the method
        assertDoesNotThrow(() -> movieService.deleteMovie(movieId));

        // Verify
        verify(httpClient).execute(any(HttpDelete.class));
    }

    @Test
    void testGetAllMovies_EmptyList() throws IOException {
        // Prepare test data
        Movie[] movies = {};
        String jsonResponse = mapper.writeValueAsString(movies);

        // Mock the HTTP GET request
        HttpGet httpGet = new HttpGet("http://localhost:8080/api/movies");
        when(httpClient.execute(any(HttpGet.class))).thenReturn(httpResponse);
        when(httpResponse.getEntity()).thenReturn(httpEntity);
        when(httpEntity.getContent()).thenReturn(new ByteArrayInputStream(jsonResponse.getBytes(StandardCharsets.UTF_8)));

        // Execute the method
        List<Movie> result = movieService.getAllMovies();

        // Verify
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

}