package io.bigdata.openfda.producer.kafka;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DrugAdverseEvent implements Serializable {
    private String country;
    private String receiveDate;
    private Integer serious;

    // patient
    private Integer patientAge;
    private Integer patientSex;
    private List<String> patientReactions;

    // drug
    private List<String> medicinalProduct;
    private List<String> drugBrandNames;
    private List<String> drugGenericNames;
    private List<String> drugSubstanceNames;
    private List<String> drugManufacturerNames;
}