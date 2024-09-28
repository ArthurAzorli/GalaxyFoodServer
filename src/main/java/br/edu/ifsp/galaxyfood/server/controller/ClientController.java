package br.edu.ifsp.galaxyfood.server.controller;

import br.edu.ifsp.galaxyfood.server.model.dto.*;
import br.edu.ifsp.galaxyfood.server.model.service.ClientService;
import br.edu.ifsp.galaxyfood.server.utils.ErrorMessage;
import br.edu.ifsp.galaxyfood.server.utils.ExceptionController;
import jakarta.servlet.http.HttpSession;
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
    public ResponseEntity<Object> login(@RequestBody LoginDTO dto, HttpSession session) {
        try {
            var client = service.login(dto.login(), dto.password());

            session.setAttribute("user", client.getId());
            session.setAttribute("type", "client");

            var data = new HashMap<String, Object>();
            data.put("message", "Login realizado com sucesso!");
            data.put("result", true);

            return ResponseEntity.ok(data);

        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Object> create(@RequestBody InClientDTO dto, HttpSession session) {
       try {

           var client = service.create(dto);

           session.setAttribute("user", client.getId());
           session.setAttribute("type", "client");

           return ResponseEntity.status(201).body(client.toDTO());

       } catch (ExceptionController e) {
           return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
       }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Object> get(@PathVariable("id") UUID id){
        try {
            var client = service.get(id);
            return ResponseEntity.status(302).body(client.toDTO());

        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Object> update(@PathVariable("id") UUID id,@RequestBody InClientDTO dto, HttpSession session){
        try {

            var client = service.update(dto,id);
            return ResponseEntity.status(202).body(client.toDTO());

        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

    @PutMapping("/changepassword/{id}")
    public ResponseEntity<Object> changePassword(@PathVariable("id") UUID id,@RequestBody PasswordDTO dto, HttpSession session) {
        try {
            service.changePassword(dto.oldPassword(), dto.newPassword(), id);

            var data = new HashMap<String, Object>();
            data.put("message", "Senha atualizada com sucesso!");
            data.put("result", true);

            return ResponseEntity.status(202).body(data);

        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

    @PutMapping("/addphone/{id}")
    public ResponseEntity<Object> addPhone(@PathVariable("id") UUID id,@RequestBody InPhoneDTO dto, HttpSession session){
        try {
            var client = service.addPhone(dto.phone(), id);
            return ResponseEntity.status(201).body(client.toDTO());

        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

    @PutMapping("/addaddress")
    public ResponseEntity<Object> addAddress(@PathVariable("id") UUID id,@RequestBody InAddressDTO dto, HttpSession session){
        try {
            var client = service.addAddress(dto, id);
            return ResponseEntity.status(201).body(client.toDTO());

        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

    @DeleteMapping("/remphone/{clientId}/{id}")
    public ResponseEntity<Object> remPhone( @PathVariable("clientId") UUID clientId, @PathVariable("id") UUID id) {
        try {
            var client = service.remPhone(clientId, id);
            return ResponseEntity.ok(client.toDTO());
        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

    @DeleteMapping("/remaddress/{id}/{clientId}")
    public ResponseEntity<Object> remAddress( @PathVariable("clientId") UUID clientId, @PathVariable("id") UUID id) {
        try {
            var client = service.remAddress(id, clientId);
            return ResponseEntity.ok(client.toDTO());
        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Object> delete(@PathVariable("clientId") UUID clientId) {
        try {
            service.delete(clientId);

            var data = new HashMap<String, Object>();
            data.put("message", "Cliente deletado com sucesso!");
            data.put("result", true);

            return ResponseEntity.ok(data);
        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }
}
