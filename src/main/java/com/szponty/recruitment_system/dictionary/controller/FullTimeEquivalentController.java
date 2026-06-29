package com.szponty.recruitment_system.dictionary.controller;

import com.szponty.recruitment_system.dictionary.model.FullTimeEquivalent;
import com.szponty.recruitment_system.dictionary.service.FullTimeEquivalentService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dictionaries/fte")
public class FullTimeEquivalentController extends AbstractDictionaryController<FullTimeEquivalent> {
    public FullTimeEquivalentController (FullTimeEquivalentService service) {
        super(service);
    }
}
