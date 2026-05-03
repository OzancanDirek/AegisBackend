package org.example.Service.Impl;

import lombok.RequiredArgsConstructor;
import org.example.Dtos.HouseHold.CreateHouseholdDto;
import org.example.Dtos.HouseHold.HouseholdResponseDto;
import org.example.Dtos.RequestDto.AidRequestResponseDto;
import org.example.Dtos.RequestDto.CreateAidRequestDto;
import org.example.Dtos.RequestDto.UpdateAidRequestDto;
import org.example.Model.AidRequest;
import org.example.Model.AidRequestType;
import org.example.Model.HouseHold;
import org.example.Model.Resident;
import org.example.Model.Users;
import org.example.Repository.AidRequestTypeRepository;
import org.example.Repository.HouseHoldRepository;
import org.example.Repository.RequestRepository;
import org.example.Repository.ResidentRepository;
import org.example.Repository.UserRepository;
import org.example.Service.IAidRequestService;
import org.springframework.stereotype.Service;
import org.example.Model.PriorityLevel;


import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AidRequestServiceImpl implements IAidRequestService
{
    private final RequestRepository aidRequestRepository;
    private final AidRequestTypeRepository aidRequestTypeRepository;
    private final HouseHoldRepository houseHoldRepository;
    private final UserRepository userRepository;
    private final ResidentRepository residentRepository;

    @Override
    public AidRequestResponseDto createAidRequest(CreateAidRequestDto dto)
    {
        HouseHold houseHold = houseHoldRepository.findById(dto.getHouseholdId())
                .orElseThrow(() -> new RuntimeException("Household bulunamadı: " + dto.getHouseholdId()));

        AidRequestType type = aidRequestTypeRepository.findById(dto.getTypeId())
                .orElseThrow(() -> new RuntimeException("Yardım türü bulunamadı: " + dto.getTypeId()));

        AidRequest aidRequest = AidRequest.builder()
                .household(houseHold)
                .requestType(type)
                .description(dto.getDescription())
                .urgencyLevel(dto.getUrgencyLevel() != null ? dto.getUrgencyLevel() : 1)
                .status(AidRequest.RequestStatus.PENDING)
                .build();

        return toDto(aidRequestRepository.save(aidRequest));
    }

    @Override
    public AidRequestResponseDto updateAidRequest(UpdateAidRequestDto dto)
    {
        AidRequest request = aidRequestRepository.findById(dto.getRequestId())
                .orElseThrow(() -> new RuntimeException("Talep bulunamadı: " + dto.getRequestId()));

        if (dto.getStatus() != null) request.setStatus(dto.getStatus());
        if (dto.getUrgencyLevel() != null) request.setUrgencyLevel(dto.getUrgencyLevel());
        if (dto.getDescription() != null) request.setDescription(dto.getDescription());

        return toDto(aidRequestRepository.save(request));
    }

    @Override
    public List<AidRequestResponseDto> getAllAidRequest()
    {
        return aidRequestRepository.findAllOrderByUrgency()
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public List<AidRequestResponseDto> getByHousehold(Integer householdId)
    {
        return aidRequestRepository.findByHousehold_HouseholdId(householdId)
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public List<AidRequestResponseDto> getByStatusAidRequest(AidRequest.RequestStatus status)
    {
        return aidRequestRepository.findByStatus(status)
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public List<AidRequestType> getAllTypesAidRequest()
    {
        return aidRequestTypeRepository.findAll();
    }

    @Override
    public void deleteAidRequest(Integer requestId)
    {
        aidRequestRepository.deleteById(requestId);
    }

    @Override
    public HouseholdResponseDto getMyHousehold(UUID userId)
    {
        return residentRepository.findByUser_UserId(userId)
                .map(r -> r.getHousehold())
                .filter(h -> h != null)
                .map(h -> HouseholdResponseDto.builder()
                        .householdId(h.getHouseholdId())
                        .householdName(h.getHouseholdName())
                        .emergencyContactName(h.getEmergencyContactName())
                        .emergencyContactPhone(h.getEmergencyContactPhone())
                        .notes(h.getNotes())
                        .build())
                .orElse(null);
    }

    @Override
    public HouseholdResponseDto createHousehold(UUID userId, CreateHouseholdDto dto)
    {
        HouseHold household = HouseHold.builder()
                .householdName(dto.getHouseholdName())
                .emergencyContactName(dto.getEmergencyContactName())
                .emergencyContactPhone(dto.getEmergencyContactPhone())
                .notes(dto.getNotes())
                .build();
        household = houseHoldRepository.save(household);

        Resident resident = residentRepository.findByUser_UserId(userId)
                .orElseGet(() -> {
                    Users user = userRepository.findById(userId)
                            .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));
                    Resident newResident = new Resident();
                    newResident.setUser(user);
                    newResident.setFullName(user.getName() + " " + user.getSurname());
                    newResident.setPriorityLevel(PriorityLevel.MEDIUM);
                    return newResident;
                });

        resident.setHousehold(household);
        residentRepository.save(resident);

        return HouseholdResponseDto.builder()
                .householdId(household.getHouseholdId())
                .householdName(household.getHouseholdName())
                .emergencyContactName(household.getEmergencyContactName())
                .emergencyContactPhone(household.getEmergencyContactPhone())
                .notes(household.getNotes())
                .build();
    }

    private AidRequestResponseDto toDto(AidRequest r)
    {
        return AidRequestResponseDto.builder()
                .requestId(r.getRequestId())
                .householdId(r.getHousehold() != null ? r.getHousehold().getHouseholdId() : null)
                .householdName(r.getHousehold() != null ? r.getHousehold().getHouseholdName() : null)
                .typeId(r.getRequestType() != null ? r.getRequestType().getTypeId() : null)
                .typeName(r.getRequestType() != null ? r.getRequestType().getTypeName() : null)
                .category(r.getRequestType() != null ? r.getRequestType().getCategory() : null)
                .description(r.getDescription())
                .urgencyLevel(r.getUrgencyLevel())
                .status(r.getStatus())
                .createdAt(r.getCreatedAt())
                .build();
    }
}
