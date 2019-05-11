package nl.han.oose.persistence;

import nl.han.oose.service.SpotitubeLoginException;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Default
public class TokenDAOImpl implements TokenDAO {

    private ConnectionFactory connectionFactory;

    @Inject
    public TokenDAOImpl(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Override
    public void insertToken(String token, String user) {
        try (
                Connection connection = connectionFactory.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "insert into usertoken\n" +
                                "values (?, now() + interval 2 day, ?)");
        ) {
            preparedStatement.setString(1, token);
            preparedStatement.setString(2, user);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SpotitubePersistenceException(e);
        }
    }

    @Override
    public String validateToken(String token) {
        try (
                Connection connection = connectionFactory.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "SELECT * FROM usertoken WHERE token=? and expiredate >= now()");
        ) {
            preparedStatement.setString(1, token);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return token;
            } else {
                throw new SpotitubeLoginException("Failed to validate token");
            }
        } catch (SQLException e) {
            throw new SpotitubeLoginException("Failed to validate token");
        }
    }

}