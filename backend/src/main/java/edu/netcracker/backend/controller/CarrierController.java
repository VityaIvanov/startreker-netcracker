package edu.netcracker.backend.controller;

import edu.netcracker.backend.dto.request.UserCreateForm;
import edu.netcracker.backend.dto.request.UserUpdateForm;
import edu.netcracker.backend.dto.response.UserDTO;
import edu.netcracker.backend.service.serviceInterface.CarrierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @GetMapping(path = "/api/admin/carrier-by-username")
    //@PreAuthorize("hasRole('ADMIN')")
    public UserDTO getCarrierByUsername(@RequestParam("username") String username){
        return carrierService.getCarrierByUsername(username);
    }

    @GetMapping(path = "/api/admin/carrier-by-email")
    //@PreAuthorize("hasRole('ADMIN')")
    public UserDTO getCarrierByEmail(@Valid @Email @RequestParam("email") String email){
        return carrierService.getCarrierByEmail(email);
    }

    @GetMapping(path = "/api/admin/carrier/{userId}")
    //@PreAuthorize("hasRole('ADMIN')")
    public UserDTO getCarrierById(@PathVariable Integer userId){
        return carrierService.getCarrierById(userId);
    }

    @GetMapping(path = "/api/admin/carrier")
    //@PreAuthorize("hasRole('ADMIN')")
    public List<UserDTO> getAllCarrier(){
        return carrierService.getAllCarrier();
    }

    @GetMapping(path = "/api/admin/carrier-in-range-id")
    //@PreAuthorize("hasRole('ADMIN')")
    public List<UserDTO> getAllCarrier(@RequestParam("startId") Integer startId, @RequestParam("endId") Integer endId){
        return carrierService.getAllCarrier(startId, endId);
    }

    @GetMapping(path = "/api/admin/pagination")
    //@PreAuthorize("hasRole('ADMIN')")
    public List<UserDTO> getPagination(@RequestParam("from") Integer from, @RequestParam("number") Integer number){
        return carrierService.getPagination(from, number);
    }

    @PostMapping(path = "/api/admin/carrier")
    //@PreAuthorize("hasRole('ADMIN')")
    public UserDTO createCarrier(@Valid @RequestBody UserCreateForm createForm){
        return carrierService.createCarrier(createForm);
    }

    @DeleteMapping(path = "/api/admin/carrier/{userId}")
    //@PreAuthorize("hasRole('ADMIN')")
    public UserDTO deleteCarrier(@PathVariable Integer userId){
        return carrierService.deleteCarrier(userId);
    }

    @PutMapping(path = "/api/admin/carrier")
    //@PreAuthorize("hasRole('ADMIN')")
    public UserDTO updateCarrier(@Valid @RequestBody UserUpdateForm carrierForm){
        return carrierService.updateCarrier(carrierForm);
    }
}
