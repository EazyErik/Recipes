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
public class LoginController {
    @Autowired
    LoginService loginService;

    @PostMapping("/register/1")
    public ResponseEntity<Object> register(@RequestBody String email,@RequestBody String password){
        System.out.println("register");
         if(!loginService.isValidEmail(email) || !loginService.isValidPassword(password) || loginService.existsEmail(email)){
             return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
         }
         return new ResponseEntity<>(HttpStatus.OK);
    }
}
