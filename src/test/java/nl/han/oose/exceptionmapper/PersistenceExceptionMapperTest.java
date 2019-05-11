package nl.han.oose.exceptionmapper;

import nl.han.oose.dto.ErrorDTO;
import nl.han.oose.persistence.SpotitubePersistenceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class PersistenceExceptionMapperTest {

    private PersistenceExceptionMapper sut;

    @BeforeEach
    void setUp() {
        sut = new PersistenceExceptionMapper();
    }

    @Test
    void persistenceExceptionsReturnInternalServerErrorAndDatabaseConnectionErrormessage() {
        Response actualResult = sut.toResponse(new SpotitubePersistenceException(new SQLException("Ingewikkelde message")));
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), actualResult.getStatus());

        ErrorDTO actualError = (ErrorDTO) actualResult.getEntity();
        assertEquals("Database connection error. Please try again later.", actualError.getMessage());
    }
}