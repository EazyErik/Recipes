package recipes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class LoginService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;


    public boolean isValidEmail(String email) {
        if (Objects.isNull(email) || !email.contains("@") || !email.contains(".")) {
            return false;
        }
        return true;
    }

    public boolean isValidPassword(String password) {
        if (Objects.isNull(password) || password.length() < 8 || password.isBlank()) {
            return false;
        }
        return true;
    }

    public boolean existsEmail(String email) {
        Iterable<User> all = userRepository.findAll();
        AtomicBoolean response = new AtomicBoolean(false);

        all.forEach((value) -> {
            if(value.getEmail().equals(email)){
                response.set(true);
            }
        });
        return response.get();
    }

    public void saveUser(User user){
        String encode = passwordEncoder.encode(user.getPassword());
        System.out.println(encode);
        user.setPassword(encode);
        userRepository.save(user);
    }
}
