package br.edu.ifsp.galaxyfood.server.controller;

import br.edu.ifsp.galaxyfood.server.model.dto.InPackageDTO;
import br.edu.ifsp.galaxyfood.server.model.dto.OutPackageDTO;
import br.edu.ifsp.galaxyfood.server.model.service.PackageService;
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
@RequestMapping("/package")
public class PackageController {

    @Autowired
    private PackageService service;

    @PostMapping("/create")
    public ResponseEntity<Object> create(@RequestBody InPackageDTO dto, HttpSession session){
        try {
            var pack = service.create(dto, session);
            return ResponseEntity.status(201).body(pack.toDTO());
        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Object> get(@PathVariable("id") UUID id){
        try {
            var pack = service.get(id);
            return ResponseEntity.status(302).body(pack.toDTO());
        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

    @GetMapping("/get")
    public ResponseEntity<Object> getAll(HttpSession session){
        try {
            var packages = service.getAll(session);

            List<OutPackageDTO> list = new ArrayList<>();
            for (var pack : packages) list.add(pack.toDTO());

            return ResponseEntity.status(302).body(list);
        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

    @GetMapping("/get/restaurant/{id}")
    public ResponseEntity<Object> getAll(@PathVariable("id") UUID idRestaurant){
        try {
            var packages = service.getAll(idRestaurant);

            List<OutPackageDTO> list = new ArrayList<>();
            for (var pack : packages) list.add(pack.toDTO());

            return ResponseEntity.status(302).body(list);
        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

    @GetMapping("/root")
    public ResponseEntity<Object> getRoot(HttpSession session){
        try {
            var pack = service.getRoot(session);
            return ResponseEntity.status(302).body(pack.toDTO());
        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

    @GetMapping("/root/{id}")
    public ResponseEntity<Object> getRoot(@PathVariable("id") UUID idRestaurant){
        try {
            var pack = service.getRoot(idRestaurant);
            return ResponseEntity.status(302).body(pack.toDTO());
        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Object> update(@PathVariable("id") UUID idPackage, @RequestBody InPackageDTO dto, HttpSession session){
        try {
            var pack = service.update(idPackage, dto, session);
            return ResponseEntity.status(202).body(pack.toDTO());
        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

    @PutMapping("/move/{idPackage}/{idParent}")
    public ResponseEntity<Object> move(@PathVariable("idPackage") UUID idPackage, @PathVariable("idParent") UUID idParent, HttpSession session){
        try {

            var pack = service.move(idPackage, idParent, session);
            return ResponseEntity.status(202).body(pack.toDTO());

        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") UUID idPackage, HttpSession session){
        try {
            service.delete(idPackage, session);

            var data = new HashMap<String, Object>();
            data.put("message", "Pasta deletada com Sucesso!");
            data.put("result", true);

            return ResponseEntity.ok(data);
        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }
}
