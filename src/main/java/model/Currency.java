package model;

public record Currency(String symbol, String name, String code) {
    public static final Currency NULL = new Currency("NULL", "NULL", "NULL");
}