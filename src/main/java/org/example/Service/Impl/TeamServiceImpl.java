package org.example.Service.Impl;

import lombok.RequiredArgsConstructor;
import org.example.Dtos.TeamDto.AddTeamMembersDto;
import org.example.Dtos.TeamDto.CreateTeamDto;
import org.example.Dtos.TeamDto.RemoveTeamMemberDto;
import org.example.Dtos.TeamDto.TeamResponseDto;
import org.example.Dtos.TeamDto.UpdateTeamDto;
import org.example.Dtos.UserDto.UserResponseDto;
import org.example.Model.Team;
import org.example.Model.Users;
import org.example.Repository.TeamRepository;
import org.example.Repository.UserRepository;
import org.example.Service.ITeamService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements ITeamService
{
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    @Override
    public List<TeamResponseDto> getTeams()
    {
        return teamRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public TeamResponseDto CreateTeam(CreateTeamDto dto)
    {

        boolean exists = teamRepository.existsByTeamName(dto.getTeamName());
        if (exists)
        {
            throw new RuntimeException("Bu takım adı zaten mevcut!");
        }

        Team team = Team.builder()
                .teamName(dto.getTeamName())
                .teamType(dto.getTeamType())
                .status(dto.getStatus())
                .build();

        Team saved = teamRepository.save(team);

        return mapToDto(saved);
    }

    @Override
    public TeamResponseDto DeleteTeam(Integer teamId)
    {

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Bu takım bulunamadı"));

        teamRepository.delete(team);

        return mapToDto(team);
    }

    public TeamResponseDto updateTeam(UpdateTeamDto dto)
    {
        Team team = teamRepository.findById(dto.getTeamId())
                .orElseThrow(() -> new RuntimeException("Takim bulunamadi"));

        if (dto.getTeamName() != null)
        {
            team.setTeamName(dto.getTeamName());
        }

        if (dto.getTeamType() != null)
        {
            team.setTeamType(dto.getTeamType());
        }

        if (dto.getStatus() != null)
        {
            team.setStatus(dto.getStatus());
        }

        Team updated = teamRepository.save(team);

        return mapToDto(updated);
    }

    @Override
    public TeamResponseDto addMembers(AddTeamMembersDto dto)
    {
        Team team = teamRepository.findById(dto.getTeamId())
                .orElseThrow(() -> new RuntimeException("Team bulunamadı"));

        Set<Users> users = new HashSet<>();

        for (UUID userId : dto.getUserIds())
        {
            Users user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User bulunamadı: " + userId));

            boolean alreadyInAnotherTeam = teamRepository.existsUserInAnyTeam(userId);

            if (alreadyInAnotherTeam)
            {
                throw new RuntimeException("Bu kullanıcı zaten başka bir takımda: " + user.getName());
            }

            users.add(user);
        }

        team.getMembers().addAll(users);

        Team updated = teamRepository.save(team);

        return mapToDto(updated);
    }

    @Override
    public TeamResponseDto removeMember(RemoveTeamMemberDto dto)
    {
        Team team = teamRepository.findById(dto.getTeamId())
                .orElseThrow(() -> new RuntimeException("Takım bulunamadı"));

        Users user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("Kullanici bulunamadı"));

        team.getMembers().remove(user);

        Team updated = teamRepository.save(team);

        return mapToDto(updated);
    }


    private TeamResponseDto mapToDto(Team team)
    {

        return TeamResponseDto.builder()
                .teamId(team.getTeamId())
                .teamName(team.getTeamName())
                .teamType(team.getTeamType())
                .status(team.getStatus())
                .members(
                        team.getMembers().stream()
                                .map(this::mapUser)
                                .collect(Collectors.toSet())
                )
                .build();
    }

    private UserResponseDto mapUser(Users user)
    {

        return UserResponseDto.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .surname(user.getSurname())
                .email(user.getEmail())
                .build();
    }
}
