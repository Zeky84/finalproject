package utilities.finalproject.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    /*
    this is the service that will handle the JWT
     */
    //Create JWT signing key  ***1***
    //Create / Generate a JWT ***2***
    // Extract claims from JWT ***3***
    // Validate JWT(is token expired) ***4***
    // Sign JWT

    @Value("${jwt.signIngKey}") //It was set into System Environment Variables, but didn't work... ***1***
    private String jwtSignInKey;

    @Value("${jwt.expirationInMs}")
    private Long expirationInMs;

    public String generateJwtToken(Map<String, ?> extraClaims, UserDetails user) { //***2***
        String jwt = Jwts.builder()
                .claims(extraClaims)
                .subject(user.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationInMs))
                .setHeaderParam("typ", "JWT")
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact(); //This is the method that will generate the JWT. Trevor said that this should be named "build" instead of "compact"
        return jwt;
    }


    private Key getSigningKey() { //Converting the signingKey to secretKey in bytes  ***5***
        byte[] jwtSigningKeyAsBytes = Decoders.BASE64.decode(jwtSignInKey);
        SecretKey secretKey = Keys.hmacShaKeyFor(jwtSigningKeyAsBytes);
        return secretKey;
    }

    public Claims extractAlltClaims(String jwt) { //This will return all the claims inside the token ***3***
        Claims claimsBody = Jwts.parser()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(jwt)
                .getBody();
        return claimsBody;
    }
//---------------------------------Trevor's methods below---------------------------------
    private <T> T extractClaim(String jwt, Function<Claims, T> extractClaims) {
        final Claims claims = extractAlltClaims(jwt);
        return extractClaims.apply(claims);
    }
    public String extractSubjectTrevor(String jwt) {
        return extractClaim(jwt, Claims::getSubject);
    }


   //-------------------------My methods below----------------------------------------------

    //These 3 methods below are for extracting specific claims(Same that Trevor did with extractClaim method above)
    public String extractSubject(String jwt){
        return extractAlltClaims(jwt).getSubject();
    }
    public Date extractExpiration(String jwt){
        return extractAlltClaims(jwt).getExpiration();
    }
    public String extractIssuedAt(String jwt){
        return extractAlltClaims(jwt).getIssuedAt().toString();
    }




    public Boolean isTokenExpired(String token) { //***4*** Validating the token
        return extractExpiration(token).before(new Date());
    }


    public Boolean validateToken(String token, UserDetails user) { //***4*** Validating the token
        String subject = extractSubject(token);
        Date expirationDate = extractClaim(token, Claims::getExpiration);
        return user.getUsername().equalsIgnoreCase(subject) && new Date().before(expirationDate);
    }
}
