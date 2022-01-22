package io.bigdata.openfda.consumer.mongo;

import io.bigdata.openfda.producer.kafka.DrugAdverseEvent;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Collection;

@Mapper
public interface DrugAdverseEventMapper {
    DrugAdverseEventMapper INSTANCE = Mappers.getMapper(DrugAdverseEventMapper.class);

    DrugAdverseEventDocument map(DrugAdverseEvent drugAdverseEvent);

    Collection<DrugAdverseEventDocument> map(Collection<DrugAdverseEvent> drugAdverseEvents);
}
