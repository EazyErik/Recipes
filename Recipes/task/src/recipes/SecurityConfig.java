package recipes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        System.out.println("drin");
//        http
//                .authorizeRequests()
//
//                .antMatchers("api/**").permitAll() // Erlaubt den Zugriff auf öffentliche Ressourcen ohne Authentifizierung
//                .anyRequest().authenticated()
//                .and()
//                .httpBasic();
//
//
    //todo: register User
   @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/api/register")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();


    }

    @Bean
    @Override
    protected UserDetailsService userDetailsService() {
        // Erstellen Sie einen Benutzer mit Benutzername "user" und Passwort "password".
        return new InMemoryUserDetailsManager(User.builder()
                .username("user")
                .password(passwordEncoder().encode("password"))
                .roles("USER")
                .build());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

