package nl.han.oose.resource;

import nl.han.oose.dto.TracksDTO;
import nl.han.oose.service.AuthenticationService;
import nl.han.oose.service.TrackService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/tracks")
public class TrackResource {


    private AuthenticationService authenticationService;
    private TrackService trackService;

    public TrackResource() {
    }

    @Inject
    public TrackResource(AuthenticationService authenticationService, TrackService trackService) {
        this.authenticationService = authenticationService;
        this.trackService = trackService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllTracksWithoutTracksFromCurrentPlaylist(@QueryParam("forPlaylist") int id, @QueryParam("token") String token) {
        authenticationService.validateToken(token);
        TracksDTO tracksDTO = trackService.getAllTracksWithoutTracksFromCurrentPlaylist(id);
        return Response.ok(tracksDTO).build();
    }
}
