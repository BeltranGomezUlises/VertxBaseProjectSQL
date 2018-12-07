package utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * utileria de conversion de fechas, herramienta de manejo de fechas donde incluye suma de tiempo, días y conversiones diferentes formatos de presentación
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
public class UtilsDate {

    private static final SimpleDateFormat SDF_DATABASE = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * convierte date en su representacion texto en formato yyyy-MM-dd HH:mm:ss
     *
     * @param date fecha a convertir
     * @return texto representativo de la fecha
     */
    public static String sdfDataBase(Date date) {
        return SDF_DATABASE.format(date);
    }

}
