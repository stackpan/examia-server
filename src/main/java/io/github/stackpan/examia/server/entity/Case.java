package io.github.stackpan.examia.server.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "cases")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Case implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "uuid")
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(length = 50, nullable = false)
    private String title;

    @Column(length = 500)
    private String description;

    @Column
    private Integer durationInSeconds;

    @Column(columnDefinition = "timestamptz")
    @CreationTimestamp
    private Instant createdAt;

    @Column(columnDefinition = "timestamptz")
    @UpdateTimestamp
    private Instant updatedAt;

    @Column(nullable = false)
    private boolean isSoftDeleted = false;

}
