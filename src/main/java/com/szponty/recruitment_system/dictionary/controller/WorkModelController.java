package com.szponty.recruitment_system.dictionary.controller;

import com.szponty.recruitment_system.dictionary.model.WorkModel;
import com.szponty.recruitment_system.dictionary.service.WorkModelService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dictionary/work-model")
public class WorkModelController extends AbstractDictionaryController<WorkModel> {
    public WorkModelController(WorkModelService service) {
        super(service);
    }
}