package com.xdesign.example.demo.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.opencsv.bean.CsvBindByName;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Summit {
    @Id
    @JsonIgnore
    @CsvBindByName(column = "DoBIH Number")
    private String dobihNumber;
    @CsvBindByName(column = "Name")
    private String name;
    @CsvBindByName(column = "Height (m)")
    private Double height;
    @CsvBindByName(column = "Post 1997")
    private String category;
    @CsvBindByName(column = "Grid Ref")
    private String gridReference;

    public String getDobihNumber() {
        return dobihNumber;
    }

    public void setDobihNumber(String dobihNumber) {
        this.dobihNumber = dobihNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getGridReference() {
        return gridReference;
    }

    public void setGridReference(String gridReference) {
        this.gridReference = gridReference;
    }
}
