package com.coolservice.backendangularandspringsecurity.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "roles")
public class Role extends SuperEntity {
    private String name;
}
