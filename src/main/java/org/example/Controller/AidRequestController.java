package org.example.Controller;

import lombok.RequiredArgsConstructor;
import org.example.Dtos.HouseHold.CreateHouseholdDto;
import org.example.Dtos.HouseHold.HouseholdResponseDto;
import org.example.Dtos.RequestDto.AidRequestResponseDto;
import org.example.Dtos.RequestDto.CreateAidRequestDto;
import org.example.Dtos.RequestDto.UpdateAidRequestDto;
import org.example.Model.AidRequest;
import org.example.Model.AidRequestType;
import org.example.Model.Users;
import org.example.Repository.UserRepository;
import org.example.Service.IAidRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
@RequestMapping("/api/aid-requests")
@RequiredArgsConstructor
public class AidRequestController
{
    private final IAidRequestService aidRequestService;
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<AidRequestResponseDto> create(@RequestBody CreateAidRequestDto dto)
    {
        return ResponseEntity.ok(aidRequestService.createAidRequest(dto));
    }

    @PutMapping
    public ResponseEntity<AidRequestResponseDto> update(@RequestBody UpdateAidRequestDto dto)
    {
        return ResponseEntity.ok(aidRequestService.updateAidRequest(dto));
    }

    @GetMapping
    public ResponseEntity<List<AidRequestResponseDto>> getAll()
    {
        return ResponseEntity.ok(aidRequestService.getAllAidRequest());
    }

    @GetMapping("/household/{householdId}")
    public ResponseEntity<List<AidRequestResponseDto>> getByHousehold(@PathVariable Integer householdId)
    {
        return ResponseEntity.ok(aidRequestService.getByHousehold(householdId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<AidRequestResponseDto>> getByStatus(@PathVariable AidRequest.RequestStatus status)
    {
        return ResponseEntity.ok(aidRequestService.getByStatusAidRequest(status));
    }

    @GetMapping("/types")
    public ResponseEntity<List<AidRequestType>> getTypes()
    {
        return ResponseEntity.ok(aidRequestService.getAllTypesAidRequest());
    }

    @DeleteMapping("/{requestId}")
    public ResponseEntity<Void> delete(@PathVariable Integer requestId)
    {
        aidRequestService.deleteAidRequest(requestId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/my-household")
    public ResponseEntity<?> getMyHousehold(Authentication authentication)
    {
        String email = authentication.getName();
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));
        HouseholdResponseDto dto = aidRequestService.getMyHousehold(user.getUserId());
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/my-household")
    public ResponseEntity<HouseholdResponseDto> createHousehold(
            Authentication authentication,
            @RequestBody CreateHouseholdDto dto)
    {
        String email = authentication.getName();
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));
        return ResponseEntity.ok(aidRequestService.createHousehold(user.getUserId(), dto));
    }
}
