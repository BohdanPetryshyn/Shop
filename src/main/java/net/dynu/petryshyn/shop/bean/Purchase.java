package net.dynu.petryshyn.shop.bean;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.Objects;


@Entity(name = "Purchase")
@Table(name="purchase")
public class Purchase implements Serializable {
    @Id
    @GeneratedValue
    @Column(name="id")
    private long id;

    @Column(name="name")
    private String name;

    @Column(name="price")
    private BigDecimal prise;

    @Column(name="currency")
    private Currency currency;

    @Column(name="date")
    private LocalDate date;

    public Purchase() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrise() {
        return prise;
    }

    public void setPrise(BigDecimal prise) {
        this.prise = prise;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Purchase{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", prise=" + prise +
                ", currency=" + currency +
                ", date=" + date +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Purchase purchase = (Purchase) o;
        return Objects.equals(name, purchase.name) &&
                Objects.equals(prise, purchase.prise) &&
                Objects.equals(currency, purchase.currency) &&
                Objects.equals(date, purchase.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, prise, currency, date);
    }
}
