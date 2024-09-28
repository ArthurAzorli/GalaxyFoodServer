package br.edu.ifsp.galaxyfood.server.controller;

import br.edu.ifsp.galaxyfood.server.model.dto.InPackageDTO;
import br.edu.ifsp.galaxyfood.server.model.dto.OutPackageDTO;
import br.edu.ifsp.galaxyfood.server.model.service.PackageService;
import br.edu.ifsp.galaxyfood.server.utils.ErrorMessage;
import br.edu.ifsp.galaxyfood.server.utils.ExceptionController;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<Object> create(@RequestBody InPackageDTO dto, @RequestParam UUID userId) {
        try {
            var pack = service.create(dto, userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(pack.toDTO());
        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorMessage("An unexpected error occurred.", HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Object> get(@PathVariable("id") UUID id) {
        try {
            var pack = service.get(id);
            return ResponseEntity.ok(pack.toDTO());
        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorMessage("An unexpected error occurred.", HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }

    @GetMapping("/get")
    public ResponseEntity<Object> getAll(@RequestParam UUID userId) {
        try {
            var packages = service.getAll(userId);
            List<OutPackageDTO> list = new ArrayList<>();
            for (var pack : packages) {
                list.add(pack.toDTO());
            }
            return ResponseEntity.ok(list);
        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorMessage("An unexpected error occurred.", HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }

    @GetMapping("/get/restaurant/{id}")
    public ResponseEntity<Object> getAllByRestaurant(@PathVariable("id") UUID idRestaurant) {
        try {
            var packages = service.getAll(idRestaurant);
            List<OutPackageDTO> list = new ArrayList<>();
            for (var pack : packages) {
                list.add(pack.toDTO());
            }
            return ResponseEntity.ok(list);
        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorMessage("An unexpected error occurred.", HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Object> update(@PathVariable("id") UUID idPackage, @RequestBody InPackageDTO dto, @RequestParam UUID userId) {
        try {
            var pack = service.update(idPackage, dto, userId);
            return ResponseEntity.ok(pack.toDTO());
        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorMessage("An unexpected error occurred.", HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") UUID idPackage, @RequestParam UUID userId) {
        try {
            service.delete(idPackage, userId);
            var response = new HashMap<String, Object>();
            response.put("message", "Pasta deletada com sucesso!");
            response.put("result", true);
            return ResponseEntity.ok(response);
        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorMessage("An unexpected error occurred.", HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }
}
