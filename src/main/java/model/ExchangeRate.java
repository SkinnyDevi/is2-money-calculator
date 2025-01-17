package model;

import java.time.LocalDate;
import java.util.Objects;

public record ExchangeRate(Currency from, Currency to, LocalDate date, double rate) {
    public static final ExchangeRate NULL = new ExchangeRate(null, null, null, 0);

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ExchangeRate that = (ExchangeRate) o;
        return Objects.equals(to, that.to) && Objects.equals(from, that.from) && Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to, date, rate);
    }
}