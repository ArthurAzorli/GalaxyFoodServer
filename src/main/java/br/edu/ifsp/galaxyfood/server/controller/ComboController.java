package br.edu.ifsp.galaxyfood.server.controller;

import br.edu.ifsp.galaxyfood.server.model.dto.InComboDTO;
import br.edu.ifsp.galaxyfood.server.model.dto.InComboItemDTO;
import br.edu.ifsp.galaxyfood.server.model.dto.OutComboDTO;
import br.edu.ifsp.galaxyfood.server.model.service.ComboService;
import br.edu.ifsp.galaxyfood.server.utils.ErrorMessage;
import br.edu.ifsp.galaxyfood.server.utils.ExceptionController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/combo")
public class ComboController {

    @Autowired
    private ComboService service;

    @PostMapping("/create/{idRestaurant}")
    public ResponseEntity<Object> create(@PathVariable("idRestaurant") UUID idRestaurant, @RequestBody InComboDTO dto){
        try {
            var combo = service.create(idRestaurant, dto);
            return ResponseEntity.status(201).body(combo.comboToDTO());
        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Object> get(@PathVariable("id") UUID id){
        try {
            var combo = service.get(id);
            return ResponseEntity.status(302).body(combo.comboToDTO());
        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

    @GetMapping("/get")
    public ResponseEntity<Object> getAll(){
        try {
            var combos = service.getAll();

            List<OutComboDTO> list = new ArrayList<>();
            for (var combo : combos) list.add(combo.comboToDTO());

            return ResponseEntity.status(302).body(list);
        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

    @GetMapping("/get/restaurant/{id}")
    public ResponseEntity<Object> getAll(@PathVariable("id") UUID idRestaurant){
        try {
            var combos = service.getAllByRestaurant(idRestaurant);

            List<OutComboDTO> list = new ArrayList<>();
            for (var combo : combos) list.add(combo.comboToDTO());

            return ResponseEntity.status(302).body(list);
        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

    @PutMapping("/update/{idRestaurant}/{id}")
    public ResponseEntity<Object> update(@PathVariable("idRestaurant") UUID idRestaurant, @PathVariable("id") UUID idCombo, @RequestBody InComboDTO dto){
        try {
            var combo = service.update(idRestaurant,idCombo, dto);
            return ResponseEntity.status(202).body(combo.comboToDTO());
        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

    @PutMapping("/move/{idRestaurant}/{idCombo}/{idPackage}")
    public ResponseEntity<Object> move(@PathVariable("idRestaurant") UUID idRestaurant, @PathVariable("idCombo") UUID idCombo, @PathVariable("idPackage") UUID idParent){
        try {
            var combo = service.move(idRestaurant,idCombo, idParent);
            return ResponseEntity.status(202).body(combo.comboToDTO());

        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

    @PutMapping("/addfood/{idRestaurant}")
    public ResponseEntity<Object> addFood(@PathVariable("idRestaurant") UUID idRestaurant, @RequestBody InComboItemDTO dto){
         try {
            var combo = service.addFood(idRestaurant, dto);
            return ResponseEntity.status(201).body(combo.comboToDTO());
        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

    @DeleteMapping("/remfood/{idRestaurant}/{id}")
    public ResponseEntity<Object> remPhone(@PathVariable("idRestaurant") UUID idRestaurant, @PathVariable("id") UUID id){
        try {
            var combo = service.remFood(idRestaurant, id);
            return ResponseEntity.ok(combo.comboToDTO());

        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

    @DeleteMapping("/delete/{idRestaurant}/{id}")
    public ResponseEntity<Object> delete(@PathVariable("idRestaurant") UUID idRestaurant, @PathVariable("id") UUID idCombo){
        try {
            service.delete(idRestaurant, idCombo);

            var data = new HashMap<String, Object>();
            data.put("message", "Combo deletado com Sucesso!");
            data.put("result", true);

            return ResponseEntity.ok(data);
        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }
}
