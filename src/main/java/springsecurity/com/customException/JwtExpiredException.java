package springsecurity.com.customException;

public class JwtExpiredException extends RuntimeException{
    public JwtExpiredException(String message){
        super(message);
    }
}
