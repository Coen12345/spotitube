package nl.han.oose.dto;

import java.util.ArrayList;
import java.util.List;

public class PlaylistsDTO {

    private List<PlaylistDTO> playlists = new ArrayList<>();
    private long length;

    public PlaylistsDTO() {
    }

    public List<PlaylistDTO> getPlaylists() {
        return playlists;
    }

    public void addPlaylist(PlaylistDTO playlist) {
        playlists.add(playlist);
    }

    public void setPlaylists(List<PlaylistDTO> playlists) {
        this.playlists = playlists;
    }

    public void deletePlaylist(int id) {
        for (PlaylistDTO playlist : playlists
        ) {
            if (playlist.getId() == id) {
                playlists.remove(playlist);
            }
        }
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public void addLength(int length) {
        this.length += length;
    }

    @Override
    public String toString() {
        return "PlaylistsDTO{" +
                "playlists=" + playlists +
                ", length=" + length +
                '}';
    }
}