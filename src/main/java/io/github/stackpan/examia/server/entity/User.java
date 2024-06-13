package io.github.stackpan.examia.server.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(length = 50, nullable = false, unique = true)
    private String username;

    @Column(length = 50, nullable = false, unique = true)
    private String email;

    @Column(length = 50, nullable = false)
    private String firstName;

    @Column(length = 100)
    private String lastName;

    @Column(length = 1000, nullable = false)
    private String password;

    @Column(columnDefinition = "timestamptz")
    @CreationTimestamp
    private Instant createdAt;

    @Column(columnDefinition = "timestamptz")
    @UpdateTimestamp
    private Instant updatedAt;

    @Column(nullable = false)
    private boolean isSoftDeleted = false;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<Case> cases = new ArrayList<>();

}
