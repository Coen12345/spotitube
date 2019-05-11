package nl.han.oose.persistence;

import nl.han.oose.dto.UserDTO;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Default
public class UserDAOImpl implements UserDAO {

    private ConnectionFactory connectionFactory;

    @Inject
    public UserDAOImpl(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Override
    public UserDTO getUser(String user, String password) {
        UserDTO foundUser = null;
        try (
                Connection connection = connectionFactory.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "SELECT * FROM ACCOUNT WHERE user=? AND password=?");
        ) {
            preparedStatement.setString(1, user);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                foundUser = new UserDTO();
                foundUser.setName(resultSet.getString("name"));
                foundUser.setUser(user);
                foundUser.setPassword(password);
            }
        } catch (SQLException e) {
            throw new SpotitubePersistenceException(e);
        }
        return foundUser;
    }
}
