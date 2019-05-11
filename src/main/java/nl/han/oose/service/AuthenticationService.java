package nl.han.oose.service;

import nl.han.oose.dto.TokenDTO;

public interface AuthenticationService {
    TokenDTO login(String username, String password);

    String insertToken(String token, String user);

    String validateToken(String token);
}
