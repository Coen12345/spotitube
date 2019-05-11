package nl.han.oose.service;

import nl.han.oose.dto.PlaylistDTO;
import nl.han.oose.dto.PlaylistsDTO;
import nl.han.oose.dto.TrackDTO;
import nl.han.oose.dto.TracksDTO;

public interface PlaylistsService {

    PlaylistsDTO getPlaylistsFromUser(String token);

    void deletePlaylistFromUser(String token, int id);

    PlaylistDTO addPlaylist(PlaylistDTO playlistDTO);

    void assignPlaylistToUser(String token, int id);


    void editPlaylist(PlaylistDTO playlistDTO, int id);

    TracksDTO getAllTracksFromPlaylist(int id);

    void removeTrackFromPlaylist(int playlistid, int trackid);

    void addTrackToPlaylist(int playlistid, TrackDTO track);
}
