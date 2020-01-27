package com.bytatech.ayoos.service.dto;
import java.time.LocalDate;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Consultation entity.
 */
public class ConsultationDTO implements Serializable {

    private Long id;

    private String trackingId;

    private LocalDate date;


    private Long prescriptionId;

    private Long diagnosisId;

    private Long paramedicalExaminationId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTrackingId() {
        return trackingId;
    }

    public void setTrackingId(String trackingId) {
        this.trackingId = trackingId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Long getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(Long prescriptionId) {
        this.prescriptionId = prescriptionId;
    }

    public Long getDiagnosisId() {
        return diagnosisId;
    }

    public void setDiagnosisId(Long diagnosisId) {
        this.diagnosisId = diagnosisId;
    }

    public Long getParamedicalExaminationId() {
        return paramedicalExaminationId;
    }

    public void setParamedicalExaminationId(Long paramedicalExaminationId) {
        this.paramedicalExaminationId = paramedicalExaminationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ConsultationDTO consultationDTO = (ConsultationDTO) o;
        if (consultationDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), consultationDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ConsultationDTO{" +
            "id=" + getId() +
            ", trackingId='" + getTrackingId() + "'" +
            ", date='" + getDate() + "'" +
            ", prescription=" + getPrescriptionId() +
            ", diagnosis=" + getDiagnosisId() +
            ", paramedicalExamination=" + getParamedicalExaminationId() +
            "}";
    }
}
