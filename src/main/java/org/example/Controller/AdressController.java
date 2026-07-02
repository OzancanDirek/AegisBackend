package org.example.Controller;

import lombok.RequiredArgsConstructor;
import org.example.Dtos.AdressDto.CreateAdressDto;
import org.example.Dtos.AdressDto.ResultAdressDto;
import org.example.Dtos.AdressDto.UpdateAdressDto;
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
@RequiredArgsConstructor
public class AdressController
{
    private final IAdressService adressService;

    @GetMapping("allAdresses")
    public List<ResultAdressDto> getAllAdresses()
    {
        return adressService.getAllAdress();
    }

    @PostMapping
    public ResponseEntity<ResultAdressDto> createAddress(@RequestBody CreateAdressDto createAdressDto)
    {
        return ResponseEntity.ok(adressService.createAdress(createAdressDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAdress(@PathVariable int id)
    {
        adressService.deleteAdress(id);
        return ResponseEntity.ok("Adres başarıyla silindi");
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResultAdressDto> updateAddress(@PathVariable int id, @RequestBody UpdateAdressDto updateAdressDto)
    {
        return ResponseEntity.ok(adressService.updateAdress(id, updateAdressDto));
    }
}