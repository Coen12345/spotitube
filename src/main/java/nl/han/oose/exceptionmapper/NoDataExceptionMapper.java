package nl.han.oose.exceptionmapper;

import nl.han.oose.dto.ErrorDTO;
import nl.han.oose.service.SpotitubeNoDataException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class NoDataExceptionMapper implements ExceptionMapper<SpotitubeNoDataException> {
    @Override
    public Response toResponse(SpotitubeNoDataException e) {
        return Response.status(Response.Status.NOT_FOUND)
                .entity(new ErrorDTO("Data was not found."))
                .build();
    }
}
