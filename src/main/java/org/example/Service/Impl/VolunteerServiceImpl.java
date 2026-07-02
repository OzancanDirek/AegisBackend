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
import org.example.Service.IAuditService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.example.Repository.AddressRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VolunteerServiceImpl implements IVolunteerService
{
    private final VolunteerRepository volunteerRepository;
    private final UserRepository userRepository;
    private final AddressRepository adressesRepository;
    private final RoleRepository roleRepository;
    private final IAuditService auditService;

    private String currentUserEmail()
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null ? auth.getName() : "unknown";
    }

    private ResultVolunteerDto mapToDto(Volunteer volunteer)
    {
        ResultVolunteerDto dto = new ResultVolunteerDto();
        dto.setVolunteerId(volunteer.getVolunteerId());

        if (volunteer.getUser() != null)
        {
            dto.setUserId(volunteer.getUser().getUserId());
            dto.setName(volunteer.getUser().getName());
            dto.setSurname(volunteer.getUser().getSurname());
            dto.setPhone(volunteer.getUser().getPhone());
        }

        dto.setAvailabilityStatus(volunteer.getAvailabilityStatus());
        dto.setTransportType(volunteer.getTransportType());
        dto.setMaxDistanceKm(volunteer.getMaxDistanceKm());

        Adresses address = volunteer.getAddress() != null
                ? volunteer.getAddress()
                : (volunteer.getUser() != null ? volunteer.getUser().getAddress() : null);

        if (address != null)
        {
            dto.setCity(address.getCity());
            dto.setDistrict(address.getDistrict());
            dto.setNeighborhood(address.getNeighborhood());
        }

        if (volunteer.getSkills() != null)
            dto.setSkills(volunteer.getSkills().stream().map(Skill::getSkillName).toList());

        return dto;
    }

    @Override
    public Volunteer createVolunteer(CreateVolunteerDto dto)
    {
        Volunteer volunteer = Volunteer.builder()
                .availabilityStatus(dto.getAvailabilityStatus() != null ? dto.getAvailabilityStatus() : true)
                .transportType(dto.getTransportType())
                .maxDistanceKm(dto.getMaxDistanceKm())
                .build();

        if (dto.getUserId() != null)
        {
            Users user = userRepository.findById(dto.getUserId()).orElse(null);
            if (user != null) volunteer.setUser(user);
        }

        Adresses address = null;
        if (dto.getAddressId() != null)
            address = adressesRepository.getReferenceById(dto.getAddressId());
        else if (volunteer.getUser() != null && volunteer.getUser().getAddress() != null)
            address = volunteer.getUser().getAddress();

        volunteer.setAddress(address);

        if (dto.getSkillIds() != null && !dto.getSkillIds().isEmpty())
        {
            Set<Skill> skills = dto.getSkillIds().stream()
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

        Volunteer saved = volunteerRepository.save(volunteer);

        String userName = saved.getUser() != null
                ? saved.getUser().getName() + " " + saved.getUser().getSurname()
                : "Bilinmeyen";

        auditService.log(
                currentUserEmail(),
                "CREATE",
                "VOLUNTEER",
                String.valueOf(saved.getVolunteerId()),
                userName + " gönüllü olarak kaydedildi"
        );

        return saved;
    }

    @Override
    public void updateAvailability(Integer id, Boolean status)
    {
        volunteerRepository.findByVolunteerId(id).ifPresent(volunteer -> {
            volunteer.setAvailabilityStatus(status);
            volunteerRepository.save(volunteer);

            String userName = volunteer.getUser() != null
                    ? volunteer.getUser().getName() + " " + volunteer.getUser().getSurname()
                    : "Bilinmeyen";

            auditService.log(
                    currentUserEmail(),
                    "UPDATE",
                    "VOLUNTEER",
                    String.valueOf(id),
                    userName + " müsaitlik durumu: " + (status ? "Müsait" : "Meşgul")
            );
        });
    }

    @Override
    public List<ResultVolunteerDto> getAllVolunteers()
    {
        return volunteerRepository.findAll().stream().map(this::mapToDto).toList();
    }

    @Override
    public ResultVolunteerDto getVolunteerById(Integer id)
    {
        return volunteerRepository.findByVolunteerId(id).map(this::mapToDto).orElse(null);
    }

    @Override
    public List<ResultVolunteerDto> findAvailableVolunteers()
    {
        return volunteerRepository.findAll().stream()
                .filter(Volunteer::getAvailabilityStatus)
                .map(this::mapToDto).toList();
    }

    @Override
    public List<ResultVolunteerDto> findBySkill(String skillName)
    {
        return volunteerRepository.findAll().stream()
                .filter(v -> v.getSkills() != null &&
                        v.getSkills().stream().anyMatch(s -> s.getSkillName().equalsIgnoreCase(skillName)))
                .map(this::mapToDto).toList();
    }

    @Override
    public ResultVolunteerDto getVolunteerProfile(UUID userId)
    {
        Volunteer volunteer = volunteerRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new RuntimeException("Volunteer not found"));
        return getVolunteerById(volunteer.getVolunteerId());
    }
}