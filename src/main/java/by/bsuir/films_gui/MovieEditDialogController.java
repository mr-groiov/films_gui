package by.bsuir.films_gui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class MovieEditDialogController {
    @FXML
    private TextField titleField;
    @FXML
    private TextArea descriptionField;
    @FXML
    private TextField genreField;
    @FXML
    private TextField yearField;

    private Stage dialogStage;
    private Movie movie;
    private boolean okClicked = false;

    @FXML
    private void initialize() {
        // Инициализация, если нужно
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;

        // Настройка обработчиков для кнопок DialogPane
        DialogPane dialogPane = (DialogPane) dialogStage.getScene().getRoot();
        Button okButton = (Button) dialogPane.lookupButton(dialogPane.getButtonTypes().stream()
                .filter(bt -> bt.getText().equals("OK"))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("OK button not found")));
        Button cancelButton = (Button) dialogPane.lookupButton(dialogPane.getButtonTypes().stream()
                .filter(bt -> bt.getText().equals("Отмена"))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Cancel button not found")));

        okButton.setOnAction(event -> handleOk());
        cancelButton.setOnAction(event -> handleCancel());
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
        titleField.setText(movie.getTitle());
        descriptionField.setText(movie.getDescription() != null ? movie.getDescription() : "");
        genreField.setText(movie.getGenre());
        yearField.setText(movie.getYear() == 0 ? "" : String.valueOf(movie.getYear()));
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    @FXML
    private void handleOk() {
        if (isInputValid()) {
            movie.setTitle(titleField.getText());
            movie.setDescription(descriptionField.getText());
            movie.setGenre(genreField.getText());
            movie.setYear(Integer.parseInt(yearField.getText()));
            okClicked = true;
            dialogStage.close();
        }
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    private boolean isInputValid() {
        String errorMessage = "";

        if (titleField.getText() == null || titleField.getText().trim().isEmpty()) {
            errorMessage += "Название не может быть пустым!\n";
        }
        if (descriptionField.getText() == null || descriptionField.getText().trim().isEmpty()) {
            errorMessage += "Описание не может быть пустым!\n";
        }
        if (genreField.getText() == null || genreField.getText().trim().isEmpty()) {
            errorMessage += "Жанр не может быть пустым!\n";
        }
        if (yearField.getText() == null || yearField.getText().trim().isEmpty()) {
            errorMessage += "Год не может быть пустым!\n";
        } else {
            try {
                int year = Integer.parseInt(yearField.getText());
                if (year < 1888 || year > 2100) {
                    errorMessage += "Год должен быть между 1888 и 2100!\n";
                }
            } catch (NumberFormatException e) {
                errorMessage += "Год должен быть числом!\n";
            }
        }

        if (errorMessage.isEmpty()) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Пожалуйста, исправьте ошибки");
            alert.setContentText(errorMessage);
            alert.showAndWait();
            return false;
        }
    }
}