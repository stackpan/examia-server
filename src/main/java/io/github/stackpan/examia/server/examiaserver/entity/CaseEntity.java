package io.github.stackpan.examia.server.examiaserver.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "cases")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "uuid")
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private UserEntity owner;

    @Column(length = 50, nullable = false)
    private String title;

    @Column(length = 500)
    private String description;

    @Column
    private Integer durationInSeconds;

    @Column(columnDefinition = "timestamptz")
    private OffsetDateTime createdAt;

    @Column(columnDefinition = "timestamptz")
    private OffsetDateTime updatedAt;

    @Column(nullable = false)
    private boolean isSoftDeleted = false;

}
