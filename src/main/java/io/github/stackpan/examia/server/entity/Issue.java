package io.github.stackpan.examia.server.entity;

import io.github.stackpan.examia.server.data.enums.IssueType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "issues")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Issue implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "uuid")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "case_id")
    private Case examiaCase;

    private Long sequence;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IssueType type;

    @Column(nullable = false)
    private String body;

    @Column(columnDefinition = "timestamptz")
    @CreationTimestamp
    private Instant createdAt;

    @Column(columnDefinition = "timestamptz")
    @UpdateTimestamp
    private Instant updatedAt;

}
