package io.bigdata.openfda.producer.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Collections;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class FdaEventResponse {
    private FdaMeta meta;
    private List<FdaResult> results;

    public List<FdaResult> getResults() {
        return Collections.unmodifiableList(results);
    }
}