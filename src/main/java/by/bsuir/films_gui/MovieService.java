package by.bsuir.films_gui;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class MovieService {
    private static final String API_URL = "http://localhost:8080/api/movies";
    private final ObjectMapper mapper = new ObjectMapper();
    private final CloseableHttpClient httpClient;

    public MovieService() {
        this.httpClient = HttpClients.createDefault();
    }

    public MovieService(CloseableHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public List<Movie> getAllMovies() throws IOException {
        try (CloseableHttpResponse response = httpClient.execute(new HttpGet(API_URL))) {
            String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            return Arrays.asList(mapper.readValue(responseBody, Movie[].class));
        }
    }

    public Movie addMovie(Movie movie) throws IOException {
        HttpPost request = new HttpPost(API_URL);
        request.setHeader("Content-Type", "application/json; charset=UTF-8");
        request.setEntity(new StringEntity(mapper.writeValueAsString(movie), StandardCharsets.UTF_8));
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            return mapper.readValue(responseBody, Movie.class);
        }
    }

    public Movie updateMovie(int id, Movie movie) throws IOException {
        HttpPut request = new HttpPut(API_URL + "/" + id);
        request.setHeader("Content-Type", "application/json; charset=UTF-8");
        request.setEntity(new StringEntity(mapper.writeValueAsString(movie), StandardCharsets.UTF_8));
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            return mapper.readValue(responseBody, Movie.class);
        }
    }

    public void deleteMovie(int id) throws IOException {
        HttpDelete request = new HttpDelete(API_URL + "/" + id);
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            // Нет возвращаемого значения
        }
    }
}