package org.example.Dtos.TeamDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.Dtos.UserDto.UserResponseDto;

import java.util.Set;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TeamResponseDto
{
    private Integer teamId;
    private String teamName;
    private String teamType;
    private String status;

    private Set<UserResponseDto> members;
}
