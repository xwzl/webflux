package com.spring.webflux.repository;

import com.spring.webflux.domain.City;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author xuweizhi
 * @since 2019/07/04 17:28
 */
@Repository
public interface CityRepository extends ReactiveMongoRepository<City, Long> {


}
