package nl.han.oose.service;

import nl.han.oose.dto.PlaylistDTO;
import nl.han.oose.dto.PlaylistsDTO;
import nl.han.oose.dto.TrackDTO;
import nl.han.oose.dto.TracksDTO;
import nl.han.oose.persistence.PlaylistDAO;
import nl.han.oose.util.IdGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
public class PlaylistServiceTest {

    @Mock
    PlaylistDAO playlistDAOStub;

    @Mock
    IdGenerator idGenerator;

    @InjectMocks
    private PlaylistsServiceImpl sut;

    private PlaylistsDTO mockedPlaylists;

    private PlaylistDTO mockedPlaylist;

    private String testToken = "1234-1234-1234";

    private TracksDTO mockedTracks;

    @BeforeEach
    void setUp() {
        mockedPlaylists = new PlaylistsDTO();
        mockedPlaylists.setLength(123123);
        mockedPlaylists.addPlaylist(new PlaylistDTO(-1, "testplaylist", true));

        mockedPlaylist = new PlaylistDTO(5, "test", true);

        mockedTracks = new TracksDTO();
        mockedTracks.addTrack(new TrackDTO());
    }

    @Test
    public void getPlaylistsSuccess() {
        Mockito.when(playlistDAOStub.getPlaylistsFromUser(testToken))
                .thenReturn(mockedPlaylists);
        PlaylistsDTO actualRestult = sut.getPlaylistsFromUser(testToken);

        assertEquals(123123, actualRestult.getLength());
    }

    @Test
    void getPlaylistFailure() {
        Mockito.when(playlistDAOStub.getPlaylistsFromUser(anyString()))
                .thenReturn(new PlaylistsDTO());
        SpotitubeNoDataException spotitubeNoDataException = assertThrows(SpotitubeNoDataException.class, () -> {
            PlaylistsDTO actualResult = sut.getPlaylistsFromUser("wrong");
        });
        assertEquals("No playlists were found", spotitubeNoDataException.getMessage());
    }

    @Test
    void addPlaylistSuccess() {
        Mockito.when(idGenerator.generateID())
                .thenReturn(1);
        doNothing().when(playlistDAOStub).addPlaylist(any());

        PlaylistDTO actualResult = sut.addPlaylist(mockedPlaylist);
        assertEquals(1, actualResult.getId());
        assertEquals(false, actualResult.isOwner());
    }

    @Test
    void getAllTracksFromPlaylistSuccess() {
        Mockito.when(playlistDAOStub.getAllTracksFromPlaylist(1))
                .thenReturn(mockedTracks);
        TracksDTO actualResult = sut.getAllTracksFromPlaylist(1);
        assertEquals(1, actualResult.getTracks().size());
    }

    @Test
    void getAllTracksFromPlaylistFailure() {
        Mockito.when(playlistDAOStub.getAllTracksFromPlaylist(1234))
                .thenReturn(new TracksDTO());

        SpotitubeNoDataException spotitubeNoDataException = assertThrows(SpotitubeNoDataException.class, () -> {
            TracksDTO actualResult = sut.getAllTracksFromPlaylist(1234);
        });
        assertEquals("No tracks were found", spotitubeNoDataException.getMessage());
    }

    @Test
    void deletePlaylistCalled() {
        sut.deletePlaylistFromUser(testToken, 5);
        Mockito.verify(playlistDAOStub).deletePlaylistFromUser(testToken, 5);
    }

    @Test
    void assignPlaylistToUserCalled() {
        sut.assignPlaylistToUser(testToken, 1);
        Mockito.verify(playlistDAOStub).assignPlaylistToUser(testToken, 1);
    }

    @Test
    void editPlaylistCalled() {
        sut.editPlaylist(mockedPlaylist, 5);
        Mockito.verify(playlistDAOStub).editPlaylist(mockedPlaylist, 5);
    }

    @Test
    void removeTracksCalled() {
        sut.removeTrackFromPlaylist(1, 1);
        Mockito.verify(playlistDAOStub).removeTrackFromPlaylist(1, 1);

    }

    @Test
    void addTracksCalled() {
        TrackDTO testTrack = new TrackDTO();
        sut.addTrackToPlaylist(1, testTrack);
        Mockito.verify(playlistDAOStub).addTrackToPlaylist(1, testTrack);

    }

    @Test
    void playlistServiceConstructorCreatesObject() {
        PlaylistsService playlistsService = new PlaylistsServiceImpl();
    }
}
