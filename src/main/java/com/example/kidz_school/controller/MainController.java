package com.example.kidz_school.controller;

import com.example.kidz_school.dto.AuthRequestDto;
import com.example.kidz_school.security.CurrentUser;
import com.example.kidz_school.entity.User;
import com.example.kidz_school.service.impl.JwtServiceImpl;
import com.example.kidz_school.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final UserServiceImpl userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtServiceImpl jwtService;

    @GetMapping("/")
    public String main(ModelMap modelMap) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof CurrentUser) {
            modelMap.addAttribute("currentUser", principal);
        }
        return "index";
    }

    @GetMapping("/myAccount")
    public String myAccount(ModelMap modelMap) {
        CurrentUser principal = (CurrentUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        modelMap.addAttribute("currentUser", principal);
        return "myAccount";
    }

    @GetMapping("/loginEndpoint")
    public String customLoginPage() {
        return "sign-up-or-login";
    }

    @PostMapping("/loginEndpoint")
    public String customLogin(@ModelAttribute AuthRequestDto authRequestDto, HttpServletResponse response, HttpSession session) {
        User userByEmail = userService.findByEmail(authRequestDto.getEmail());
        if (userByEmail==null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } else if (!passwordEncoder.matches((authRequestDto.getPassword()), userByEmail.getPassword())) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } else if (!userByEmail.isEnabled()) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        } else {
            String token = jwtService.generateToken(userByEmail);
            session.setAttribute("AuthorizationToken", "Bearer " + token);
            return "redirect:/";
        }
        return "redirect:/loginEndpoint";
    }
}
