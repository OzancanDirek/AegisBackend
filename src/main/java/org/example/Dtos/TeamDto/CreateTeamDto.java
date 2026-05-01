package org.example.Dtos.TeamDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateTeamDto
{
    private String teamName;

    private String teamType; // Arama Kurtarma, Sağlık, Lojistik vb.

    private String status = "AVAILABLE";

    private Set<Integer> memberIds;
}
