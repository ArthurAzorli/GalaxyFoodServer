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

    @PostMapping("/create/{idRestaurant}")
    public ResponseEntity<Object> create(@PathVariable("idRestaurant") UUID idRestaurant, @RequestBody InFoodDTO dto){
        try {
            var food = service.create(idRestaurant, dto);
            return ResponseEntity.status(201).body(food.foodToDTO());
        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

    @GetMapping("/get/{idRestaurant}/{id}")
    public ResponseEntity<Object> get(@PathVariable("idRestaurant") UUID idRestaurant, @PathVariable("id") UUID id){
        try {
            var food = service.get(idRestaurant, id);
            return ResponseEntity.status(302).body(food.foodToDTO());
        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

    @GetMapping("/get/{idRestaurant}")
    public ResponseEntity<Object> getAll(@PathVariable("idRestaurant") UUID idRestaurant){
        try {
            var foods = service.getAllByRestaurant(idRestaurant);

            List<OutFoodDTO> list = new ArrayList<>();
            for (var food : foods) list.add(food.foodToDTO());

            return ResponseEntity.status(302).body(list);
        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

    @PutMapping("/update/{idRestaurant}/{id}")
    public ResponseEntity<Object> update(@PathVariable("idRestaurant") UUID idRestaurant, @PathVariable("id") UUID idFood, @RequestBody InFoodDTO dto){
        try {
            var food = service.update(idRestaurant, idFood, dto);
            return ResponseEntity.status(202).body(food.foodToDTO());
        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

    @PutMapping("/move/{idRestaurant}/{idFood}/{idPackage}")
    public ResponseEntity<Object> move(@PathVariable("idRestaurant") UUID idRestaurant, @PathVariable("idFood") UUID idFood, @PathVariable("idPackage") UUID idParent){
        try {

            var food = service.move(idRestaurant, idFood, idParent);
            return ResponseEntity.status(202).body(food.foodToDTO());

        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

    @DeleteMapping("/delete/{idRestaurant}/{id}")
    public ResponseEntity<Object> delete(@PathVariable("idRestaurant") UUID idRestaurant, @PathVariable("id") UUID idFood){
        try {
            service.delete(idRestaurant, idFood);

            var data = new HashMap<String, Object>();
            data.put("message", "Alimento deletado com Sucesso!");
            data.put("result", true);

            return ResponseEntity.ok(data);
        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

}
