package springsecurity.com.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Component
public class JWTUtil {

    @Value("${app.secret.key}")
    private String seckret_key;



    public String generateToken(String subject){

      var tokenId = String.valueOf(new Random().nextInt(100));
      return Jwts.builder()
              .setId(tokenId)
              .setSubject(subject)
              .setIssuer("ABC_Ltd")
              .setAudience("XYZ_Ltd")
              .setIssuedAt(new Date(System.currentTimeMillis()))
              .setExpiration(new Date(System.currentTimeMillis()+ TimeUnit.HOURS.toMillis((60*1000)*30)))
              .signWith(SignatureAlgorithm.HS512, Base64.getEncoder().encode(seckret_key.getBytes()))
              .compact();
    }

public Claims getClaims(String token){
        return  Jwts.parser()
                .setSigningKey(Base64.getEncoder().encode(seckret_key.getBytes()))
                .parseClaimsJws(token)
                .getBody();
}

public boolean isValidToken(String token){
 return  getClaims(token).getExpiration().after(new Date(System.currentTimeMillis()));

}

public boolean isValidToken(String token,String username){
var tokenUsername = getSubject(token);
  return username.equals(tokenUsername) && !isTokenExpired(token);
}


public boolean isTokenExpired(String token){
        return  getExpirationDate(token).before(new Date(System.currentTimeMillis()));
}
public  Date getExpirationDate(String token){
        return  getClaims(token).getExpiration();

}

public String getSubject(String token){
        return getClaims(token).getSubject();

}
}
