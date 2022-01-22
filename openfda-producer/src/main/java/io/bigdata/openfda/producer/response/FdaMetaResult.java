package io.bigdata.openfda.producer.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class FdaMetaResult {
    private long skip;
    private long limit;
    private long total;
}
