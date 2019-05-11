package nl.han.oose.dto;

public class ErrorDTO {

    private String status;
    private String message;

    public ErrorDTO(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public ErrorDTO(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
