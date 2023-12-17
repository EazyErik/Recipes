package recipes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class RegisterController {
    @Autowired
    LoginService loginService;

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody User user){
        System.out.println("register");
        if(!loginService.isValidEmail(user.getEmail()) || !loginService.isValidPassword(user.getPassword()) || loginService.existsEmail(user.getEmail())){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        loginService.saveUser(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
