package com.spring.webflux.service;

import com.spring.webflux.domain.User;
import com.spring.webflux.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xuweizhi
 * @since 2019/07/04 16:09
 */
@Service
public class UserService {

    private final Map<String, User> data = new HashMap<>();

    /**
     * 获取素有的用户信息
     *
     * @return 用户信息 Flux 对象
     */
    public Flux<User> list() {
        return Flux.fromIterable(this.data.values());
    }


    public Flux<User> getById(final Flux<String> ids) {
        return ids.flatMap(id -> Mono.justOrEmpty(this.data.get(id)));
    }

    public Mono<User> getById(final String id) {
        return Mono.justOrEmpty(this.data.get(id))
                .switchIfEmpty(Mono.error(new ResourceNotFoundException()));
    }

    public Mono<User> createOrUpdate(final User user) {
        this.data.put(user.getId(), user);
        return Mono.just(user);
    }

    public Mono<User> delete(final String id) {
        return Mono.justOrEmpty(this.data.remove(id));
    }

}
