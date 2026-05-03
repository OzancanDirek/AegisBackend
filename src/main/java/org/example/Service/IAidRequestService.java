package org.example.Service;

import org.example.Dtos.HouseHold.CreateHouseholdDto;
import org.example.Dtos.HouseHold.HouseholdResponseDto;
import org.example.Dtos.RequestDto.AidRequestResponseDto;
import org.example.Dtos.RequestDto.CreateAidRequestDto;
import org.example.Dtos.RequestDto.UpdateAidRequestDto;
import org.example.Model.AidRequest;
import org.example.Model.AidRequestType;

import java.util.List;
import java.util.UUID;

public interface IAidRequestService
{
    AidRequestResponseDto createAidRequest(CreateAidRequestDto dto);

    AidRequestResponseDto updateAidRequest(UpdateAidRequestDto dto);

    List<AidRequestResponseDto> getAllAidRequest();

    List<AidRequestResponseDto> getByHousehold(Integer householdId);

    List<AidRequestResponseDto> getByStatusAidRequest(AidRequest.RequestStatus status);

    List<AidRequestType> getAllTypesAidRequest();

    void deleteAidRequest(Integer requestId);

    HouseholdResponseDto getMyHousehold(UUID userId);
    HouseholdResponseDto createHousehold(UUID userId, CreateHouseholdDto dto);
}
