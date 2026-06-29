package com.szponty.recruitment_system.dictionary.controller;

import com.szponty.recruitment_system.dictionary.model.ContractType;
import com.szponty.recruitment_system.dictionary.service.ContractTypeService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dictionary/contract-type")
public class ContractTypeController extends AbstractDictionaryController<ContractType> {
    public ContractTypeController(ContractTypeService service) {
        super(service);
    }
}
