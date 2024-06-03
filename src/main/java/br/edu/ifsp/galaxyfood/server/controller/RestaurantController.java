package br.edu.ifsp.galaxyfood.server.controller;

import br.edu.ifsp.galaxyfood.server.model.dto.*;
import br.edu.ifsp.galaxyfood.server.model.service.RestaurantService;
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
@RequestMapping("/restaurant")
public class RestaurantController {

    @Autowired
    private RestaurantService service;

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginDTO dto, HttpSession session) {
        try {
            var restaurant = service.login(dto.login(), dto.password());

            session.setAttribute("user", restaurant.getId());
            session.setAttribute("type", "restaurant");

            var data = new HashMap<String, Object>();
            data.put("message", "Login realizado com sucesso!");
            data.put("result", true);

            return ResponseEntity.ok(data);

        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Object> create(@RequestBody InRestaurantDTO dto, HttpSession session) {
        try {
            var restaurant = service.create(dto);

            session.setAttribute("user", restaurant.getId());
            session.setAttribute("type", "restaurant");

            return ResponseEntity.status(201).body(restaurant.toDTO());

        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Object> get(@PathVariable("id") UUID id){
        try {
            var restaurant = service.get(id);
            return ResponseEntity.status(302).body(restaurant.toDTO());

        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

    @GetMapping("/get")
    public ResponseEntity<Object> getAll(HttpSession session){
        try {
            var restaurants = service.getAll(session);
            List<OutRestaurantDTO> list = new ArrayList<>();
            for (var restaurant : restaurants) list.add(restaurant.toDTO());
            return ResponseEntity.status(302).body(list);

        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

    @GetMapping("/getOfLocal")
    public ResponseEntity<Object> getAll(@RequestParam(value = "address") UUID address, HttpSession session){
        try {
            var restaurants = service.getAllOfLocal(address, session);
            List<OutRestaurantDTO> list = new ArrayList<>();
            for (var restaurant : restaurants) list.add(restaurant.toDTO());
            return ResponseEntity.status(302).body(list);

        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(@RequestParam(value = "text") String text, HttpSession session){
        try {
            var restaurants = service.search(text, session);
            List<OutRestaurantDTO> list = new ArrayList<>();
            for (var restaurant : restaurants) list.add(restaurant.toDTO());
            return ResponseEntity.status(302).body(list);

        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

    @GetMapping("/searchOfLocal")
    public ResponseEntity<Object> search(@RequestParam(value = "text") String text, @RequestParam(value = "address") UUID address, HttpSession session){
        try {
            var restaurants = service.searchOfLocal(text, address, session);
            List<OutRestaurantDTO> list = new ArrayList<>();
            for (var restaurant : restaurants) list.add(restaurant.toDTO());
            return ResponseEntity.status(302).body(list);

        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Object> update(@RequestBody InRestaurantDTO dto, HttpSession session){
        try {

            var restaurant = service.update(dto, session);
            return ResponseEntity.status(202).body(restaurant.toDTO());

        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

    @PutMapping("/changepassword")
    public ResponseEntity<Object> changePassword(@RequestBody PasswordDTO dto, HttpSession session) {
        try {
            service.changePassword(dto.oldPassword(), dto.newPassword(), session);

            var data = new HashMap<String, Object>();
            data.put("message", "Senha atualizada com sucesso!");
            data.put("result", true);

            return ResponseEntity.status(202).body(data);

        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

    @PutMapping("/addphone")
    public ResponseEntity<Object> addPhone(@RequestBody PhoneDTO dto, HttpSession session){
        try {
            var restaurant = service.addPhone(dto.phone(), session);
            return ResponseEntity.status(201).body(restaurant.toDTO());

        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

    @PutMapping("/score/{id}")
    public ResponseEntity<Object> score (@PathVariable("id") UUID idRestaurant,  @RequestBody InScoreDTO dto, HttpSession session){
        try {
            var restaurant = service.score(idRestaurant, dto, session);
            return ResponseEntity.status(202).body(restaurant.toDTO());

        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

    @DeleteMapping("/remphone/{id}")
    public ResponseEntity<Object> remPhone(@PathVariable("id") String id, HttpSession session){
        try {
            var restaurant = service.remPhone(id, session);
            return ResponseEntity.ok(restaurant.toDTO());

        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Object> delete(HttpSession session){
        try {
            service.delete(session);

            session.removeAttribute("user");
            session.removeAttribute("type");

            var data = new HashMap<String, Object>();
            data.put("message", "Restaurante deletado com Sucesso!");
            data.put("result", true);

            return ResponseEntity.ok(data);
        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }
}
