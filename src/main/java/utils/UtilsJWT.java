/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.vertx.core.json.Json;
import models.ModelSesion;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
public class UtilsJWT {

    private static final String STRING_KEY = "k$5*t;ht^L$_g76k'H6LSas\"n`6xrE=)?)+g!~0r198(\"D^|Hl'~+SvuMm'P_([";

    public static String generateSessionToken(final int userId, final int branchofficeId) throws JsonProcessingException {
        JwtBuilder builder = Jwts.builder();
        ModelSesion modelSesion = new ModelSesion(1, 1);
        builder.setSubject(Json.encode(modelSesion));
        return builder.signWith(SignatureAlgorithm.HS512, STRING_KEY).compact();
    }

    public static int getUserIdFrom(String token) {
        return Integer.valueOf(Jwts.parser()
                .setSigningKey(STRING_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getSubject());
    }

    public static boolean isTokenValid(String token) {
        try {
            Jwts.parser().setSigningKey(STRING_KEY).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException | MalformedJwtException | SignatureException
                | UnsupportedJwtException | IllegalArgumentException | NullPointerException e) {
            return false;
        }
    }

}
