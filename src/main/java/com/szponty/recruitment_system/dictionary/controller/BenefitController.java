package com.szponty.recruitment_system.dictionary.controller;

import com.szponty.recruitment_system.dictionary.model.Benefit;
import com.szponty.recruitment_system.dictionary.service.BenefitService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dictionary/benefit")
public class BenefitController extends AbstractDictionaryController<Benefit> {
    public BenefitController(BenefitService service) {
        super(service);
    }
}