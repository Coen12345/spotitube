package nl.han.oose.exceptionmapper;

import nl.han.oose.dto.ErrorDTO;
import nl.han.oose.service.SpotitubeLoginException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class LoginExceptionMapper implements ExceptionMapper<SpotitubeLoginException> {

    @Override
    public Response toResponse(SpotitubeLoginException e) {
        ErrorDTO loginError = new ErrorDTO(Response.Status.UNAUTHORIZED.toString(),
                e.getMessage());
        return Response.status(Response.Status.UNAUTHORIZED).entity(loginError).build();
    }
}
