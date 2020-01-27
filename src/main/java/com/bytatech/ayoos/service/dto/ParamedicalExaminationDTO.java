package com.bytatech.ayoos.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the ParamedicalExamination entity.
 */
public class ParamedicalExaminationDTO implements Serializable {

    private Long id;

    private String bp;

    private Double height;

    private Double weight;

    private Double temperature;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBp() {
        return bp;
    }

    public void setBp(String bp) {
        this.bp = bp;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ParamedicalExaminationDTO paramedicalExaminationDTO = (ParamedicalExaminationDTO) o;
        if (paramedicalExaminationDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), paramedicalExaminationDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ParamedicalExaminationDTO{" +
            "id=" + getId() +
            ", bp='" + getBp() + "'" +
            ", height=" + getHeight() +
            ", weight=" + getWeight() +
            ", temperature=" + getTemperature() +
            "}";
    }
}
