package br.edu.ifsp.galaxyfood.server.controller;

import br.edu.ifsp.galaxyfood.server.model.domain.OrderStatus;
import br.edu.ifsp.galaxyfood.server.model.dto.InBuyDTO;
import br.edu.ifsp.galaxyfood.server.model.dto.OutBuyDTO;
import br.edu.ifsp.galaxyfood.server.model.service.BuyService;
import br.edu.ifsp.galaxyfood.server.utils.ErrorMessage;
import br.edu.ifsp.galaxyfood.server.utils.ExceptionController;
import jakarta.servlet.http.HttpSession;
import org.hibernate.usertype.UserType;
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

    @PostMapping("/create")
    public ResponseEntity<Object> create(@RequestBody InBuyDTO dto, @RequestParam UUID clientId) {
        try {
            var buy = service.create(dto, clientId);
            return ResponseEntity.status(201).body(buy.toDTO());
        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Object> get(@PathVariable("id") UUID id, @RequestParam UUID userId, @RequestParam String userType) {
        try {
            var buy = service.get(id, userId, userType);
            return ResponseEntity.status(302).body(buy.toDTO());
        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }

    @GetMapping("/get")
    public ResponseEntity<Object> getAll(@RequestParam UUID userId, @RequestParam String userType) {
        try {
            var buys = service.getAll(userId, userType);
            List<OutBuyDTO> list = new ArrayList<>();
            for (var buy : buys) {
                list.add(buy.toDTO());
            }
            return ResponseEntity.ok(list);
        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }


    @PutMapping("/updatestatus/{id}")
    public ResponseEntity<Object> updateStatus(@PathVariable("id") UUID buyId, @RequestParam int orderStatus, @RequestParam UUID restaurantId) {
        try {
            var status = OrderStatus.getOrderStatus(orderStatus);
            var buy = service.updateOrderStatus(buyId, status, restaurantId);

            return ResponseEntity.status(202).body(buy);
        } catch (ExceptionController e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorMessage(e));
        }
    }
}
