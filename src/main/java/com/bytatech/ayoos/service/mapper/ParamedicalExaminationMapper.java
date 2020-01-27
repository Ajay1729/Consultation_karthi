package com.bytatech.ayoos.service.mapper;

import com.bytatech.ayoos.domain.*;
import com.bytatech.ayoos.service.dto.ParamedicalExaminationDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ParamedicalExamination and its DTO ParamedicalExaminationDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ParamedicalExaminationMapper extends EntityMapper<ParamedicalExaminationDTO, ParamedicalExamination> {



    default ParamedicalExamination fromId(Long id) {
        if (id == null) {
            return null;
        }
        ParamedicalExamination paramedicalExamination = new ParamedicalExamination();
        paramedicalExamination.setId(id);
        return paramedicalExamination;
    }
}
