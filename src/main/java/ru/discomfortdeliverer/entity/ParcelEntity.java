package ru.discomfortdeliverer.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "parcels")
@Getter
@Setter
@ToString
public class ParcelEntity {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String form;
    private String symbol;
}
