package io.bigdata.openfda.consumer.mongo;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "drugAdverseEvent")
public class DrugAdverseEventDocument {
    @Id
    private ObjectId id;

    private String country;
    private String receiveDate;
    private Integer serious;

    private Integer patientAge;
    private Integer patientSex;
    private List<String> patientReactions;

    private List<String> medicinalProduct;
    private List<String> drugBrandNames;
    private List<String> drugGenericNames;
    private List<String> drugSubstanceNames;
    private List<String> drugManufacturerNames;
}