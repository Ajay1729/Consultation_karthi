package com.bytatech.ayoos.service.mapper;

import com.bytatech.ayoos.domain.*;
import com.bytatech.ayoos.service.dto.SymptomDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Symptom and its DTO SymptomDTO.
 */
@Mapper(componentModel = "spring", uses = {ConsultationMapper.class})
public interface SymptomMapper extends EntityMapper<SymptomDTO, Symptom> {

    @Mapping(source = "consultation.id", target = "consultationId")
    SymptomDTO toDto(Symptom symptom);

    @Mapping(source = "consultationId", target = "consultation")
    Symptom toEntity(SymptomDTO symptomDTO);

    default Symptom fromId(Long id) {
        if (id == null) {
            return null;
        }
        Symptom symptom = new Symptom();
        symptom.setId(id);
        return symptom;
    }
}
