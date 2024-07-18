package com.muhammet.entity;

import com.muhammet.utility.FollowState;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "tblfollow")
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    /**
     * takip etme isteği gönderen kişi
     */
    Long userId;
    /**
     * takip edilecek kişi
     */
    Long followId;
    @Enumerated(EnumType.STRING)
    FollowState state;

}
