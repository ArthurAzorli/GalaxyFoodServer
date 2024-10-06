package br.edu.ifsp.galaxyfood.server.controller;

import br.edu.ifsp.galaxyfood.server.model.domain.OrderStatus;
import br.edu.ifsp.galaxyfood.server.model.dto.InBuyDTO;
import br.edu.ifsp.galaxyfood.server.model.dto.OutBuyDTO;
import br.edu.ifsp.galaxyfood.server.model.service.BuyService;
import br.edu.ifsp.galaxyfood.server.utils.ErrorMessage;
import br.edu.ifsp.galaxyfood.server.utils.ExceptionController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/buy")
public class BuyController {

    @Autowired
    private BuyService service;

    @PostMapping("/create/{idClient}")
    public ResponseEntity<Object> create(@PathVariable("idClient") UUID idClient, @RequestBody InBuyDTO dto){
        try {
            var buy = service.create(idClient, dto);
            return ResponseEntity.status(201).body(buy.toDTO());
        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

    @GetMapping("/get/{idUser}/{id}")
    public ResponseEntity<Object> get(@PathVariable("idUser") UUID idUser, @PathVariable("id") UUID id, @RequestParam(name = "typeUser") String typeUser){
        try {
            var buy = service.get(idUser, id, typeUser);
            return ResponseEntity.status(302).body(buy.toDTO());
        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

    @GetMapping("/get/{idUser}")
    public ResponseEntity<Object> getAll(@PathVariable("idUser") UUID idUser, @RequestParam(name = "typeUser") String typeUser){
        try {
            var buys = service.getAll(idUser, typeUser);

            List<OutBuyDTO> list = new ArrayList<>();
            for (var buy : buys) list.add(buy.toDTO());
            return ResponseEntity.status(302).body(list);
        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

    @PutMapping("/updatestatus/{idRestaurant}/{id}")
    public ResponseEntity<Object> updateStatus(@PathVariable("idRestaurant") UUID idRestaurant, @PathVariable("id") UUID id, @RequestParam(name = "status") int orderStatus){
        try {
            var status = OrderStatus.getOrderStatus(orderStatus);
            var buy = service.updateOrderStatus(idRestaurant, id, status);

            return ResponseEntity.status(202).body(buy.toDTO());
        }catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

    @PutMapping("/cancel/{idClient}/{id}")
    public ResponseEntity<Object> cancel(@PathVariable("idClient") UUID idClient, @PathVariable("id") UUID id){
        try {
            var buy = service.cancel(idClient, id);

            return ResponseEntity.status(202).body(buy.toDTO());
        }catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }
}
