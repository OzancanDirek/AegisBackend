package org.example.Service;

import org.example.Dtos.VolunteerDtos.CreateVolunteerDto;
import org.example.Dtos.VolunteerDtos.ResultVolunteerDto;
import org.example.Model.Volunteer;

import java.util.List;

public interface IVolunteerService
{
    Volunteer createVolunteer(CreateVolunteerDto dto);

    List<ResultVolunteerDto> getAllVolunteers();

    ResultVolunteerDto getVolunteerById(Integer id);

    void updateAvailability(Integer id, Boolean status);

    List<ResultVolunteerDto> findAvailableVolunteers();

    List<ResultVolunteerDto> findBySkill(String skillName);
}
