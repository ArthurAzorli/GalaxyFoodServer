package br.edu.ifsp.galaxyfood.server.controller;

import br.edu.ifsp.galaxyfood.server.model.dto.AddressDTO;
import br.edu.ifsp.galaxyfood.server.model.dto.InClientDTO;
import br.edu.ifsp.galaxyfood.server.model.dto.PhoneDTO;
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
    public ResponseEntity<Object> login(
            @RequestParam(value = "login") String login,
            @RequestParam(value = "password") String password,
            HttpSession session
    ) {
        try {
            var client = service.login(login, password);

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

    @GetMapping("/session")
    public ResponseEntity<Object> getSession(HttpSession session){
        var data = new HashMap<String, Object>();

        data.put("user", session.getAttribute("user"));
        data.put("type", session.getAttribute("type"));

        return ResponseEntity.status(302).body(data);
    }

    @PutMapping("/update")
    public ResponseEntity<Object> update(@RequestBody InClientDTO dto, HttpSession session){
        try {
            var client = service.update(dto, session);

            return ResponseEntity.status(202).body(client.toDTO());
        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

    @PutMapping("/changepassword")
    public ResponseEntity<Object> changePassword(
        @RequestParam(value = "oldPassword") String oldPassword,
        @RequestParam(value = "newPassword") String newPassword,
        HttpSession session
    ) {
        try {
            service.changePassword(oldPassword, newPassword, session);

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
            var client = service.addPhone(dto.phone(), session);
            return ResponseEntity.status(201).body(client.toDTO());

        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

    @PutMapping("/addaddress")
    public ResponseEntity<Object> addAddress(@RequestBody AddressDTO dto, HttpSession session){
        try {
            var client = service.addAddress(dto, session);
            return ResponseEntity.status(201).body(client.toDTO());

        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

    @DeleteMapping("/remphone/{id}")
    public ResponseEntity<Object> remPhone(@PathVariable("id") String id, HttpSession session){
        try {
            var client = service.remPhone(id, session);
            return ResponseEntity.status(201).body(client.toDTO());

        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

    @DeleteMapping("/remaddress/{id}")
    public ResponseEntity<Object> remAddress(@PathVariable("id") UUID id, HttpSession session){
        try {
            var client = service.remAddress(id, session);
            return ResponseEntity.status(201).body(client.toDTO());

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
            data.put("message", "Cliente deletado com Sucesso!");
            data.put("result", true);

            return ResponseEntity.ok(data);
        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }
}
