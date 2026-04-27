package org.example.Service;

import org.example.Dtos.SpecialNeedsDto.CreateSpeicalNeeds;
import org.example.Model.SpecialNeeds;

public interface ISpecialNeedService
{
    public SpecialNeeds createSpecialNeed(CreateSpeicalNeeds createSpeicalNeeds);
}
