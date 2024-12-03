package br.edu.ifsp.galaxyfood.server.controller;

import br.edu.ifsp.galaxyfood.server.model.dto.*;
import br.edu.ifsp.galaxyfood.server.model.service.ClientService;
import br.edu.ifsp.galaxyfood.server.utils.ErrorMessage;
import br.edu.ifsp.galaxyfood.server.utils.ExceptionController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.UUID;

@RestController
@RequestMapping("/client")
public class ClientController {

    @Autowired
    private ClientService service;

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginDTO dto) {
        try {
            var client = service.login(dto.login(), dto.password());

            var data = new HashMap<String, Object>();
            data.put("message", "Login realizado com sucesso!");
            data.put("result", true);
            data.put("data", client.getId());

            return ResponseEntity.ok(data);

        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Object> create(@RequestBody InClientDTO dto) {
       try {

           var client = service.create(dto);
           return ResponseEntity.status(201).body(client.toDTO());

       } catch (ExceptionController e) {
           return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
       }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Object> get(@PathVariable("id") UUID id){
        try {
            var client = service.get(id);
            return ResponseEntity.status(200).body(client.toDTO());

        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Object> update(@PathVariable("id") UUID id, @RequestBody InClientDTO dto){
        try {

            var client = service.update(id, dto);
            return ResponseEntity.status(202).body(client.toDTO());

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
            var client = service.addPhone(id, dto.phone());
            return ResponseEntity.status(201).body(client.toDTO());

        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

    @PutMapping("/addaddress/{id}")
    public ResponseEntity<Object> addAddress(@PathVariable("id") UUID id, @RequestBody InAddressDTO dto){
        try {
            var client = service.addAddress(id, dto);
            return ResponseEntity.status(201).body(client.toDTO());

        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

    @DeleteMapping("/remphone/{id}/{idPhone}")
    public ResponseEntity<Object> remPhone(@PathVariable("id") UUID id, @PathVariable("idPhone") UUID idPhone){
        try {
            var client = service.remPhone(id, idPhone);
            return ResponseEntity.ok(client.toDTO());

        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

    @DeleteMapping("/remaddress/{id}/{idAddress}")
    public ResponseEntity<Object> remAddress(@PathVariable("id") UUID id, @PathVariable("idAddress") UUID idAddress){
        try {
            var client = service.remAddress(id, idAddress);
            return ResponseEntity.ok(client.toDTO());

        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Object> delete(@RequestBody LoginDTO dto){
        try {
            service.delete(dto);

            var data = new HashMap<String, Object>();
            data.put("message", "Cliente deletado com Sucesso!");
            data.put("result", true);

            return ResponseEntity.ok(data);
        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }
}
