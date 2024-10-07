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
    public ResponseEntity<Object> login(@RequestBody LoginDTO dto) {
        try {
            var restaurant = service.login(dto.login(), dto.password());

            var data = new HashMap<String, Object>();
            data.put("message", "Login realizado com sucesso!");
            data.put("result", true);
            data.put("data", restaurant.getId());

            return ResponseEntity.ok(data);

        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Object> create(@RequestBody InRestaurantDTO dto) {
        try {
            var restaurant = service.create(dto);

            return ResponseEntity.status(201).body(restaurant.toDTO());

        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Object> get(@PathVariable("id") UUID id){
        try {
            var restaurant = service.get(id);
            return ResponseEntity.status(200).body(restaurant.toDTO());

        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

    @GetMapping("/get")
    public ResponseEntity<Object> getAll(){
        try {
            var restaurants = service.getAll();
            List<OutRestaurantDTO> list = new ArrayList<>();
            for (var restaurant : restaurants) list.add(restaurant.toDTO());
            return ResponseEntity.status(200).body(list);

        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

    @GetMapping("/getLocal")
    public ResponseEntity<Object> getAll(@RequestParam(value = "address") UUID address){
        try {
            var restaurants = service.getAllOfLocal(address);
            List<OutRestaurantDTO> list = new ArrayList<>();
            for (var restaurant : restaurants) list.add(restaurant.toDTO());
            return ResponseEntity.status(200).body(list);

        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(@RequestParam(value = "text") String text){
        try {
            var restaurants = service.search(text);
            List<OutRestaurantDTO> list = new ArrayList<>();
            for (var restaurant : restaurants) list.add(restaurant.toDTO());
            return ResponseEntity.status(200).body(list);

        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

    @GetMapping("/searchLocal")
    public ResponseEntity<Object> search(@RequestParam(value = "text") String text, @RequestParam(value = "address") UUID address){
        try {
            var restaurants = service.searchOfLocal(text, address);
            List<OutRestaurantDTO> list = new ArrayList<>();
            for (var restaurant : restaurants) list.add(restaurant.toDTO());
            return ResponseEntity.status(200).body(list);

        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Object> update(@PathVariable("id") UUID id, @RequestBody InRestaurantDTO dto){
        try {

            var restaurant = service.update(id, dto);
            return ResponseEntity.status(202).body(restaurant.toDTO());

        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

    @PutMapping("/changepassword/{id}")
    public ResponseEntity<Object> changePassword(@PathVariable("id") UUID id, @RequestBody PasswordDTO dto) {
        try {
            service.changePassword(id, dto.oldPassword(), dto.newPassword());

            var data = new HashMap<String, Object>();
            data.put("message", "Senha atualizada com sucesso!");
            data.put("result", true);

            return ResponseEntity.status(202).body(data);

        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

    @PutMapping("/addphone/{id}")
    public ResponseEntity<Object> addPhone(@PathVariable("id") UUID id, @RequestBody InPhoneDTO dto){
        try {
            var restaurant = service.addPhone(id, dto.phone());
            return ResponseEntity.status(201).body(restaurant.toDTO());

        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

    @PutMapping("/score/{idRestaurant}/{idClient}")
    public ResponseEntity<Object> score (@PathVariable("idRestaurant") UUID idRestaurant, @PathVariable("idClient") UUID idClient,  @RequestBody InScoreDTO dto){
        try {
            var restaurant = service.score(idRestaurant, idClient, dto);
            return ResponseEntity.status(202).body(restaurant.toDTO());

        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

    @DeleteMapping("/remphone/{id}/{idPhone}")
    public ResponseEntity<Object> remPhone(@PathVariable("id") UUID id, @PathVariable("idPhone") UUID idPhone){
        try {
            var restaurant = service.remPhone(id, idPhone);
            return ResponseEntity.ok(restaurant.toDTO());

        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Object> delete(@RequestBody LoginDTO dto){
        try {
            service.delete(dto);

            var data = new HashMap<String, Object>();
            data.put("message", "Restaurante deletado com Sucesso!");
            data.put("result", true);

            return ResponseEntity.ok(data);
        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }
}
