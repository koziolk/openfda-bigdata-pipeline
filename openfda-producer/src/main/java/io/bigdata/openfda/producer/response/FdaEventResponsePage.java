package io.bigdata.openfda.producer.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Builder
public class FdaEventResponsePage {
    private FdaEventResponse response;
    private String nextPageLink;

    public boolean hasNextPage() {
        return StringUtils.isNotEmpty(nextPageLink);
    }
}