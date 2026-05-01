package org.example.Service;

import org.example.Model.PriorityLevel;
import org.example.Dtos.ResidentDto.ResidentResponseDto;
import org.example.Model.Resident;

import java.util.List;

public interface IResidentService
{
    public ResidentResponseDto addSpecialNeeds(Integer residentId, List<Integer> needIds);
    List<ResidentResponseDto> getAllResidents();

    public PriorityLevel calculatePriority(Resident resident);
}
