package springsecurity.com.customException;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TokenExpiredCustomException {

    @ExceptionHandler(JwtExpiredException.class)
    public ResponseEntity<String> handleJwtExpiredException(JwtExpiredException ex){
     return  new ResponseEntity<>("JWT Token Expired"+ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }
}
