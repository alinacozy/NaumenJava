package ru.alinacozy.NauJava.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;


/**
 * Класс-сущность нитки, необходимой для проекта.
 * Содержит информацию о цвете и количестве, нужном для конкретного проекта.
 * Здесь может быть задана связь с конкретной Floss из соответствующей таблицы,
 * а может быть задан кастомный цвет в формате RGB (если точный номер из каталога не указан в схеме).
 */
@Entity
@Table(name = "required_flosses")
public class RequiredFloss {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "project_id", nullable = false)
    @JsonBackReference
    private Project project;

    @ManyToOne(optional = true)
    @JoinColumn(name = "floss_id", nullable = true)
    private Floss floss;

    // Кастомный цвет (когда floss_id is NULL)
    @Column(name = "custom_red")
    private Integer customRed;

    @Column(name = "custom_green")
    private Integer customGreen;

    @Column(name = "custom_blue")
    private Integer customBlue;

    @Column(name = "custom_color_name")
    private String customColorName;

    @Column(nullable = false)
    private Integer quantity;

    // Constructors
    public RequiredFloss() {}

    // Constructor for branded floss
    public RequiredFloss(Project project, Floss floss, Integer quantity) {
        this.project = project;
        this.floss = floss;
        this.quantity = quantity;
    }

    // Constructor for custom RGB color
    public RequiredFloss(Project project, Integer customRed, Integer customGreen,
                         Integer customBlue, String customColorName, Integer quantity) {
        this.project = project;
        this.customRed = customRed;
        this.customGreen = customGreen;
        this.customBlue = customBlue;
        this.customColorName = customColorName;
        this.quantity = quantity;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Floss getFloss() {
        return floss;
    }

    public void setFloss(Floss floss) {
        this.floss = floss;
    }

    public Integer getCustomRed() {
        return customRed;
    }

    public void setCustomRed(Integer customRed) {
        this.customRed = customRed;
    }

    public Integer getCustomGreen() {
        return customGreen;
    }

    public void setCustomGreen(Integer customGreen) {
        this.customGreen = customGreen;
    }

    public Integer getCustomBlue() {
        return customBlue;
    }

    public void setCustomBlue(Integer customBlue) {
        this.customBlue = customBlue;
    }

    public String getCustomColorName() {
        return customColorName;
    }

    public void setCustomColorName(String customColorName) {
        this.customColorName = customColorName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    // Helper method to check if this is a branded floss or custom color
    public boolean isBrandedFloss() {
        return floss != null;
    }

    public boolean isCustomColor() {
        return floss == null && customRed != null && customGreen != null && customBlue != null;
    }
}
