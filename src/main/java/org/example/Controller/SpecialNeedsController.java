package org.example.Controller;

import lombok.RequiredArgsConstructor;
import org.example.Model.SpecialNeeds;
import org.example.Repository.SpecialNeedRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/special-needs")
@RequiredArgsConstructor
public class SpecialNeedsController
{

    private final SpecialNeedRepository specialNeedRepository;

    @GetMapping("/all")
    public List<SpecialNeeds> getAllNeeds()
    {
        return specialNeedRepository.findAll();
    }
}