package nl.han.oose.persistence;

public interface TokenDAO {
    void insertToken(String token, String user);

    String validateToken(String token);
}
