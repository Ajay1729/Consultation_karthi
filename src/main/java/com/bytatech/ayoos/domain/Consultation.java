package com.bytatech.ayoos.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Consultation.
 */
@Entity
@Table(name = "consultation")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "consultation")
public class Consultation implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tracking_id")
    private String trackingId;

    @Column(name = "jhi_date")
    private LocalDate date;

    @OneToOne
    @JoinColumn(unique = true)
    private Prescription prescription;

    @OneToOne
    @JoinColumn(unique = true)
    private Diagnosis diagnosis;

    @OneToOne
    @JoinColumn(unique = true)
    private ParamedicalExamination paramedicalExamination;

    @OneToMany(mappedBy = "consultation",cascade=CascadeType.ALL)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Symptom> symptoms = new HashSet<>();
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTrackingId() {
        return trackingId;
    }

    public Consultation trackingId(String trackingId) {
        this.trackingId = trackingId;
        return this;
    }

    public void setTrackingId(String trackingId) {
        this.trackingId = trackingId;
    }

    public LocalDate getDate() {
        return date;
    }

    public Consultation date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Prescription getPrescription() {
        return prescription;
    }

    public Consultation prescription(Prescription prescription) {
        this.prescription = prescription;
        return this;
    }

    public void setPrescription(Prescription prescription) {
        this.prescription = prescription;
    }

    public Diagnosis getDiagnosis() {
        return diagnosis;
    }

    public Consultation diagnosis(Diagnosis diagnosis) {
        this.diagnosis = diagnosis;
        return this;
    }

    public void setDiagnosis(Diagnosis diagnosis) {
        this.diagnosis = diagnosis;
    }

    public ParamedicalExamination getParamedicalExamination() {
        return paramedicalExamination;
    }

    public Consultation paramedicalExamination(ParamedicalExamination paramedicalExamination) {
        this.paramedicalExamination = paramedicalExamination;
        return this;
    }

    public void setParamedicalExamination(ParamedicalExamination paramedicalExamination) {
        this.paramedicalExamination = paramedicalExamination;
    }

    public Set<Symptom> getSymptoms() {
        return symptoms;
    }

    public Consultation symptoms(Set<Symptom> symptoms) {
        this.symptoms = symptoms;
        return this;
    }

    public Consultation addSymptom(Symptom symptom) {
        this.symptoms.add(symptom);
        symptom.setConsultation(this);
        return this;
    }

    public Consultation removeSymptom(Symptom symptom) {
        this.symptoms.remove(symptom);
        symptom.setConsultation(null);
        return this;
    }

    public void setSymptoms(Set<Symptom> symptoms) {
        this.symptoms = symptoms;
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
        Consultation consultation = (Consultation) o;
        if (consultation.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), consultation.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Consultation{" +
            "id=" + getId() +
            ", trackingId='" + getTrackingId() + "'" +
            ", date='" + getDate() + "'" +
            "}";
    }
}
