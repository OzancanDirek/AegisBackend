package org.example.Dtos.TeamDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.Model.Users;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResultTeamDto
{
    private Integer teamId;

    private String teamName;

    private String teamType;

    private String status;

    private Set<Users> members;
}
