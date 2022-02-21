package app.exception;

public class CentreNotFoundException extends Exception {
    public CentreNotFoundException(int centre_id) {
        super(String.format("Centre is not found with id : '%s'", centre_id));
    }
}