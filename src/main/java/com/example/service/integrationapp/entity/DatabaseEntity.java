package com.example.service.integrationapp.entity;

import com.example.service.integrationapp.model.EntityModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "entityes")
public class DatabaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;
    private Instant date;

    public static DatabaseEntity from(EntityModel model) {
        return new DatabaseEntity(model.getId(), model.getName(), model.getDate());
    }
}
