package edu.netcracker.backend.controller;

import edu.netcracker.backend.dto.request.UserCreateForm;
import edu.netcracker.backend.dto.request.UserUpdateForm;
import edu.netcracker.backend.dto.response.UserDTO;
import edu.netcracker.backend.service.serviceInterface.CarrierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import java.util.List;

@RestController
@Validated
public class CarrierController {

    private CarrierService carrierService;

    @Autowired
    public CarrierController(CarrierService carrierService) {
        this.carrierService = carrierService;
    }

    @GetMapping(path = "/api/carrier-by-username")
    public UserDTO getCarrierByUsername(@RequestParam("username") String username){
        return carrierService.getCarrierByUsername(username);
    }

    @GetMapping(path = "/api/carrier-by-email")
    public UserDTO getCarrierByEmail(@Valid @Email @RequestParam("email") String email){
        return carrierService.getCarrierByEmail(email);
    }

    @GetMapping(path = "/api/carrier/{userId}")
    public UserDTO getCarrierById(@PathVariable Number userId){
        return carrierService.getCarrierById(userId);
    }

    @GetMapping(path = "/api/carrier")
    public List<UserDTO> getAllCarrier(){
        return carrierService.getAllCarrier();
    }

    @GetMapping(path = "/api/carrier-in-range-id")
    public List<UserDTO> getAllCarrier(@RequestParam("startId") Number startId, @RequestParam("endId") Number endId){
        return carrierService.getAllCarrier(startId, endId);
    }

    @PostMapping(path = "/api/carrier")
    public UserDTO createCarrier(@Valid @RequestBody UserCreateForm createForm){
        return carrierService.createCarrier(createForm);
    }

    @DeleteMapping(path = "/api/carrier/{userId}")
    public UserDTO deleteCarrier(@PathVariable Number userId){
        return carrierService.deleteCarrier(userId);
    }

    @PutMapping(path = "/api/carrier")
    public UserDTO updateCarrier(@Valid @RequestBody UserUpdateForm carrierForm){
        return carrierService.updateCarrier(carrierForm);
    }
}
