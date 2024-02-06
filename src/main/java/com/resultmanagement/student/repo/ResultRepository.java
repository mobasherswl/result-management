package com.resultmanagement.student.repo;

import java.time.Instant;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ResultRepository extends ReactiveCrudRepository<Result, Integer> {
    @Aggregation({
        "{'$match':{'grade':?0,'obtainedMarks':{'$lte':?1},},}",
        "{'$group':{'_id':'$grade','positionInClass':{'$min':'$positionInClass',},},}"
    })
    Mono<Integer> findTopPositionInTheClass(int grade, int obtainedMarks);

    @Aggregation({
        "{'$match':{'grade':?0,},}",
        "{'$group':{'_id':'$grade','positionInClass':{'$max':'$positionInClass',},},}"
    })
    Mono<Integer> findBottomPositionInTheClass(int grade);

    @Query("{'obtainedMarks':{'$lt':?0}}")
    @Update("{ '$inc' : { 'positionInClass' : ?1 }, '$set': { 'updatedOn': ?2 } }")
    Mono<Integer> updateClassPositions(int obtainedMarks, int increment, Instant updatedOn);
}
