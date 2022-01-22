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
public class OpenFda {
    @JsonProperty("application_number")
    private List<String> applicationNumbers;

    @JsonProperty("brand_name")
    private List<String> brandNames;

    @JsonProperty("generic_name")
    private List<String> genericNames;

    @JsonProperty("manufacturer_name")
    private List<String> manufacturerNames;

    @JsonProperty("substance_name")
    private List<String> substanceNames;

    @JsonProperty("route")
    private List<String> routes;
}
