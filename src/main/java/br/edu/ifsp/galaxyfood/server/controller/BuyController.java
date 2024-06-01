package br.edu.ifsp.galaxyfood.server.controller;

import br.edu.ifsp.galaxyfood.server.model.dto.InBuyDTO;
import br.edu.ifsp.galaxyfood.server.model.dto.OutBuyDTO;
import br.edu.ifsp.galaxyfood.server.model.service.BuyService;
import br.edu.ifsp.galaxyfood.server.utils.ErrorMessage;
import br.edu.ifsp.galaxyfood.server.utils.ExceptionController;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/buy")
public class BuyController {

    @Autowired
    private BuyService service;

    @PostMapping("/create")
    public ResponseEntity<Object> create(@RequestBody InBuyDTO dto, HttpSession session){
        try {
            var buy = service.create(dto, session);
            return ResponseEntity.status(201).body(buy.toDTO());
        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Object> get(@PathVariable("id") UUID id, HttpSession session){
        try {
            var buy = service.get(id, session);
            return ResponseEntity.status(302).body(buy.toDTO());
        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

    @GetMapping("/get")
    public ResponseEntity<Object> getAll(HttpSession session){
        try {
            var buys = service.getAll(session);

            List<OutBuyDTO> list = new ArrayList<>();
            for (var buy : buys) list.add(buy.toDTO());

            return ResponseEntity.status(302).body(list);
        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }
}
