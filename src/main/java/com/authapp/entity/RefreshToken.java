package com.authapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.Date;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "refresh_token")
@ToString
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    private String token;

    @Column(nullable = false)
    private Instant expiryDate;

//    public boolean isExpired() {
//        return expiryDate.before(new Date());
//    }
}
