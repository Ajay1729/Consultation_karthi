package com.bytatech.ayoos.service.mapper;

import com.bytatech.ayoos.domain.*;
import com.bytatech.ayoos.service.dto.PrescriptionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Prescription and its DTO PrescriptionDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PrescriptionMapper extends EntityMapper<PrescriptionDTO, Prescription> {



    default Prescription fromId(Long id) {
        if (id == null) {
            return null;
        }
        Prescription prescription = new Prescription();
        prescription.setId(id);
        return prescription;
    }
}
