package com.example.application.data.entity;

public enum Status {

    PAID("Zapłacone", "Wydatek został już opłacony"),
    UNPAID("Niezapłacone", "Wydatek nie został jeszcze opłacony");

    private String name;
    private String desc;


    Status(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public static Status getRandom() {
        return values()[(int) (Math.random() * values().length)];
    }

}
