package org.example.Service;

import org.example.Dtos.ResidentDto.ResidentResponseDto;

import java.util.List;

public interface IResidentService
{
    public ResidentResponseDto addSpecialNeeds(Integer residentId, List<Integer> needIds);
    List<ResidentResponseDto> getAllResidents();
}
