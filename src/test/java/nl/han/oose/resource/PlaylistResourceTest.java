package nl.han.oose.resource;

import nl.han.oose.dto.PlaylistDTO;
import nl.han.oose.dto.PlaylistsDTO;
import nl.han.oose.dto.TrackDTO;
import nl.han.oose.dto.TracksDTO;
import nl.han.oose.service.AuthenticationService;
import nl.han.oose.service.PlaylistsService;
import nl.han.oose.service.SpotitubeNoDataException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.ws.rs.core.Response;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PlaylistResourceTest {

    @Mock
    private PlaylistsService playlistsService;

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private PlaylistResource sut;

    private PlaylistsDTO mockedPlaylists;

    private String wrongToken = "12345-12345";

    private String testToken = "1234-1234-1234";
    private PlaylistDTO mockedPlaylist;
    private TracksDTO mockedTracks;

    @BeforeEach
    void setUp() {
        mockedPlaylists = new PlaylistsDTO();
        mockedPlaylists.setLength(123123);
        mockedPlaylists.addPlaylist(new PlaylistDTO(1, "testplaylist", true));

        mockedPlaylist = new PlaylistDTO(5, "test", true);

        mockedTracks = new TracksDTO();
        mockedTracks.addTrack(new TrackDTO());
    }

    @Test
    void getPlaylistsSuccess() {
        mockitoWhenGetPlaylistReturnMockedPlaylist();
        mockitoValidateUserTokenWithTokenReturn(testToken);
        Response actualResult = sut.getPlaylistsFromUser(testToken);
        assertEquals(Response.Status.OK.getStatusCode(), actualResult.getStatus());

        PlaylistsDTO actualPlaylists = (PlaylistsDTO) actualResult.getEntity();
        assertEquals(123123, actualPlaylists.getLength());
    }


    @Test
    void getPlaylistFailure() {
        when(playlistsService.getPlaylistsFromUser(anyString()))
                .thenThrow(new SpotitubeNoDataException("No playlists were found"));
        mockitoValidateUserTokenWithTokenReturn(anyString());
        SpotitubeNoDataException spotitubeNoDataException = assertThrows(SpotitubeNoDataException.class, () -> {
            Response actualResult = sut.getPlaylistsFromUser(wrongToken);
        });
        assertEquals("No playlists were found", spotitubeNoDataException.getMessage());
    }

    @Test
    void deletePlaylistSuccess() {
        mockitoWhenGetPlaylistReturnMockedPlaylist();
        mockitoValidateUserTokenWithTokenReturn(testToken);
        Mockito.doNothing().when(playlistsService).deletePlaylistFromUser(testToken, 1);

        Response actualResult = sut.deletePlaylist(testToken, 1);
        assertEquals(Response.Status.OK.getStatusCode(), actualResult.getStatus());
    }

    @Test
    void addPlaylistSuccess() {
        mockitoWhenGetPlaylistReturnMockedPlaylist();
        mockitoValidateUserTokenWithTokenReturn(testToken);
        Mockito.when(playlistsService.addPlaylist(mockedPlaylist))
                .thenReturn(mockedPlaylist);
        Mockito.doNothing().when(playlistsService).assignPlaylistToUser(testToken, 5);

        Response actualResult = sut.addPlaylist(testToken, mockedPlaylist);
        assertEquals(Response.Status.OK.getStatusCode(), actualResult.getStatus());

        PlaylistsDTO actualPlaylists = (PlaylistsDTO) actualResult.getEntity();
        assertEquals(mockedPlaylists.getPlaylists(), actualPlaylists.getPlaylists());
    }

    @Test
    void editPlaylistSuccess() {
        mockitoValidateUserTokenWithTokenReturn(testToken);
        mockitoWhenGetPlaylistReturnMockedPlaylist();
        Mockito.doNothing().when(playlistsService).editPlaylist(mockedPlaylist, 5);

        Response actualResult = sut.editPlaylist(testToken, 5, mockedPlaylist);
        assertEquals(Response.Status.OK.getStatusCode(), actualResult.getStatus());

        PlaylistsDTO actualPlaylists = (PlaylistsDTO) actualResult.getEntity();
        assertEquals(mockedPlaylists.getPlaylists(), actualPlaylists.getPlaylists());
    }

    @Test
    void getAllTracksFromPlaylistSuccess() {
        mockitoValidateUserTokenWithTokenReturn(testToken);
        mockitoWhenGetTracksReturnMockedTracks();

        Response actualResult = sut.getAllTracksFromPlaylist(5, testToken);
        assertEquals(Response.Status.OK.getStatusCode(), actualResult.getStatus());

        TracksDTO actualTracks = (TracksDTO) actualResult.getEntity();
        assertEquals(mockedTracks.getTracks(), actualTracks.getTracks());
    }

    @Test
    void getAllTracksFromPlaylistFailure() {
        mockitoValidateUserTokenWithTokenReturn(testToken);
        Mockito.when(playlistsService.getAllTracksFromPlaylist(10))
                .thenThrow(new SpotitubeNoDataException("No data was found"));
        SpotitubeNoDataException spotitubeNoDataException = assertThrows(SpotitubeNoDataException.class, () -> {
            Response actualResult = sut.getAllTracksFromPlaylist(10, testToken);
        });

        assertEquals("No data was found", spotitubeNoDataException.getMessage());
    }

    @Test
    void removeTrackFromPlaylistSuccess() {
        mockitoValidateUserTokenWithTokenReturn(testToken);
        mockitoWhenGetTracksReturnMockedTracks();
        Mockito.doNothing().when(playlistsService).removeTrackFromPlaylist(5, 1);

        Response actualResult = sut.removeTrackFromPlaylist(5, 1, testToken);
        assertEquals(Response.Status.OK.getStatusCode(), actualResult.getStatus());

        TracksDTO actualTracks = (TracksDTO) actualResult.getEntity();
        assertEquals(mockedTracks.getTracks(), actualTracks.getTracks());

    }

    @Test
    void addTrackToPlaylistSuccess() {
        mockitoValidateUserTokenWithTokenReturn(testToken);
        mockitoWhenGetTracksReturnMockedTracks();
        TrackDTO mockedTrack = new TrackDTO();
        Mockito.doNothing().when(playlistsService).addTrackToPlaylist(5, mockedTrack);

        Response actualResult = sut.addTrackToPlaylist(5, testToken, mockedTrack);
        assertEquals(Response.Status.OK.getStatusCode(), actualResult.getStatus());

        TracksDTO actualTracks = (TracksDTO) actualResult.getEntity();
        assertEquals(mockedTracks.getTracks(), actualTracks.getTracks());
    }

    @Test
    void playlistResourceConstructorCreatesObject() {
        PlaylistResource playlistResource = new PlaylistResource();
    }

    private void mockitoWhenGetTracksReturnMockedTracks() {
        Mockito.when(playlistsService.getAllTracksFromPlaylist(5))
                .thenReturn(mockedTracks);
    }

    private void mockitoWhenGetPlaylistReturnMockedPlaylist() {
        Mockito.when(playlistsService.getPlaylistsFromUser(testToken))
                .thenReturn(mockedPlaylists);
    }

    private void mockitoValidateUserTokenWithTokenReturn(String usertoken) {
        Mockito.when(authenticationService.validateToken(usertoken))
                .thenReturn(usertoken);
    }
}
