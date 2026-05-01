package org.example.Dtos.TeamDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTeamDto
{
    private Integer teamId;

    private String teamName;

    private String teamType;

    private String status;
}
