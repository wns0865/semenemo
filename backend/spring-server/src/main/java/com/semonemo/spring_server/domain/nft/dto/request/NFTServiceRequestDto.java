package com.semonemo.spring_server.domain.nft.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigInteger;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class NFTServiceRequestDto {
    private Long userId;
    private BigInteger tokenId;
    private List<String> tags;
    private int frameType;
}
