package com.semonemo.spring_server.domain.nft.entity;

import java.util.Set;

import com.semonemo.spring_server.domain.user.entity.Users;
import com.semonemo.spring_server.global.common.BaseTimeEntity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "nft_market")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NFTMarket extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "market_id")
    private Long marketId;

    @ManyToOne
    @JoinColumn(name = "nft_id")
    private NFTs nftId;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private Users seller;

    @Column(name = "price")
    private Long price;

    @Column(name = "like_count")
    private Long likeCount;

    @Column(name = "is_sold")
    private Boolean isSold;

    @OneToMany(mappedBy = "marketId")
    private Set<NFTMarketLike> likedMarkets;
}
