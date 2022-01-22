package io.bigdata.openfda.producer.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class FdaResult {
    @JsonProperty("safetyreportid")
    private String safetyReportId;

    @JsonProperty("occurcountry")
    private String occurrenceCountry;

    @JsonProperty("receivedate")
    private String receiveDate;

    @JsonProperty("receiptdate")
    private String receiptDate;

    @JsonProperty("companynumb")
    private String companyNumb;

    // 1 - death
    // 2 - survived
    @JsonProperty("serious")
    private Integer serious;

    @JsonProperty("primarysource")
    private FdaPrimarySource primarySource;

    @JsonProperty("patient")
    private FdaPatient patient;
}