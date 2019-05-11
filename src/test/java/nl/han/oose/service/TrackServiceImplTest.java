package nl.han.oose.service;

import nl.han.oose.dto.TrackDTO;
import nl.han.oose.dto.TracksDTO;
import nl.han.oose.persistence.TrackDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class TrackServiceImplTest {

    @Mock
    TrackDAO trackDAOStub;

    @InjectMocks
    private TrackServiceImpl sut;

    private TracksDTO mockedTracks;

    @BeforeEach
    void setUp() {
        mockedTracks = new TracksDTO();
        mockedTracks.addTrack(new TrackDTO());
    }

    @Test
    void getAllTracksWithoutTracksFromCurrentPlaylist() {
        Mockito.when(sut.getAllTracksWithoutTracksFromCurrentPlaylist(1))
                .thenReturn(mockedTracks);

        TracksDTO actualResult = sut.getAllTracksWithoutTracksFromCurrentPlaylist(1);
        assertEquals(mockedTracks.getTracks(), actualResult.getTracks());
    }

    @Test
    void trackServiceConstructorCreatesObject() {
        TrackService trackService = new TrackServiceImpl();
    }
}