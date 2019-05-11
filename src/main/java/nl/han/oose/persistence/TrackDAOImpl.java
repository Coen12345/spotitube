package nl.han.oose.persistence;

import nl.han.oose.dto.TrackDTO;
import nl.han.oose.dto.TracksDTO;
import nl.han.oose.service.SpotitubeNoDataException;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Default
public class TrackDAOImpl implements TrackDAO {

    private ConnectionFactory connectionFactory;

    @Inject
    public TrackDAOImpl(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Override
    public TracksDTO getAllTracksWithoutTracksFromCurrentPlaylist(int id) {
        TracksDTO foundTracks = null;
        try (
                Connection connection = connectionFactory.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "select * \n" +
                                "from track t where t.id not in\t(select pt.track_id\n" +
                                "                               \t\tfrom playlist_tracks pt\n" +
                                "                               \t\twhere playlist_id=?)\t\t");
        ) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            foundTracks = new TracksDTO();
            while (resultSet.next()) {
                TrackDTO track = new TrackDTO();
                track.setId(resultSet.getInt("id"));
                track.setTitle(resultSet.getString("title"));
                track.setPerformer(resultSet.getString("performer"));
                track.setDuration(resultSet.getInt("duration"));
                track.setAlbum(resultSet.getString("album"));
                track.setPlaycount(resultSet.getInt("playcount"));
                track.setPublicationDate(resultSet.getString("publicationDate"));
                track.setDescription(resultSet.getString("description"));
                track.setOfflineAvailable(resultSet.getBoolean("offlineAvailable"));

                foundTracks.addTrack(track);
            }
        } catch (SQLException e) {
            throw new SpotitubeNoDataException(e);
        }
        return foundTracks;
    }
}
