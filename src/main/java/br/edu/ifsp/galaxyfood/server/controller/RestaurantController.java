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
    public ResponseEntity<Object> getAll(@RequestHeader(value = "User-Type") String userType) {
        try {
            var restaurants = service.getAll(userType);
            List<OutRestaurantDTO> list = new ArrayList<>();
            for (var restaurant : restaurants) list.add(restaurant.toDTO());
            return ResponseEntity.status(302).body(list);
        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));

        }
    }


    @GetMapping("/getOfLocal")
    public ResponseEntity<Object> getAllOfLocal(@RequestParam(value = "address") UUID addressId, @RequestParam(value = "clientId") UUID clientId){
        try {
            var restaurants = service.getAllOfLocal(addressId, clientId);
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
            var restaurants = service.search(text);
            List<OutRestaurantDTO> list = new ArrayList<>();
            for (var restaurant : restaurants) list.add(restaurant.toDTO());
            return ResponseEntity.status(302).body(list);

        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

    @GetMapping("/searchOfLocal")
    public ResponseEntity<Object> searchOfLocal(@RequestParam(value = "text") String text, @RequestParam(value = "address") UUID addressId, @RequestParam(value = "clientId") UUID clientId){
        try {
            var restaurants = service.searchOfLocal(text, addressId, clientId);
            List<OutRestaurantDTO> list = new ArrayList<>();
            for (var restaurant : restaurants) list.add(restaurant.toDTO());
            return ResponseEntity.status(302).body(list);

        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Object> update(@RequestBody InRestaurantDTO dto, @PathVariable("id") UUID restaurantId){
        try {
            var restaurant = service.update(dto, restaurantId);
            return ResponseEntity.status(202).body(restaurant.toDTO());

        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

    @PutMapping("/changepassword")
    public ResponseEntity<Object> changePassword(@RequestBody PasswordDTO dto, @RequestParam("restaurantId") UUID restaurantId) {
        try {
            service.changePassword(dto.oldPassword(), dto.newPassword(), restaurantId);

            var data = new HashMap<String, Object>();
            data.put("message", "Senha atualizada com sucesso!");
            data.put("result", true);

            return ResponseEntity.status(202).body(data);

        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

    @PutMapping("/addphone")
    public ResponseEntity<Object> addPhone(@RequestBody InPhoneDTO dto, @RequestParam("restaurantId") UUID restaurantId){
        try {
            var restaurant = service.addPhone(dto.phone(), restaurantId);
            return ResponseEntity.status(201).body(restaurant.toDTO());

        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }


    @PutMapping("/score/{id}")
    public ResponseEntity<Object> score(@PathVariable("id") UUID idRestaurant, @RequestBody InScoreDTO dto, @RequestParam("clientId") UUID clientId){
        try {
            var restaurant = service.score(idRestaurant, clientId, dto);
            return ResponseEntity.status(202).body(restaurant.toDTO());

        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

    @DeleteMapping("/remphone/{id}")
    public ResponseEntity<Object> remPhone(@PathVariable("id") UUID idPhone, @RequestParam("restaurantId") UUID restaurantId){
        try {
            var restaurant = service.remPhone(idPhone, restaurantId);
            return ResponseEntity.ok(restaurant.toDTO());

        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Object> delete(@PathVariable("id") UUID restaurantId){
        try {
            service.delete(restaurantId);


            var data = new HashMap<String, Object>();
            data.put("message", "Restaurante deletado com Sucesso!");
            data.put("result", true);
            return ResponseEntity.ok(data);
        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }
}
