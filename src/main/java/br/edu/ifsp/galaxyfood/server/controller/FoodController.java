package br.edu.ifsp.galaxyfood.server.controller;

import br.edu.ifsp.galaxyfood.server.model.dto.InFoodDTO;
import br.edu.ifsp.galaxyfood.server.model.dto.OutFoodDTO;
import br.edu.ifsp.galaxyfood.server.model.service.FoodService;
import br.edu.ifsp.galaxyfood.server.utils.ErrorMessage;
import br.edu.ifsp.galaxyfood.server.utils.ExceptionController;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/food")
public class FoodController {

    @Autowired
    private FoodService service;

    @PostMapping("/create")
    public ResponseEntity<Object> create(@RequestBody InFoodDTO dto, HttpSession session) {
        UUID restaurantId = (UUID) session.getAttribute("restaurantId"); // Exemplo de como obter o restaurantId da sessão
        try {
            var food = service.create(dto, restaurantId);

            var data = new HashMap<String, Object>();
            data.put("message", "Food item created successfully!");
            data.put("result", true);
            data.put("food", food.toDTO()); // Converta para DTO se necessário

            return ResponseEntity.status(201).body(data);

        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Object> get(@PathVariable("id") UUID id) {
        try {
            var food = service.get(id);
            return ResponseEntity.ok(food.toDTO()); // Converta para DTO se necessário

        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Object> update(@PathVariable("id") UUID id, @RequestBody InFoodDTO dto, HttpSession session) {
        UUID restaurantId = (UUID) session.getAttribute("restaurantId"); // Exemplo de como obter o restaurantId da sessão
        try {
            var food = service.update(id, dto, restaurantId);
            return ResponseEntity.status(202).body(food.toDTO()); // Converta para DTO se necessário

        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") UUID id, HttpSession session) {
        UUID restaurantId = (UUID) session.getAttribute("restaurantId"); // Exemplo de como obter o restaurantId da sessão
        try {
            service.delete(id, restaurantId);

            var data = new HashMap<String, Object>();
            data.put("message", "Food item deleted successfully!");
            data.put("result", true);

            return ResponseEntity.ok(data);
        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

}
