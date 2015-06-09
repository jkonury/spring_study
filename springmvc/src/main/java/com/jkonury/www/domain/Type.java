package com.jkonury.www.domain;

import lombok.Data;

@Data
public class Type {
    int id;
    String name;

    public Type() {
    }

    public Type(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
