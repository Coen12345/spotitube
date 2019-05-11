package nl.han.oose.service;


import nl.han.oose.dto.TracksDTO;
import nl.han.oose.persistence.TrackDAO;

import javax.enterprise.inject.Default;
import javax.inject.Inject;

@Default
public class TrackServiceImpl implements TrackService {

    private TrackDAO trackDAO;

    public TrackServiceImpl() {
    }

    @Inject
    public TrackServiceImpl(TrackDAO trackDAO) {
        this.trackDAO = trackDAO;
    }

    @Override
    public TracksDTO getAllTracksWithoutTracksFromCurrentPlaylist(int id) {
        return trackDAO.getAllTracksWithoutTracksFromCurrentPlaylist(id);
    }
}
