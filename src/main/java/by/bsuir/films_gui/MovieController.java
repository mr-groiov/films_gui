package by.bsuir.films_gui;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class MovieController {
    @FXML
    private TableView<Movie> movieTable;
    @FXML
    private TableColumn<Movie, Integer> idColumn;
    @FXML
    private TableColumn<Movie, String> titleColumn;
    @FXML
    private TableColumn<Movie, String> descriptionColumn;
    @FXML
    private TableColumn<Movie, String> genreColumn;
    @FXML
    private TableColumn<Movie, Integer> yearColumn;
    @FXML
    private Button addButton;
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;

    private final MovieService movieService = new MovieService();
    private final ObservableList<Movie> movieData = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        // Настройка столбцов таблицы
        idColumn.setCellValueFactory(new PropertyValueFactory<>("movieId"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));

        // Настройка пропорциональной ширины столбцов
        movieTable.widthProperty().addListener((obs, oldWidth, newWidth) -> {
            double totalWidth = newWidth.doubleValue();
            idColumn.setPrefWidth(totalWidth * 0.05); // 10%
            titleColumn.setPrefWidth(totalWidth * 0.2); // 20%
            descriptionColumn.setPrefWidth(totalWidth * 0.45); // 45%
            genreColumn.setPrefWidth(totalWidth * 0.15); // 15%
            yearColumn.setPrefWidth(totalWidth * 0.2); // 20%
        });

        // Включить перенос текста для descriptionColumn
        descriptionColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(item);
                    setWrapText(true); // Перенос текста
                }
            }
        });

        movieTable.setItems(movieData);
        loadMovies();

        // Управление активностью кнопок
        editButton.setDisable(true);
        deleteButton.setDisable(true);

        movieTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    editButton.setDisable(newValue == null);
                    deleteButton.setDisable(newValue == null);
                });
    }

    // Остальные методы без изменений
    private void loadMovies() {
        try {
            movieData.setAll(movieService.getAllMovies());
        } catch (IOException e) {
            showAlert("Ошибка", "Не удалось загрузить фильмы: " + e.getMessage());
        }
    }

    @FXML
    private void handleAddMovie() {
        Movie newMovie = new Movie();
        boolean okClicked = showMovieEditDialog(newMovie, "Добавить фильм");
        if (okClicked) {
            try {
                movieService.addMovie(newMovie);
                loadMovies();
            } catch (IOException e) {
                showAlert("Ошибка", "Не удалось добавить фильм: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleEditMovie() {
        Movie selectedMovie = movieTable.getSelectionModel().getSelectedItem();
        if (selectedMovie != null) {
            boolean okClicked = showMovieEditDialog(selectedMovie, "Редактировать фильм");
            if (okClicked) {
                try {
                    movieService.updateMovie(selectedMovie.getMovieId(), selectedMovie);
                    loadMovies();
                } catch (IOException e) {
                    showAlert("Ошибка", "Не удалось обновить фильм: " + e.getMessage());
                }
            }
        }
    }

    @FXML
    private void handleDeleteMovie() {
        Movie selectedMovie = movieTable.getSelectionModel().getSelectedItem();
        if (selectedMovie != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Подтверждение удаления");
            alert.setHeaderText("Вы уверены, что хотите удалить фильм?");
            alert.setContentText(selectedMovie.getTitle());

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    movieService.deleteMovie(selectedMovie.getMovieId());
                    loadMovies();
                } catch (IOException e) {
                    showAlert("Ошибка", "Не удалось удалить фильм: " + e.getMessage());
                }
            }
        }
    }

    private boolean showMovieEditDialog(Movie movie, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/movie-edit-dialog.fxml"));
            if (loader.getLocation() == null) {
                throw new IOException("Cannot find movie-edit-dialog.fxml");
            }
            DialogPane dialogPane = loader.load();

            MovieEditDialogController controller = loader.getController();
            controller.setMovie(movie);

            Stage dialogStage = new Stage();
            dialogStage.setTitle(title);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(movieTable.getScene().getWindow());
            dialogStage.setScene(new Scene(dialogPane));

            controller.setDialogStage(dialogStage);
            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Ошибка", "Не удалось открыть диалоговое окно: " + e.getMessage());
            return false;
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}