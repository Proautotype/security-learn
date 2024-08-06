package com.eazybytes.springsecsecurity1.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "authorities")
public class Authority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

}
