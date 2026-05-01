package org.example.Dtos.TeamDto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddTeamMembersDto
{
    private Integer teamId;
    private Set<UUID> userIds;
}
