package nl.han.oose.persistence;

import nl.han.oose.dto.PlaylistDTO;
import nl.han.oose.dto.PlaylistsDTO;
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
public class PlaylistDAOImpl implements PlaylistDAO {

    private ConnectionFactory connectionFactory;

    @Inject
    public PlaylistDAOImpl(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Override
    public PlaylistsDTO getPlaylistsFromUser(String token) {
        PlaylistsDTO foundPlaylists = null;
        try (
                Connection connection = connectionFactory.getConnection();
                PreparedStatement preparedStatementPlaylists = connection.prepareStatement(
                        "select * from playlist p inner join user_playlist up on p.id = up.id \n" +
                                "where exists\n" +
                                "( select 1 from usertoken ut inner join account a on ut.user = a.user \n" +
                                " where ut.token=?\n" +
                                "and up.user = ut.user)");
        ) {
            preparedStatementPlaylists.setString(1, token);

            ResultSet resultSetPlaylists = preparedStatementPlaylists.executeQuery();
            foundPlaylists = new PlaylistsDTO();
            while (resultSetPlaylists.next()) {
                PlaylistDTO playlist = new PlaylistDTO();
                playlist.setId(resultSetPlaylists.getInt("id"));
                playlist.setName(resultSetPlaylists.getString("name"));
                playlist.setOwner(resultSetPlaylists.getBoolean("owner"));

                foundPlaylists.addPlaylist(playlist);
                foundPlaylists.addLength(resultSetPlaylists.getInt("length"));
            }
        } catch (SQLException e) {
            throw new SpotitubeNoDataException(e);
        }
        return foundPlaylists;
    }

    @Override
    public void deletePlaylistFromUser(String token, int id) {
        try (
                Connection connection = connectionFactory.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "delete from user_playlist\n" +
                                "where user =(select ut.user \n" +
                                "             from usertoken ut\n" +
                                "             where ut.token =?)\n" +
                                "and id=?");
        ) {
            preparedStatement.setString(1, token);
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SpotitubePersistenceException(e);
        }
    }

    @Override
    public void addPlaylist(PlaylistDTO playlistDTO) {
        try (
                Connection connection = connectionFactory.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "insert into playlist\n" +
                                "values (?, ?, ?, 0)");
        ) {
            preparedStatement.setInt(1, playlistDTO.getId());
            preparedStatement.setString(2, playlistDTO.getName());
            preparedStatement.setBoolean(3, playlistDTO.isOwner());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SpotitubePersistenceException(e);
        }
    }

    @Override
    public void assignPlaylistToUser(String token, int id) {
        try (
                Connection connection = connectionFactory.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "insert into user_playlist\n" +
                                "values((select a.user \n" +
                                "          from account a \n" +
                                "          where a.user in\n" +
                                "          (select ut.user from usertoken ut \n" +
                                "           where token=?)), ?)");
        ) {
            preparedStatement.setString(1, token);
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SpotitubePersistenceException(e);
        }
    }

    @Override
    public void editPlaylist(PlaylistDTO playlistDTO, int id) {
        try (
                Connection connection = connectionFactory.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "update playlist\n" +
                                "set name=?,\n" +
                                "owner=?\n" +
                                "where id=?");
        ) {

            preparedStatement.setString(1, playlistDTO.getName());
            preparedStatement.setBoolean(2, playlistDTO.isOwner());
            preparedStatement.setInt(3, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SpotitubePersistenceException(e);
        }
    }

    @Override
    public TracksDTO getAllTracksFromPlaylist(int id) {
        TracksDTO foundTracks = null;
        try (
                Connection connection = connectionFactory.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "select distinct t.id, t.title, t.performer, t.duration, t.album, t.playcount, t.publicationDate, t.description, p.offline\n" +
                                "from track t inner join playlist_tracks p on\n" +
                                "                                t.id = p.track_id\n" +
                                "and playlist_id=?");
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
                track.setOfflineAvailable(resultSet.getBoolean("offline"));

                foundTracks.addTrack(track);
            }
        } catch (SQLException e) {
            throw new SpotitubeNoDataException(e);
        }
        return foundTracks;
    }

    @Override
    public void removeTrackFromPlaylist(int playlistid, int trackid) {
        try (
                Connection connection = connectionFactory.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "delete from playlist_tracks where playlist_id=? and track_id=?");
        ) {
            preparedStatement.setInt(1, playlistid);
            preparedStatement.setInt(2, trackid);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SpotitubePersistenceException(e);
        }
    }

    @Override
    public void addTrackToPlaylist(int playlistid, TrackDTO track) {
        try (
                Connection connection = connectionFactory.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "insert into playlist_tracks values(?,?,?)");
        ) {
            preparedStatement.setInt(1, playlistid);
            preparedStatement.setInt(2, track.getId());
            preparedStatement.setBoolean(3, track.isOfflineAvailable());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SpotitubePersistenceException(e);
        }
    }
}
