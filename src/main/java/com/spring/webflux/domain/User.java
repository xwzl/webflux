package com.spring.webflux.domain;

import lombok.Data;

/**
 * 基础数据类
 *
 * @author xuweizhi
 * @since 2019/07/04 16:10
 */
@Data
public class User {

    private String id;

    private String name;

    private String email;

}
