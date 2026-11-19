package application;

import java.math.BigDecimal;
import java.sql.SQLException;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        ApplicantDao applicantDao = new ApplicantDao();
        primaryStage.setTitle("Employment Application");
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25));

        Text scenetitle = new Text("Employment Application");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        // Company logo
        Label logoBadge = new Label("G");
        logoBadge.setMinSize(45, 45);
        logoBadge.setMaxSize(45, 45);
        logoBadge.setAlignment(Pos.CENTER);
        logoBadge.setStyle("-fx-background-color: #2f6fed;"
                + "-fx-background-radius: 30px;"
                + "-fx-text-fill: white;"
                + "-fx-font-size: 24px; -fx-font-weight: bold;");

        Label companyName = new Label("GyubinWorks");
        companyName.setStyle("-fx-font-size: 22px;"
                + "-fx-font-weight: bold; -fx-text-fill: #2f6fed;");

        HBox logoBox = new HBox(10, logoBadge, companyName);
        logoBox.setAlignment(Pos.CENTER_LEFT);

        VBox heading = new VBox(12, logoBox, scenetitle);
        grid.add(heading, 0, 0, 2, 1);
        grid.add(new Label("Personal Information"), 0, 1, 2, 1);

        TextField fullName = new TextField();
        TextField address = new TextField();
        TextField phone = new TextField();
        TextField email = new TextField();
        TextField education = new TextField();
        addRow(grid, 2, "Full Name:", fullName);
        addRow(grid, 3, "Current Address:", address);
        addRow(grid, 4, "Contact Number:", phone);
        addRow(grid, 5, "Email Address:", email);
        addRow(grid, 6, "Highest Education:", education);

        ToggleGroup gender = new ToggleGroup();
        grid.add(new Label("Gender:"), 0, 7);
        grid.add(choices(gender, "Male", "Female", "Other"), 1, 7);
        grid.add(new Label("Employment Eligibility"), 0, 8, 2, 1);

        DatePicker available = new DatePicker();
        TextField position = new TextField();
        TextField salary = new TextField();
        addRow(grid, 9, "Date Available:", available);
        addRow(grid, 10, "Desired Position:", position);
        addRow(grid, 11, "Desired Salary:", salary);

        ToggleGroup work = new ToggleGroup();
        ToggleGroup relatives = new ToggleGroup();
        grid.add(new Label("Authorized to work?"), 0, 12);
        grid.add(choices(work, "Yes", "No"), 1, 12);
        grid.add(new Label("Relatives in company?"), 0, 13);
        grid.add(choices(relatives, "Yes", "No"), 1, 13);

        TextField explanation = new TextField();
        TextField signature = new TextField();
        addRow(grid, 14, "If yes, explain:", explanation);
        addRow(grid, 15, "Applicant's Signature:", signature);

        Button submitButton = new Button("Submit");
        HBox hBox = new HBox(10, submitButton);
        hBox.setAlignment(Pos.BOTTOM_RIGHT);
        grid.add(hBox, 1, 16);

        submitButton.setOnAction(actionEvent -> {
            if (fullName.getText().isBlank() || address.getText().isBlank()
                    || phone.getText().isBlank() || email.getText().isBlank()
                    || education.getText().isBlank() || gender.getSelectedToggle() == null
                    || available.getValue() == null || position.getText().isBlank()
                    || salary.getText().isBlank() || work.getSelectedToggle() == null
                    || relatives.getSelectedToggle() == null || signature.getText().isBlank()) {
                alert("Error", "Please complete fields!", AlertType.ERROR);
                return;
            }
            if (selected(relatives).equals("Yes") && explanation.getText().isBlank()) {
                alert("Error", "Please explain further.", AlertType.ERROR);
                return;
            }
            try {
                BigDecimal amount = new BigDecimal(salary.getText().trim());
                if (amount.signum() < 0 || amount.scale() > 2
                        || amount.compareTo(new BigDecimal("9999999999.99")) > 0) {
                    alert("Error", "Invalid salary.", AlertType.ERROR);
                    return;
                }
                Applicant applicant = new Applicant();
                applicant.setFullName(fullName.getText().trim());
                applicant.setCurrentAddress(address.getText().trim());
                applicant.setContactNumber(phone.getText().trim());
                applicant.setEmail(email.getText().trim());
                applicant.setEducation(education.getText().trim());
                applicant.setGender(selected(gender));
                applicant.setDateAvailable(available.getValue());
                applicant.setDesiredPosition(position.getText().trim());
                applicant.setDesiredSalary(amount);
                applicant.setAuthorizedToWork(selected(work));
                applicant.setHasRelatives(selected(relatives));
                applicant.setExplanation(explanation.getText().trim());
                applicant.setSignature(signature.getText().trim());

                if (applicantDao.saveApplicant(applicant) > 0) {
                    alert("Submit", "Successful!", AlertType.INFORMATION);
                } else {
                    alert("Error", "Failed!", AlertType.ERROR);
                }
            } catch (NumberFormatException ex) {
                alert("Error", "Please enter a valid salary.", AlertType.ERROR);
            } catch (SQLException ex) {
                alert("Error", ex.getMessage(), AlertType.ERROR);
            }
        });
        ScrollPane scroll = new ScrollPane(grid);
        scroll.setFitToWidth(true);
        primaryStage.setScene(new Scene(scroll, 650, 780));
        primaryStage.show();
    }

    private void addRow(GridPane grid, int row, String label, Control field) {
        grid.add(new Label(label), 0, row);
        grid.add(field, 1, row);
    }

    private HBox choices(ToggleGroup group, String... labels) {
        HBox box = new HBox(10);
        for (String label : labels) {
            RadioButton button = new RadioButton(label);
            button.setToggleGroup(group);
            box.getChildren().add(button);
        }
        return box;
    }

    private String selected(ToggleGroup group) {
        return ((RadioButton) group.getSelectedToggle()).getText();
    }

    public void alert(String title, String message, AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
