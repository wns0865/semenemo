package com.semonemo.spring_server.domain.elasticsearch.dto;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

import com.semonemo.spring_server.domain.blockchain.dto.NFTInfoDto;
import com.semonemo.spring_server.domain.elasticsearch.document.AssetSellDocument;
import com.semonemo.spring_server.domain.elasticsearch.document.NFTSellDocument;
import com.semonemo.spring_server.domain.nft.entity.NFTMarket;
import com.semonemo.spring_server.domain.user.dto.response.UserInfoResponseDTO;

public record NftSearchResponseDto(
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
