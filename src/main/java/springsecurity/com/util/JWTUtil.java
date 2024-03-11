package springsecurity.com.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import springsecurity.com.customException.JwtExpiredException;
import springsecurity.com.customException.TokenExpiredCustomException;

import java.security.SecureRandom;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.Date;
 
 
@Slf4j
@Component
public class JWTUtil {

    @Value("${app.secret.key}")
    private String seckret_key;



    public String generateToken(String subject){

      var tokenId = String.valueOf(new SecureRandom().nextInt(10000000));
      log.info("=======tokenId======:"+tokenId);
      
      ZoneId zoneId = ZoneId.systemDefault();
      ZonedDateTime issuedAt = ZonedDateTime.now(zoneId);
      log.info("===========issuedAt========="+issuedAt);
      ZonedDateTime expiredTime = issuedAt.plusMinutes(30);
      log.info("=========expiredTime========:"+expiredTime);
      
      return Jwts.builder()
              .setId(tokenId)
              .setSubject(subject)
              .setIssuer("ABC_Ltd")
              .setAudience("XYZ_Ltd")
             // .setIssuedAt(new Date(System.currentTimeMillis()))
              .setIssuedAt(Date.from(issuedAt.toInstant()))
             // .setExpiration(new Date(System.currentTimeMillis()+ TimeUnit.HOURS.toMillis((60*1000)*30)))
               .setExpiration(Date.from(expiredTime.toInstant()))
              .signWith(SignatureAlgorithm.HS512, Base64.getEncoder().encode(seckret_key.getBytes()))
              .compact();
    }

public Claims getClaims(String token){
        return  Jwts.parser()
                .setSigningKey(Base64.getEncoder().encode(seckret_key.getBytes()))
                .parseClaimsJws(token)
                .getBody();
}

//    public Claims getClaims(String token) {
//        try {
//            return  Jwts.parser()
//                .setSigningKey(Base64.getEncoder().encode(seckret_key.getBytes()))
//                .parseClaimsJws(token)
//               .getBody();
//
//        } catch (ExpiredJwtException ex) {
//            throw new JwtExpiredException("token");
//        } catch (Exception e) {
//            // Handle other exceptions as needed
//            throw new RuntimeException("Error parsing JWT token", e);
//        }
//    }


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
