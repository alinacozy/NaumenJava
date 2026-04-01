package ru.alinacozy.NauJava.entity;

import jakarta.persistence.*;

/**
 * Класс-сущность инвентаря пользователя.
 * Объект этого класса отражает, в каком количестве есть конкретная нитка у конкретного пользователя
 */
@Entity
@Table(name = "inventory")
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "floss_id", nullable = false)
    private Floss floss;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Integer quantity;

    public Inventory() {}

    public Inventory(Floss floss, User user, Integer quantity) {
        this.floss = floss;
        this.user = user;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Floss getFloss() {
        return floss;
    }

    public void setFloss(Floss floss) {
        this.floss = floss;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
