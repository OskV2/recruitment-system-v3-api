package com.szponty.recruitment_system.dictionary.controller;

import com.szponty.recruitment_system.dictionary.model.ContractType;
import com.szponty.recruitment_system.dictionary.model.Department;
import com.szponty.recruitment_system.dictionary.service.DepartmentService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dictionary/department")
public class DepartmentController extends AbstractDictionaryController<Department> {
    public DepartmentController(DepartmentService service) {
        super(service);
    }
}
