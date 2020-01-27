package com.bytatech.ayoos.domain;


import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A ParamedicalExamination.
 */
@Entity
@Table(name = "paramedical_examination")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "paramedicalexamination")
public class ParamedicalExamination implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "bp")
    private String bp;

    @Column(name = "height")
    private Double height;

    @Column(name = "weight")
    private Double weight;

    @Column(name = "temperature")
    private Double temperature;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBp() {
        return bp;
    }

    public ParamedicalExamination bp(String bp) {
        this.bp = bp;
        return this;
    }

    public void setBp(String bp) {
        this.bp = bp;
    }

    public Double getHeight() {
        return height;
    }

    public ParamedicalExamination height(Double height) {
        this.height = height;
        return this;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Double getWeight() {
        return weight;
    }

    public ParamedicalExamination weight(Double weight) {
        this.weight = weight;
        return this;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getTemperature() {
        return temperature;
    }

    public ParamedicalExamination temperature(Double temperature) {
        this.temperature = temperature;
        return this;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ParamedicalExamination paramedicalExamination = (ParamedicalExamination) o;
        if (paramedicalExamination.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), paramedicalExamination.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ParamedicalExamination{" +
            "id=" + getId() +
            ", bp='" + getBp() + "'" +
            ", height=" + getHeight() +
            ", weight=" + getWeight() +
            ", temperature=" + getTemperature() +
            "}";
    }
}
