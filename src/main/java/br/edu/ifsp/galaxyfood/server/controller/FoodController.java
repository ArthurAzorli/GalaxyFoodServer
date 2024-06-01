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
    public ResponseEntity<Object> create(@RequestBody InFoodDTO dto, HttpSession session){
        try {
            var food = service.create(dto, session);
            return ResponseEntity.status(201).body(food.foodToDTO());
        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Object> get(@PathVariable("id") UUID id){
        try {
            var food = service.get(id);
            return ResponseEntity.status(302).body(food.foodToDTO());
        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

    @GetMapping("/get")
    public ResponseEntity<Object> getAll(HttpSession session){
        try {
            var foods = service.getAll(session);

            List<OutFoodDTO> list = new ArrayList<>();
            for (var food : foods) list.add(food.foodToDTO());

            return ResponseEntity.status(302).body(list);
        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

    @GetMapping("/get/restaurant/{id}")
    public ResponseEntity<Object> getAll(@PathVariable("id") UUID idRestaurant, HttpSession session){
        try {
            var foods = service.getAll(idRestaurant, session);

            List<OutFoodDTO> list = new ArrayList<>();
            for (var food : foods) list.add(food.foodToDTO());

            return ResponseEntity.status(302).body(list);
        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Object> update(@PathVariable("id") UUID idFood, @RequestBody InFoodDTO dto, HttpSession session){
        try {
            var food = service.update(idFood, dto, session);
            return ResponseEntity.status(202).body(food.foodToDTO());
        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

    @PutMapping("/move/{idFood}/{idPackage}")
    public ResponseEntity<Object> move(@PathVariable("idFood") UUID idFood, @PathVariable("idPackage") UUID idParent, HttpSession session){
        try {

            var food = service.move(idFood, idParent, session);
            return ResponseEntity.status(202).body(food.foodToDTO());

        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") UUID idFood, HttpSession session){
        try {
            service.delete(idFood, session);

            var data = new HashMap<String, Object>();
            data.put("message", "Alimento deletado com Sucesso!");
            data.put("result", true);

            return ResponseEntity.ok(data);
        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

}
