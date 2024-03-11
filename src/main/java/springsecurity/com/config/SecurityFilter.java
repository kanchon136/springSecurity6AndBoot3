package springsecurity.com.config;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import springsecurity.com.dto.UserResponse;
import springsecurity.com.util.JWTUtil;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;



    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {


        String token = request.getHeader("Authorization");
        if (token != null) {
            String userName = jwtUtil.getSubject(token);
            if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails user = userDetailsService.loadUserByUsername(userName);
                boolean isValid = jwtUtil.isValidToken(token, user.getUsername());
                if (isValid) {
                    var authToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        }
        filterChain.doFilter(request, response);
    }  catch (ExpiredJwtException e){
            handleExpiredJwtException(request,response,e);
        }
    }

    private void handleExpiredJwtException(HttpServletRequest request, HttpServletResponse response, ExpiredJwtException ex) throws IOException {
        // Your custom handling logic for expired token
        // For example, you can send a custom response to the client
        var userResponse = new UserResponse(null,"Token Expired",null);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(userResponse.toString());
    }


}
