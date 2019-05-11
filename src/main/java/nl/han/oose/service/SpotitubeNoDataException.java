package nl.han.oose.service;

import java.sql.SQLException;

public class SpotitubeNoDataException extends RuntimeException {

    public SpotitubeNoDataException(SQLException message) {
        super(message);
    }

    public SpotitubeNoDataException(String message) {
        super(message);
    }
}
