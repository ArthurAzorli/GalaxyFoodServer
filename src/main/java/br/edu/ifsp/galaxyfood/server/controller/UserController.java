package br.edu.ifsp.galaxyfood.server.controller;

import br.edu.ifsp.galaxyfood.server.utils.ErrorMessage;
import br.edu.ifsp.galaxyfood.server.utils.ExceptionController;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping("/session")
    public ResponseEntity<Object> getSession(HttpSession session){
        try {
            if (session.getAttribute("user") == null) throw new ExceptionController(498, "Você não está logado!");

            var data = new HashMap<String, Object>();

            data.put("user", session.getAttribute("user"));
            data.put("type", session.getAttribute("type"));

            return ResponseEntity.ok(data);
        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

    @PutMapping("/logout")
    public ResponseEntity<Object> logout(HttpSession session){
        try {

            if (session.getAttribute("user") == null) throw new ExceptionController(498, "Você não está logado!");

            session.removeAttribute("user");
            session.removeAttribute("type");

            var data = new HashMap<String, Object>();

            data.put("message", "Deslogado com Sucesso!");
            data.put("result", true);

            return ResponseEntity.ok(data);

        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }
}
