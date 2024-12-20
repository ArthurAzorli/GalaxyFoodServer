package br.edu.ifsp.galaxyfood.server.controller;

import br.edu.ifsp.galaxyfood.server.model.dto.InPackageDTO;
import br.edu.ifsp.galaxyfood.server.model.dto.OutPackageDTO;
import br.edu.ifsp.galaxyfood.server.model.service.PackageService;
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
@RequestMapping("/package")
public class PackageController {

    @Autowired
    private PackageService service;

    @PostMapping("/create/{idRestaurant}")
    public ResponseEntity<Object> create(@PathVariable("idRestaurant") UUID idRestaurant, @RequestBody InPackageDTO dto){
        try {
            var pack = service.create(idRestaurant, dto);
            return ResponseEntity.status(201).body(pack.toDTO());
        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

    @GetMapping("/get/{idRestaurant}/{id}")
    public ResponseEntity<Object> get(@PathVariable("idRestaurant") UUID idRestaurant, @PathVariable("id") UUID id){
        try {
            var pack = service.get(idRestaurant, id);
            return ResponseEntity.status(200).body(pack.toDTO());
        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

    @GetMapping("/get/{idRestaurant}")
    public ResponseEntity<Object> getAll(@PathVariable("idRestaurant") UUID idRestaurant){
        try {
            var packages = service.getAllByRestaurant(idRestaurant);

            List<OutPackageDTO> list = new ArrayList<>();
            for (var pack : packages) list.add(pack.toDTO());

            return ResponseEntity.status(200).body(list);
        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

    @GetMapping("/root/{idRestaurant}")
    public ResponseEntity<Object> getRoot(@PathVariable("idRestaurant") UUID idRestaurant){
        try {
            var pack = service.getRoot(idRestaurant);
            return ResponseEntity.status(200).body(pack.toDTO());
        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

    @PutMapping("/update/{idRestaurant}/{id}")
    public ResponseEntity<Object> update(@PathVariable("idRestaurant") UUID idRestaurant, @PathVariable("id") UUID idPackage, @RequestBody InPackageDTO dto){
        try {
            var pack = service.update(idRestaurant, idPackage, dto);
            return ResponseEntity.status(202).body(pack.toDTO());
        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

    @PutMapping("/move/{idRestaurant}/{idPackage}/{idParent}")
    public ResponseEntity<Object> move(@PathVariable("idRestaurant") UUID idRestaurant, @PathVariable("idPackage") UUID idPackage, @PathVariable("idParent") UUID idParent){
        try {

            var pack = service.move(idRestaurant, idPackage, idParent);
            return ResponseEntity.status(202).body(pack.toDTO());

        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

    @DeleteMapping("/delete/{idRestaurant}/{id}")
    public ResponseEntity<Object> delete(@PathVariable("idRestaurant") UUID idRestaurant, @PathVariable("id") UUID idPackage){
        try {
            service.delete(idRestaurant, idPackage);

            var data = new HashMap<String, Object>();
            data.put("message", "Pasta deletada com Sucesso!");
            data.put("result", true);

            return ResponseEntity.ok(data);
        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }
}
