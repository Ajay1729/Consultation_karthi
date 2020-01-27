package com.bytatech.ayoos.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Symptom.
 */
@Entity
@Table(name = "symptom")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "symptom")
public class Symptom implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "symptom_ref")
    private String symptomRef;

    @ManyToOne
    @JsonIgnoreProperties("symptoms")
    private Consultation consultation;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSymptomRef() {
        return symptomRef;
    }

    public Symptom symptomRef(String symptomRef) {
        this.symptomRef = symptomRef;
        return this;
    }

    public void setSymptomRef(String symptomRef) {
        this.symptomRef = symptomRef;
    }

    public Consultation getConsultation() {
        return consultation;
    }

    public Symptom consultation(Consultation consultation) {
        this.consultation = consultation;
        return this;
    }

    public void setConsultation(Consultation consultation) {
        this.consultation = consultation;
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
        Symptom symptom = (Symptom) o;
        if (symptom.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), symptom.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Symptom{" +
            "id=" + getId() +
            ", symptomRef='" + getSymptomRef() + "'" +
            "}";
    }
}
