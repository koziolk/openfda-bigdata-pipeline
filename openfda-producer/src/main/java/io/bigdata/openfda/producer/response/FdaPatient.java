package io.bigdata.openfda.producer.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class FdaPatient {
    @JsonProperty("patientonsetage")
    private Integer patientAge;

    @JsonProperty("patientsex")
    private Integer patientSex;

    @JsonProperty("reaction")
    private List<FdaReaction> reactions;

    @JsonProperty("drug")
    private List<FdaDrug> drugs;
}