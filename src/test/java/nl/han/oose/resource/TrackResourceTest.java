package nl.han.oose.resource;

import nl.han.oose.dto.TrackDTO;
import nl.han.oose.dto.TracksDTO;
import nl.han.oose.service.AuthenticationService;
import nl.han.oose.service.TrackService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.ws.rs.core.Response;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class TrackResourceTest {

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private TrackService trackService;

    @InjectMocks
    private TrackResource sut;

    private String testToken = "1234-1234-1234";
    private TracksDTO mockedTracks;

    @BeforeEach
    void setUp() {
        mockedTracks = new TracksDTO();
        mockedTracks.addTrack(new TrackDTO());
    }

    @Test
    void getAllTracksWithoutTracksFromCurrentPlaylist() {
        Mockito.when(authenticationService.validateToken(testToken))
                .thenReturn(testToken);
        Mockito.when(trackService.getAllTracksWithoutTracksFromCurrentPlaylist(1))
                .thenReturn(mockedTracks);

        Response actualResult = sut.getAllTracksWithoutTracksFromCurrentPlaylist(1, testToken);
        assertEquals(Response.Status.OK.getStatusCode(), actualResult.getStatus());

        TracksDTO actualTracks = (TracksDTO) actualResult.getEntity();
        assertEquals(mockedTracks.getTracks(), actualTracks.getTracks());

    }

    @Test
    void trackResourceConstructorCreatesObject() {
        TrackResource trackResource = new TrackResource();
    }
}