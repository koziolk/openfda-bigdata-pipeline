package io.bigdata.openfda.producer.kafka;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@ToString
@Builder
@Getter
public class DrugAdverseEvent implements Serializable {
    // general
    private String country;
    private String receiveDate;
    // 1 - death, 2 - survived
    private Integer serious;

    // patient
    private Integer patientAge;

    // 1 - female, 2 - male
    private Integer patientSex;
    private List<String> patientReactions;

    // drug
    private List<String> medicinalProduct;
    private List<String> drugBrandNames;
    private List<String> drugGenericNames;
    private List<String> drugSubstanceNames;
    private List<String> drugManufacturerNames;
}