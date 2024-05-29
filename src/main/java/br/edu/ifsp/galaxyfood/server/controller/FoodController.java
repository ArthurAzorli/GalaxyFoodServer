package br.edu.ifsp.galaxyfood.server.controller;

import br.edu.ifsp.galaxyfood.server.model.dto.InFoodDTO;
import br.edu.ifsp.galaxyfood.server.model.service.FoodService;
import br.edu.ifsp.galaxyfood.server.utils.ErrorMessage;
import br.edu.ifsp.galaxyfood.server.utils.ExceptionController;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/food")
public class FoodController {

    @Autowired
    private FoodService service;

    /*@PostMapping("/create")
    public ResponseEntity<Object> create(@RequestBody InFoodDTO dto, HttpSession session){
        try {
            var food = service.create(dto, session);
            return ResponseEntity.status(201).body(food.toDTO());
        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }*/

}
