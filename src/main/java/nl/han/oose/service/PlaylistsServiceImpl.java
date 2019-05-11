package nl.han.oose.service;

import nl.han.oose.dto.PlaylistDTO;
import nl.han.oose.dto.PlaylistsDTO;
import nl.han.oose.dto.TrackDTO;
import nl.han.oose.dto.TracksDTO;
import nl.han.oose.persistence.PlaylistDAO;
import nl.han.oose.util.IdGenerator;

import javax.enterprise.inject.Default;
import javax.inject.Inject;

@Default
public class PlaylistsServiceImpl implements PlaylistsService {

    private PlaylistDAO playlistDAO;
    private IdGenerator idGenerator;

    public PlaylistsServiceImpl() {
    }

    @Inject
    public PlaylistsServiceImpl(PlaylistDAO playlistDAO, IdGenerator idGenerator) {
        this.playlistDAO = playlistDAO;
        this.idGenerator = idGenerator;
    }

    @Override
    public PlaylistsDTO getPlaylistsFromUser(String token) {
        PlaylistsDTO userPlaylists = playlistDAO.getPlaylistsFromUser(token);
        if (!userPlaylists.getPlaylists().isEmpty()) {
            return userPlaylists;
        } else {
            throw new SpotitubeNoDataException("No playlists were found");
        }
    }

    @Override
    public void deletePlaylistFromUser(String token, int id) {
        playlistDAO.deletePlaylistFromUser(token, id);
    }

    @Override
    public PlaylistDTO addPlaylist(PlaylistDTO playlistDTO) {
        playlistDTO.setId(idGenerator.generateID());
        playlistDTO.setOwner(false);
        playlistDAO.addPlaylist(playlistDTO);
        return playlistDTO;
    }

    @Override
    public void assignPlaylistToUser(String token, int id) {
        playlistDAO.assignPlaylistToUser(token, id);
    }

    @Override
    public void editPlaylist(PlaylistDTO playlistDTO, int id) {
        playlistDAO.editPlaylist(playlistDTO, id);
    }

    @Override
    public TracksDTO getAllTracksFromPlaylist(int id) {
        TracksDTO tracks = playlistDAO.getAllTracksFromPlaylist(id);
        if (!tracks.getTracks().isEmpty()) {
            return tracks;
        } else {
            throw new SpotitubeNoDataException("No tracks were found");
        }
    }

    @Override
    public void removeTrackFromPlaylist(int playlistid, int trackid) {
        playlistDAO.removeTrackFromPlaylist(playlistid, trackid);
    }

    @Override
    public void addTrackToPlaylist(int playlistid, TrackDTO track) {
        playlistDAO.addTrackToPlaylist(playlistid, track);
    }
}