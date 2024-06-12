package br.edu.ifsp.galaxyfood.server.controller;

import br.edu.ifsp.galaxyfood.server.model.dto.InComboDTO;
import br.edu.ifsp.galaxyfood.server.model.dto.InComboItemDTO;
import br.edu.ifsp.galaxyfood.server.model.dto.OutComboDTO;
import br.edu.ifsp.galaxyfood.server.model.service.ComboService;
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
@RequestMapping("/combo")
public class ComboController {

    @Autowired
    private ComboService service;

    @PostMapping("/create")
    public ResponseEntity<Object> create(@RequestBody InComboDTO dto, HttpSession session){
        try {
            var combo = service.create(dto, session);
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
    public ResponseEntity<Object> getAll(HttpSession session){
        try {
            var combos = service.getAll(session);

            List<OutComboDTO> list = new ArrayList<>();
            for (var combo : combos) list.add(combo.comboToDTO());

            return ResponseEntity.status(302).body(list);
        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

    @GetMapping("/get/restaurant/{id}")
    public ResponseEntity<Object> getAll(@PathVariable("id") UUID idRestaurant, HttpSession session){
        try {
            var combos = service.getAll(idRestaurant, session);

            List<OutComboDTO> list = new ArrayList<>();
            for (var combo : combos) list.add(combo.comboToDTO());

            return ResponseEntity.status(302).body(list);
        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Object> update(@PathVariable("id") UUID idCombo, @RequestBody InComboDTO dto, HttpSession session){
        try {
            var combo = service.update(idCombo, dto, session);
            return ResponseEntity.status(202).body(combo.comboToDTO());
        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

    @PutMapping("/move/{idCombo}/{idPackage}")
    public ResponseEntity<Object> move(@PathVariable("idCombo") UUID idCombo, @PathVariable("idPackage") UUID idParent, HttpSession session){
        try {
            var combo = service.move(idCombo, idParent, session);
            return ResponseEntity.status(202).body(combo.comboToDTO());

        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

    @PutMapping("/addfood")
    public ResponseEntity<Object> addFood(@RequestBody InComboItemDTO dto, HttpSession session){
        try {
            var combo = service.addFood(dto, session);
            return ResponseEntity.status(201).body(combo.comboToDTO());
        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

    @DeleteMapping("/remfood/{id}")
    public ResponseEntity<Object> remPhone(@PathVariable("id") UUID id, HttpSession session){
        try {
            var combo = service.remFood(id, session);
            return ResponseEntity.ok(combo.comboToDTO());

        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") UUID idCombo, HttpSession session){
        try {
            service.delete(idCombo, session);

            var data = new HashMap<String, Object>();
            data.put("message", "Combo deletado com Sucesso!");
            data.put("result", true);

            return ResponseEntity.ok(data);
        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }
}
