package nl.han.oose.service;

import nl.han.oose.dto.TokenDTO;
import nl.han.oose.dto.UserDTO;
import nl.han.oose.persistence.TokenDAO;
import nl.han.oose.persistence.UserDAO;
import nl.han.oose.util.TokenGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @Mock
    private UserDAO userDAOStub;

    @Mock
    private TokenDAO tokenDAOStub;

    @Mock
    private TokenGenerator tokenGeneratorStub;

    @InjectMocks
    private AuthenticationServiceImpl sut;

    private String testToken = "1234-1234-1234";

    @BeforeEach
    void setUp() {
    }

    private void mockitoValidateUserToken() {
        Mockito.when(tokenDAOStub.validateToken(testToken))
                .thenReturn(testToken);
    }

    @Test
    void loginSuccess() {
        // mockitoValidateUserToken();

        Mockito.when(userDAOStub.getUser("test", "testpass"))
                .thenReturn(new UserDTO("test", "testpass"));
        Mockito.when(tokenGeneratorStub.generateToken())
                .thenReturn(testToken);
        doNothing().when(tokenDAOStub).insertToken(testToken, "test");

        TokenDTO actualResult = sut.login("test", "testpass");

        assertEquals(testToken, actualResult.getToken());
        assertEquals("test", actualResult.getUser());
    }

    @Test
    void loginFailure() {
        Mockito.when(userDAOStub.getUser("wronguser", "wrongpass"))
                .thenReturn(null);

        SpotitubeLoginException spotitubeLoginException = assertThrows(SpotitubeLoginException.class, () -> {
            TokenDTO actualResult = sut.login("wronguser", "wrongpass");
        });
        assertEquals("Login failed for user wronguser", spotitubeLoginException.getMessage());
    }

    @Test
    void validateTokenCalled() {
        sut.validateToken(testToken);
        Mockito.verify(tokenDAOStub).validateToken(testToken);
    }

    @Test
    void authenticationServiceConstructorCreatesObject() {
        AuthenticationService authenticationService = new AuthenticationServiceImpl();
    }
}
