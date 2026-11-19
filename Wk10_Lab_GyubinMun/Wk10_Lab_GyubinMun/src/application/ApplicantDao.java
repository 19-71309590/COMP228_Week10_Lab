package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Date;

public class ApplicantDao {
    public int saveApplicant(Applicant applicant) throws SQLException {
        String query = "INSERT INTO applications "
            + "(full_name, current_address, contact_number, email, education, gender, "
            + "date_available, desired_position, desired_salary, authorized_to_work, "
            + "has_relatives, explanation, signature) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = Database.getDBConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            int counter = 1;
            statement.setString(counter++, applicant.getFullName());
            statement.setString(counter++, applicant.getCurrentAddress());
            statement.setString(counter++, applicant.getContactNumber());
            statement.setString(counter++, applicant.getEmail());
            statement.setString(counter++, applicant.getEducation());
            statement.setString(counter++, applicant.getGender());
            statement.setDate(counter++, Date.valueOf(applicant.getDateAvailable()));
            statement.setString(counter++, applicant.getDesiredPosition());
            statement.setBigDecimal(counter++, applicant.getDesiredSalary());
            statement.setString(counter++, applicant.getAuthorizedToWork());
            statement.setString(counter++, applicant.getHasRelatives());
            statement.setString(counter++, applicant.getExplanation());
            statement.setString(counter++, applicant.getSignature());

            return statement.executeUpdate();
        }
    }
}
