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
public class FdaDrug {
    @JsonProperty("medicinalproduct")
    private String medicinalProduct;

    @JsonProperty("drugindication")
    private String drugIndication;

    @JsonProperty("openfda")
    private OpenFda openFda;
}