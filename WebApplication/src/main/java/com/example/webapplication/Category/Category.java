package com.example.webapplication.Category;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name ="categories")
@XmlAccessorType(XmlAccessType.FIELD)
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @XmlTransient
    private Long categoryId;
    private String name;

    public Category(String caterogoryName) {
        this.name = caterogoryName;
    }

    @Override
    public String toString() {
        return "Category{" +
                "categoryId=" + categoryId +
                ", name='" + name + '\'' +
                '}';
    }

    public Category() {
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCaterogoryName() {
        return name;
    }

    public void setCaterogoryName(String caterogoryName) {
        this.name = caterogoryName;
    }

}
