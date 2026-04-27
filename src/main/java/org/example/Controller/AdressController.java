package org.example.Controller;

import lombok.RequiredArgsConstructor;
import org.example.Dtos.AdressDto.CreateAdressDto;
import org.example.Dtos.AdressDto.UpdateAdressDto;
import org.example.Model.Adresses;
import org.example.Service.IAdressService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class AdressController
{
    private final IAdressService adressService;

    @GetMapping("allAdresses")
    public List<Adresses> getAllAdresses()
    {
        return adressService.getAllAdress();
    }

    @PostMapping
    public ResponseEntity<Adresses> createAddress(@RequestBody CreateAdressDto createAdressDto)
    {
        return ResponseEntity.ok(adressService.createAdress(createAdressDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAdress(@PathVariable int id)
    {
        adressService.deleteAdress(id);
        return ResponseEntity.ok("Adress basariyla silindi");
    }

    @PutMapping("/{id}")
    public ResponseEntity<Adresses> updateAddress( @PathVariable int id,@RequestBody UpdateAdressDto updateAdressDto)
    {
        return ResponseEntity.ok(adressService.updateAdress(id, updateAdressDto));
    }
}
