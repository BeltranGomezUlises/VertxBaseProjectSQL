package models;

/**
 * Modelo para contener los datos de la sesion de usuario
 *
 * @author Ulises Beltrán Gómez - beltrangomezulises@gmail.com
 */
public class ModelSesion {

    private int userId;
    private int sucursalId;

    public ModelSesion() {
    }

    public ModelSesion(int userId, int sucursalId) {
        this.userId = userId;
        this.sucursalId = sucursalId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getSucursalId() {
        return sucursalId;
    }

    public void setSucursalId(int sucursalId) {
        this.sucursalId = sucursalId;
    }

}
