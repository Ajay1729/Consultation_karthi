package com.bytatech.ayoos.service.mapper;

import com.bytatech.ayoos.domain.*;
import com.bytatech.ayoos.service.dto.ConsultationDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Consultation and its DTO ConsultationDTO.
 */
@Mapper(componentModel = "spring", uses = {PrescriptionMapper.class, DiagnosisMapper.class, ParamedicalExaminationMapper.class})
public interface ConsultationMapper extends EntityMapper<ConsultationDTO, Consultation> {

    @Mapping(source = "prescription.id", target = "prescriptionId")
    @Mapping(source = "diagnosis.id", target = "diagnosisId")
    @Mapping(source = "paramedicalExamination.id", target = "paramedicalExaminationId")
    ConsultationDTO toDto(Consultation consultation);

    @Mapping(source = "prescriptionId", target = "prescription")
    @Mapping(source = "diagnosisId", target = "diagnosis")
    @Mapping(source = "paramedicalExaminationId", target = "paramedicalExamination")
    @Mapping(target = "symptoms", ignore = true)
    Consultation toEntity(ConsultationDTO consultationDTO);

    default Consultation fromId(Long id) {
        if (id == null) {
            return null;
        }
        Consultation consultation = new Consultation();
        consultation.setId(id);
        return consultation;
    }
}
