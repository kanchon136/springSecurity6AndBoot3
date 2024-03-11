package springsecurity.com.config;

 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
 
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired private SecurityFilter securityFilter;
    @Autowired private UserDetailsService userDetailsService;
    @Autowired
    private UnAuthorizedUserEntryEndpoint authorizedUserEntryEndpoint;




@Bean
public PasswordEncoder passwordEncoder() {
    PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    return encoder;
}

@Bean
public AuthenticationProvider authenticationProvider(){
    var daoAuthenticationProvider = new DaoAuthenticationProvider();
    daoAuthenticationProvider.setUserDetailsService(userDetailsService);
    daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
    return  daoAuthenticationProvider;

}

public AuthenticationManager authenticationManager(AuthenticationManagerBuilder builder) throws Exception {
   builder.authenticationProvider(authenticationProvider());
   return builder.build();
}

 @Bean
 public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	 return http
			 .sessionManagement(session->
			   session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) 
             .csrf(csrf -> {
                 csrf.disable();
             })
            
             .cors(cors -> cors.disable())
            
             .authorizeHttpRequests(auth -> {
                 auth.requestMatchers("/error/**").permitAll();
                auth.requestMatchers("/user/ceateUser","/user/authenticate").permitAll();
                 auth.anyRequest().authenticated();
             }).exceptionHandling(ex-> ex.authenticationEntryPoint(authorizedUserEntryEndpoint))
             .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class).build();
           //  .httpBasic(Customizer.withDefaults()).build();
    }
    @Bean
    public AuthenticationManager manager(final AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }
}
