package recipes;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.authentication.AuthenticationManager;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserRepository userRepository;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers( "/api/register","h2-console/**").permitAll() // Allow POST requests to /api/register without authentication
                .antMatchers( "/api/**").authenticated()
                .and()
                .httpBasic()
                .and()
                .csrf(csrf -> csrf.disable()).headers(headers -> headers.frameOptions().disable());

    }

//    @Bean
//    @Override
//    public UserDetailsService userDetailsService() {
//        UserDetails user = User.withDefaultPasswordEncoder()
//                .username("user")
//                .password("password")
//                .roles("USER")
//                .build();
//
//        return new InMemoryUserDetailsManager(user);
//    }

    //todo: UserDetails Klasse erstellen und Anpassungen der Endpoints wegen Authorisierung

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth
//                .userDetailsService(userDetailsService)
//                .passwordEncoder(passwordEncoder());
    }



//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//      //  auth.userDetailsService(userDetailsService);
////        auth.inMemoryAuthentication().withUser("erik@hallo.com").password(passwordEncoder().encode("123asdfasdfasdfasdf")
////        ).roles("USER");
//
//        auth
//                .userDetailsService(email -> {
//                    recipes.User
//                            user = userRepository.findByEmail(email)
//                            .orElseThrow(() -> new RuntimeException("User not found: " + email));
//
//                    return org.springframework.security.core.userdetails.User
//                            .withUsername(user.getEmail())
//                            .password(passwordEncoder().encode(user.getPassword()))
//                            .roles("USER")
//                            .build();
//                });
//    }



    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userDetailsService);
//    }

