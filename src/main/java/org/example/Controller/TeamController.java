package org.example.Controller;


import lombok.RequiredArgsConstructor;
import org.example.Dtos.TeamDto.AddTeamMembersDto;
import org.example.Dtos.TeamDto.CreateTeamDto;
import org.example.Dtos.TeamDto.RemoveTeamMemberDto;
import org.example.Dtos.TeamDto.TeamResponseDto;
import org.example.Dtos.TeamDto.UpdateTeamDto;
import org.example.Model.Team;
import org.example.Service.ITeamService;
import org.springframework.web.bind.annotation.CrossOrigin;
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
@RequestMapping("/api/teams")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class TeamController
{
    private final ITeamService teamService;

    @GetMapping("/getAllTeams")
    public List<TeamResponseDto> getAllTeams()
    {
        return teamService.getTeams();
    }

    @PostMapping
    public TeamResponseDto createTeam(@RequestBody CreateTeamDto createTeamDto)
    {
        return teamService.CreateTeam(createTeamDto);
    }

    @DeleteMapping("/{id}")
    public TeamResponseDto deleteTeam(@PathVariable Integer id)
    {
        return teamService.DeleteTeam(id);
    }

    @PutMapping("/update")
    public TeamResponseDto updateTeam(@RequestBody UpdateTeamDto dto)
    {
        return teamService.updateTeam(dto);
    }

    @PostMapping("/add-members")
    public TeamResponseDto addMembers(@RequestBody AddTeamMembersDto dto)
    {
        return teamService.addMembers(dto);
    }

    @PostMapping("/remove-member")
    public TeamResponseDto removeMember(@RequestBody RemoveTeamMemberDto dto)
    {
        return teamService.removeMember(dto);
    }
}
