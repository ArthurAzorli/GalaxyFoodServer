package br.edu.ifsp.galaxyfood.server.controller;


import br.edu.ifsp.galaxyfood.server.model.dto.InRestaurantOwnerDTO;
import br.edu.ifsp.galaxyfood.server.model.service.RestaurantOwnerService;
import br.edu.ifsp.galaxyfood.server.utils.ErrorMessage;
import br.edu.ifsp.galaxyfood.server.utils.ExceptionController;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.UUID;

@RestController
@RequestMapping("/restaurantowner")
public class RestaurantOwnerController {

    @Autowired
    private RestaurantOwnerService service;

    @PostMapping("/create")
    public ResponseEntity<Object> create(@RequestBody InRestaurantOwnerDTO dto){
        try {
            var owner = service.create(dto);
            return ResponseEntity.status(201).body(owner.toDTO());
        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Object> get(@PathVariable("id") UUID id){
        try {
            var owner = service.get(id);
            return ResponseEntity.status(302).body(owner.toDTO());
        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Object> update(@PathVariable("restaurantId") UUID restaurantId, @RequestBody InRestaurantOwnerDTO dto) {
        try {
            var owner = service.update(dto, restaurantId);
            return ResponseEntity.status(202).body(owner.toDTO());
        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Object> delete(@PathVariable("restaurantId") UUID restaurantId) {
        try {
            service.delete(restaurantId);
            var data = new HashMap<String, Object>();
            data.put("message", "Dono deletado com Sucesso!");
            data.put("result", true);
            return ResponseEntity.ok(data);
        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }
}
