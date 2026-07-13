package com.szponty.recruitment_system.recruitmentprocess.mapper;

import com.szponty.recruitment_system.recruitmentprocess.DTO.ProcessVersionStepResponse;
import com.szponty.recruitment_system.recruitmentprocess.model.ProcessVersionStep;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProcessVersionStepMapper {

    private final ProcessStepMapper processStepMapper;

    public ProcessVersionStepResponse toResponse(
            ProcessVersionStep entity
    ) {
        return new ProcessVersionStepResponse(
                entity.getId(),
                entity.getStepOrder(),
                processStepMapper.toResponse(entity.getProcessStep())
        );
    }
}
