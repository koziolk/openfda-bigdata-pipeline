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
public class FdaMeta {
    @JsonProperty("last_updated")
    private String lastUpdated;

    @JsonProperty("results")
    private FdaMetaResult result;
}
