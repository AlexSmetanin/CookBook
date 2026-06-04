package chpt.cookbook.controller;

import chpt.cookbook.entity.User;
import chpt.cookbook.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        super();
        this.userService = userService;
    }

    @GetMapping("/login")
    public String showLogin(Model model) {
        // creat user object to hold user form data
        User user = new User();
        model.addAttribute("user", user);
        model.addAttribute("message", "");
        return "login" ;
    }

    @PostMapping("/login")
    public String findUser(@ModelAttribute("user") User user, Model model) {
        List<User> userList = userService.getAllUsers();
        for (User currentUser:userList) {
            if (user.getLogin().equals(currentUser.getLogin()) &&
                    user.getPassword().equals(currentUser.getPassword())) {
                return "redirect:/recipes";
            }
        }
        model.addAttribute("message", "Невірний логін або пароль!");
        return "login";
    }
}
