package nl.han.oose.dto;

import java.util.ArrayList;
import java.util.List;

public class TracksDTO {

    private List<TrackDTO> tracks = new ArrayList<>();

    public TracksDTO() {
    }

    public void addTrack(TrackDTO track) {
        tracks.add(track);
    }

    public List<TrackDTO> getTracks() {
        return tracks;
    }
}
