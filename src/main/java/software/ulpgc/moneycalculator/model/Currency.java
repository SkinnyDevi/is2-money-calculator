package software.ulpgc.moneycalculator.model;

import java.util.Objects;

public record Currency(String symbol, String name, String code) {
    public static final Currency NULL = new Currency("NULL", "NULL", "NULL");

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Currency currency = (Currency) o;
        return Objects.equals(code, currency.code);
    }

    @Override
    public int hashCode() {
        return code().hashCode() * 24 + name().hashCode();
    }

    @Override
    public String toString() {
        return "(" + symbol() + ") " + "[" + code() + "] - " + name();
    }
}