package nl.han.oose.exceptionmapper;

import nl.han.oose.dto.ErrorDTO;
import nl.han.oose.service.SpotitubeNoDataException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import static org.junit.jupiter.api.Assertions.*;

class NoDataExceptionMapperTest {

    private NoDataExceptionMapper sut;

    @BeforeEach
    void setUp() {
        sut = new NoDataExceptionMapper();
    }

    @Test
    void noDataMapperReturnsNotFoundResponseAndDataNotFoundMessage() {
        Response actualResult = sut.toResponse(new SpotitubeNoDataException("Ingewikkelde errormessage"));
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), actualResult.getStatus());

        ErrorDTO actualError = (ErrorDTO) actualResult.getEntity();
        assertEquals("Data was not found.", actualError.getMessage());
    }
}