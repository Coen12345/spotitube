package nl.han.oose.resource;

import nl.han.oose.dto.PlaylistDTO;
import nl.han.oose.dto.PlaylistsDTO;
import nl.han.oose.dto.TrackDTO;
import nl.han.oose.service.AuthenticationService;
import nl.han.oose.service.PlaylistsService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/playlists")
public class PlaylistResource {

    private PlaylistsService playlistsService;
    private AuthenticationService authenticationService;

    public PlaylistResource() {
    }

    @Inject
    public PlaylistResource(PlaylistsService playlistsService, AuthenticationService authenticationService) {
        this.playlistsService = playlistsService;
        this.authenticationService = authenticationService;
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPlaylistsFromUser(@QueryParam("token") String token) {
        token = authenticationService.validateToken(token);
        PlaylistsDTO playlistsDTO = playlistsService.getPlaylistsFromUser(token);
        return Response.ok(playlistsDTO).build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deletePlaylist(@QueryParam("token") String token, @PathParam("id") int id) {
        token = authenticationService.validateToken(token);
        playlistsService.deletePlaylistFromUser(token, id);
        return Response.ok(playlistsService.getPlaylistsFromUser(token)).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addPlaylist(@QueryParam("token") String token, PlaylistDTO playlistDTO) {
        token = authenticationService.validateToken(token);
        PlaylistDTO addedPlaylist = playlistsService.addPlaylist(playlistDTO);
        playlistsService.assignPlaylistToUser(token, addedPlaylist.getId());
        return Response.ok(playlistsService.getPlaylistsFromUser(token)).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response editPlaylist(@QueryParam("token") String token,
                                 @PathParam("id") int id,
                                 PlaylistDTO playlistDTO) {
        token = authenticationService.validateToken(token);
        playlistsService.editPlaylist(playlistDTO, id);
        return Response.ok(playlistsService.getPlaylistsFromUser(token)).build();
    }

    @GET
    @Path("/{id}/tracks")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllTracksFromPlaylist(@PathParam("id") int id,
                                             @QueryParam("token") String token) {
        authenticationService.validateToken(token);
        return Response.ok(playlistsService.getAllTracksFromPlaylist(id)).build();
    }

    @DELETE
    @Path("/{playlistid}/tracks/{trackid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeTrackFromPlaylist(@PathParam("playlistid") int playlistid,
                                            @PathParam("trackid") int trackid,
                                            @QueryParam("token") String token) {
        authenticationService.validateToken(token);
        playlistsService.removeTrackFromPlaylist(playlistid, trackid);
        return Response.ok(playlistsService.getAllTracksFromPlaylist(playlistid)).build();
    }

    @POST
    @Path("/{playlistid}/tracks")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addTrackToPlaylist(@PathParam("playlistid") int playlistid,
                                       @QueryParam("token") String token,
                                       TrackDTO track) {
        authenticationService.validateToken(token);
        playlistsService.addTrackToPlaylist(playlistid, track);
        return Response.ok(playlistsService.getAllTracksFromPlaylist(playlistid)).build();
    }
}