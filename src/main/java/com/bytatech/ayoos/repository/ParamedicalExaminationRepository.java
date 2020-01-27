package com.bytatech.ayoos.repository;

import com.bytatech.ayoos.domain.ParamedicalExamination;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ParamedicalExamination entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ParamedicalExaminationRepository extends JpaRepository<ParamedicalExamination, Long> {

}
