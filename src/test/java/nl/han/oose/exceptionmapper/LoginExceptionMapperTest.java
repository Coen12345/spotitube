package nl.han.oose.exceptionmapper;

import nl.han.oose.dto.ErrorDTO;
import nl.han.oose.service.SpotitubeLoginException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;

import static org.junit.jupiter.api.Assertions.*;

class LoginExceptionMapperTest {

    private LoginExceptionMapper sut;

    @BeforeEach
    void setUp() {
        sut = new LoginExceptionMapper();
    }

    @Test
    void loginReturnsUnauthorizedExceptionAndGivenMessage() {
        Response actualResult = sut.toResponse(new SpotitubeLoginException("Fout met inloggen."));
        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), actualResult.getStatus());

        ErrorDTO actualError = (ErrorDTO) actualResult.getEntity();
        assertEquals("Fout met inloggen.", actualError.getMessage());
    }
}