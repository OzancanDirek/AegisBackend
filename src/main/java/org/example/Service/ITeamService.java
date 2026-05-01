package org.example.Service;

import org.example.Dtos.TeamDto.AddTeamMembersDto;
import org.example.Dtos.TeamDto.CreateTeamDto;
import org.example.Dtos.TeamDto.RemoveTeamMemberDto;
import org.example.Dtos.TeamDto.TeamResponseDto;
import org.example.Dtos.TeamDto.UpdateTeamDto;

import java.util.List;

public interface ITeamService
{
    List<TeamResponseDto> getTeams();

    TeamResponseDto CreateTeam(CreateTeamDto createTeamDto);

    TeamResponseDto DeleteTeam(Integer teamId);

    TeamResponseDto addMembers(AddTeamMembersDto dto);

    TeamResponseDto removeMember(RemoveTeamMemberDto dto);

    TeamResponseDto updateTeam(UpdateTeamDto dto);

}
