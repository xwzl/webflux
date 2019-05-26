package com.spring.webflux.handler;

import com.spring.webflux.domain.City;
import com.spring.webflux.reposity.CityRepository;
import org.jetbrains.annotations.Contract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Component 泛指组件，当组件不好归类的时候，使用该注解进行标注。然后用 final 和 @Autowired 标注在构造器注入 CityRepository Bean，
 *
 * @author xuweizhi
 * @since 2019/05/26 11:39
 */
@Component
public class CityHandler {
    private final CityRepository cityRepository;

    /**
     * 构造注入
     */
    @Contract(pure = true)
    @Autowired
    public CityHandler(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    /**
     * 从返回值可以看出，Mono 和 Flux 适用于两个场景，即：
     * <ul>
     * <li>Mono：实现发布者，并返回 0 或 1 个元素，即单对象</li>
     * <li>Flux：实现发布者，并返回 N 个元素，即 List 列表对象</li>
     * </ul>
     */
    //public Mono<ServerResponse> helloCity(ServerRequest request) {
    //    return ServerResponse.ok().contentType(MediaType.TEXT_PLAIN)
    //            .body(BodyInserters.fromObject("Hello, City!"));
    //}

    public Mono<Long> save(City city) {
        return Mono.create(
                cityMonoSink ->
                        cityMonoSink.success(cityRepository.save(city))
        );
    }

    public Mono<City> findCityById(Long id) {
        return Mono.justOrEmpty(cityRepository.findCityById(id));
    }

    public Flux<City> findAllCity() {
        return Flux.fromIterable(cityRepository.findAll());
    }

    public Mono<Long> modifyCity(City city) {
        return Mono.create(cityMonoSink -> cityMonoSink.success(cityRepository.updateCity(city)));
    }

    public Mono<Long> deleteCity(Long id) {
        return Mono.create(cityMonoSink -> cityMonoSink.success(cityRepository.deleteCity(id)));
    }

}

