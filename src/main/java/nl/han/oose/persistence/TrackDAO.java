package nl.han.oose.persistence;

import nl.han.oose.dto.TracksDTO;

public interface TrackDAO {
    TracksDTO getAllTracksWithoutTracksFromCurrentPlaylist(int id);
}
