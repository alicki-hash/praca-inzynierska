package com.arkadiusz.application.data.entity;

public enum Status {

    PAID("Zapłacone", "Wydatek został już opłacony"),
    UNPAID("Niezapłacone", "Wydatek nie został jeszcze opłacony");

    private String name;
    private String description;


    Status(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public static Status getRandom() {
        return values()[(int) (Math.random() * values().length)];
    }

}
