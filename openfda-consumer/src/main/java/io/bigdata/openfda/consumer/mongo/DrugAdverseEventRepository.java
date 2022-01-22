package io.bigdata.openfda.consumer.mongo;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DrugAdverseEventRepository extends MongoRepository<DrugAdverseEventDocument, ObjectId> {
}
