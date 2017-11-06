package utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;

import java.security.Key;
import java.util.Date;

public class JWT {

    private static utils.JWT instance;
    private Key key;

    private long TTL = 60000000;

    protected JWT() {
        key = MacProvider.generateKey();
    }

    public static utils.JWT getJWT() {
        if(instance == null) {
            return new utils.JWT();
        }
        return instance;
    }

    public String generateToken(String username) {
        Date expire = new Date(System.currentTimeMillis() + TTL);
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(expire)
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }

    public String encode(String text) {
        return Jwts.builder()
                .setSubject(text)
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }

    public Claims decode(String token) {
        try {
            return (Claims)Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(token);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
