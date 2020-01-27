package com.bytatech.ayoos.service.mapper;

import com.bytatech.ayoos.domain.*;
import com.bytatech.ayoos.service.dto.DiagnosisDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Diagnosis and its DTO DiagnosisDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DiagnosisMapper extends EntityMapper<DiagnosisDTO, Diagnosis> {



    default Diagnosis fromId(Long id) {
        if (id == null) {
            return null;
        }
        Diagnosis diagnosis = new Diagnosis();
        diagnosis.setId(id);
        return diagnosis;
    }
}
