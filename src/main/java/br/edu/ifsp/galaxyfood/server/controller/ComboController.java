package br.edu.ifsp.galaxyfood.server.controller;

import br.edu.ifsp.galaxyfood.server.model.domain.Combo;
import br.edu.ifsp.galaxyfood.server.model.dto.InComboDTO;
import br.edu.ifsp.galaxyfood.server.model.service.ComboService;
import br.edu.ifsp.galaxyfood.server.utils.ErrorMessage;
import br.edu.ifsp.galaxyfood.server.utils.ExceptionController;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;

@RestController
@RequestMapping("/combo")
public class ComboController {

    @Autowired
    private ComboService service;

    @PostMapping("/create")
    public ResponseEntity<Object> create(@RequestBody InComboDTO dto, HttpSession session){
        try {
            var combo = service.create(dto, session);
            return ResponseEntity.status(201).body(combo.toDTO());
        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }
}
