package nl.han.oose.service;

import nl.han.oose.dto.TokenDTO;
import nl.han.oose.dto.UserDTO;
import nl.han.oose.persistence.TokenDAO;
import nl.han.oose.persistence.UserDAO;
import nl.han.oose.util.TokenGenerator;

import javax.enterprise.inject.Default;
import javax.inject.Inject;

@Default
public class AuthenticationServiceImpl implements AuthenticationService {

    private UserDAO userDAO;
    private TokenDAO tokenDAO;

    private TokenGenerator tokenGenerator;

    public AuthenticationServiceImpl() {
    }

    @Inject
    public AuthenticationServiceImpl(UserDAO userDAO, TokenDAO tokenDAO, TokenGenerator tokenGenerator) {
        this.userDAO = userDAO;
        this.tokenDAO = tokenDAO;
        this.tokenGenerator = tokenGenerator;
    }

    @Override
    public TokenDTO login(String username, String password) {
        UserDTO user = userDAO.getUser(username, password);
        if (user != null) {
            TokenDTO generatedToken = new TokenDTO(tokenGenerator.generateToken(), user.getUser());
            insertToken(generatedToken.getToken(), generatedToken.getUser());
            return generatedToken;
        } else {
            throw new SpotitubeLoginException("Login failed for user " + username);
        }
    }

    @Override
    public String insertToken(String token, String user) {
        tokenDAO.insertToken(token, user);
        return token;
    }

    @Override
    public String validateToken(String token) {
        return tokenDAO.validateToken(token);
    }

}
