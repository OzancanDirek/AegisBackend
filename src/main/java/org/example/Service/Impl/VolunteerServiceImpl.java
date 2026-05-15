package org.example.Service.Impl;

import lombok.RequiredArgsConstructor;
import org.example.Dtos.VolunteerDtos.CreateVolunteerDto;
import org.example.Dtos.VolunteerDtos.ResultVolunteerDto;
import org.example.Model.Adresses;
import org.example.Model.Role;
import org.example.Model.Skill;
import org.example.Model.Users;
import org.example.Model.Volunteer;
import org.example.Repository.RoleRepository;
import org.example.Repository.UserRepository;
import org.example.Repository.VolunteerRepository;
import org.example.Service.IVolunteerService;
import org.springframework.stereotype.Service;
import org.example.Repository.AddressRepository;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VolunteerServiceImpl implements IVolunteerService
{
    private final VolunteerRepository volunteerRepository;
    private final UserRepository userRepository;
    private final AddressRepository _adressesRepository;
    private final RoleRepository roleRepository;

    private ResultVolunteerDto mapToDto(Volunteer volunteer)
    {
        ResultVolunteerDto volunteerDto = new ResultVolunteerDto();

        volunteerDto.setVolunteerId(volunteer.getVolunteerId());

        if (volunteer.getUser() != null)
        {
            volunteerDto.setUserId(volunteer.getUser().getUserId());
            volunteerDto.setName(volunteer.getUser().getName());
            volunteerDto.setSurname(volunteer.getUser().getSurname());
            volunteerDto.setPhone(volunteer.getUser().getPhone());
        }

        volunteerDto.setAvailabilityStatus(volunteer.getAvailabilityStatus());
        volunteerDto.setTransportType(volunteer.getTransportType());
        volunteerDto.setMaxDistanceKm(volunteer.getMaxDistanceKm());

        Adresses address = volunteer.getAddress() != null
                ? volunteer.getAddress()
                : (volunteer.getUser() != null ? volunteer.getUser().getAddress() : null);

        if (address != null)
        {
            volunteerDto.setCity(address.getCity());
            volunteerDto.setDistrict(address.getDistrict());
            volunteerDto.setNeighborhood(address.getNeighborhood());
        }

        if (volunteer.getSkills() != null)
        {
            volunteerDto.setSkills(
                    volunteer.getSkills()
                            .stream()
                            .map(skill -> skill.getSkillName())
                            .toList()
            );
        }

        return volunteerDto;
    }

    @Override
    public Volunteer createVolunteer(CreateVolunteerDto createVolunteerDto)
    {
        Volunteer volunteer = Volunteer.builder()
                .availabilityStatus(createVolunteerDto.getAvailabilityStatus() != null ? createVolunteerDto.getAvailabilityStatus() : true)
                .transportType(createVolunteerDto.getTransportType())
                .maxDistanceKm(createVolunteerDto.getMaxDistanceKm())
                .build();

        if (createVolunteerDto.getUserId() != null)
        {
            Users user = userRepository.findById(createVolunteerDto.getUserId()).orElse(null);
            if (user != null)
            {
                volunteer.setUser(user);
            }
        }

        Adresses address = null;

        if (createVolunteerDto.getAddressId() != null)
        {
            address = _adressesRepository.getReferenceById(createVolunteerDto.getAddressId());
        }
        else if (volunteer.getUser() != null && volunteer.getUser().getAddress() != null)
        {
            address = volunteer.getUser().getAddress();
        }

        volunteer.setAddress(address);

        if (createVolunteerDto.getSkillIds() != null && !createVolunteerDto.getSkillIds().isEmpty())
        {
            Set<Skill> skills = createVolunteerDto.getSkillIds().stream()
                    .map(id -> {
                        Skill s = new Skill();
                        s.setSkillId(id);
                        return s;
                    })
                    .collect(Collectors.toSet());
            volunteer.setSkills(skills);
        }

        if (volunteer.getUser() != null)
        {
            Users user = volunteer.getUser();
            Role volunteerRole = roleRepository.findByRoleName("Gonullu").orElse(null);
            if (volunteerRole != null)
            {
                user.getRoles().clear();
                user.getRoles().add(volunteerRole);
                userRepository.save(user);
            }
        }
        return volunteerRepository.save(volunteer);
    }

    @Override
    public List<ResultVolunteerDto> getAllVolunteers()
    {
        return volunteerRepository.findAll().
                stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public ResultVolunteerDto getVolunteerById(Integer id)
    {
        return volunteerRepository.findByVolunteerId(id)
                .map(this::mapToDto)
                .orElse(null);
    }

    @Override
    public void updateAvailability(Integer id, Boolean status)
    {
        volunteerRepository.findByVolunteerId(id).ifPresent(volunteer -> {
            volunteer.setAvailabilityStatus(status);
            volunteerRepository.save(volunteer);
        });
    }

    @Override
    public List<ResultVolunteerDto> findAvailableVolunteers()
    {
        return volunteerRepository.findAll().stream()
                .filter(Volunteer::getAvailabilityStatus)
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public List<ResultVolunteerDto> findBySkill(String skillName)//yetenek adlarına göre listeleme çeşidi
    {
        return volunteerRepository.findAll().stream()
                .filter(v -> v.getSkills() != null &&
                        v.getSkills().stream()
                                .anyMatch(skill -> skill.getSkillName().equalsIgnoreCase(skillName)))
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public ResultVolunteerDto getVolunteerProfile(UUID userId)
    {
        Volunteer volunteer = volunteerRepository.findByUser_UserId(userId).orElseThrow(() -> new RuntimeException("Volunteer not found"));
        return getVolunteerById(volunteer.getVolunteerId());
    }

}
