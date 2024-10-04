package com.semonemo.spring_server.domain.nft.dto.response;

import com.semonemo.spring_server.domain.user.dto.response.UserInfoResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Optional;

import com.semonemo.spring_server.domain.blockchain.dto.NFTInfoDto;

public record NFTMarketResponseDto (
    Long marketId,
    Long nftId,
    UserInfoResponseDTO seller,
    Long price,
    int likeCount,
    boolean isLiked,
    NFTInfoDto nftInfo,
    List<String> tags
) {
}
