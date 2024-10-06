package com.semonemo.spring_server.domain.asset.controller;

import java.math.BigInteger;
import java.util.List;
import java.util.Objects;

import com.semonemo.spring_server.domain.asset.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.EventValues;
import org.web3j.abi.datatypes.Type;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;

import com.semonemo.spring_server.config.s3.S3Service;
import com.semonemo.spring_server.domain.asset.model.AssetImage;
import com.semonemo.spring_server.domain.asset.model.AssetSell;
import com.semonemo.spring_server.domain.asset.service.AssetService;
import com.semonemo.spring_server.domain.blockchain.dto.event.TradeEvent;
import com.semonemo.spring_server.domain.blockchain.service.BlockChainService;
import com.semonemo.spring_server.domain.nft.dto.response.NFTMarketLikedResponseDto;
import com.semonemo.spring_server.domain.nft.dto.response.NFTResponseDto;
import com.semonemo.spring_server.domain.user.entity.Users;
import com.semonemo.spring_server.domain.user.service.UserService;
import com.semonemo.spring_server.global.common.CommonResponse;
import com.semonemo.spring_server.global.common.CursorResult;
import com.semonemo.spring_server.global.exception.CustomException;
import com.semonemo.spring_server.global.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/asset")
@RequiredArgsConstructor
public class AssetController implements AssetApi {
	private final S3Service s3Service;
	private final AssetService assetService;
	private final UserService userService;
	private final BlockChainService blockChainService;

	@PostMapping
	public CommonResponse<?> upload(
		@AuthenticationPrincipal UserDetails userDetails,
		@RequestPart(value = "image", required = true) MultipartFile file) {
		try {
			Users users = userService.findByAddress(userDetails.getUsername());
			String fileUrl = s3Service.upload(file);
			AssetRequestDto assetRequestDto = new AssetRequestDto();
			assetRequestDto.setCreator(users.getId());
			assetRequestDto.setImageUrl(fileUrl);
			assetService.saveImage(assetRequestDto);
			return CommonResponse.success(assetRequestDto, "업로드 성공");
		} catch (Exception e) {
			throw new CustomException(ErrorCode.ASSET_UPLOAD_FAIL);
		}
	}

	@PostMapping("/sell")
	public CommonResponse<?> registSale(
		@AuthenticationPrincipal UserDetails userDetails,
		@RequestBody AssetSellRequestDto assetSellRequestDto
	){
		Users users = userService.findByAddress(userDetails.getUsername());
		AssetResponseDto asset = assetService.getAssetDetail(users.getId(),assetSellRequestDto.assetId());
		if(asset.creator().userId()!= users.getId()){
			throw new CustomException(ErrorCode.CREATOR_NOT_MATCH);
		}
		if(assetService.exist(assetSellRequestDto.assetId())){
			throw new CustomException(ErrorCode.ASSET_ON_SALE);
		}
		assetService.registSale(users.getId(),assetSellRequestDto);
		return CommonResponse.success("에셋 판매등록 성공");
	}

	// NFT 구매
	@PostMapping("/purchase")
	public CommonResponse<?> buyAsset(
        @AuthenticationPrincipal UserDetails userDetails,
        @RequestBody AssetBuyRequestDto assetBuyRequestDto) {
		try {
			TransactionReceipt transactionResult = blockChainService.waitForTransactionReceipt(assetBuyRequestDto.getTxHash());

			BigInteger tradeId = null;

			if (Objects.equals(transactionResult.getStatus(), "0x1")) {
				for (org.web3j.protocol.core.methods.response.Log txLog : transactionResult.getLogs()) {
					String eventHash = EventEncoder.encode(TradeEvent.TRADE_RECORDED_EVENT);

					if (txLog.getTopics().get(0).equals(eventHash)) {
						EventValues eventValues = Contract.staticExtractEventParameters(
							TradeEvent.TRADE_RECORDED_EVENT, txLog
						);

						if (eventValues != null) {
							List<Type> indexedValues = eventValues.getIndexedValues();

							tradeId = (BigInteger) indexedValues.get(0).getValue();

						} else {
							throw new CustomException(ErrorCode.BLOCKCHAIN_ERROR);
						}
					}
				}
			} else {
				throw new CustomException(ErrorCode.BLOCKCHAIN_ERROR);
			}
			Users users = userService.findByAddress(userDetails.getUsername());
			assetService.assetBuy(users, assetBuyRequestDto.getAssetSellId(), tradeId);
			return CommonResponse.success("에셋 구매 성공");
		} catch (Exception e) {
			throw new CustomException(ErrorCode.MARKET_BUY_FAIL);
		}
	}


	//에셋 상세 조회
	@GetMapping("/{assetId}/detail")
	public CommonResponse<AssetResponseDto> getAssetDetail(
		@AuthenticationPrincipal UserDetails userDetails,
		@PathVariable Long assetId) {
		try {
		Users users = userService.findByAddress(userDetails.getUsername());
		AssetResponseDto asset = assetService.getAssetDetail(users.getId(),assetId);
		return CommonResponse.success(asset, "에셋 상세조회 성공");
		} catch (Exception e) {
			throw new CustomException(ErrorCode.ASSET_DETAIL_FAIL);
		}
	}

	//판매 에셋 상세 조회
	@GetMapping("/sell/{assetSellId}/detail")
	public CommonResponse<AssetDetailResponseDto> getAssetSellDetail(
		@AuthenticationPrincipal UserDetails userDetails,
		@PathVariable Long assetSellId) {
		try{
		Users users = userService.findByAddress(userDetails.getUsername());
			AssetDetailResponseDto asset = assetService.getAssetSellDetail(users.getId(), assetSellId);
		return CommonResponse.success(asset, "판매 에셋 상세조회 성공");
		}catch (Exception e) {
			throw new CustomException(ErrorCode.SELL_DETAIL_FAIL);
		}
	}

	//판매중인 모든 에셋
	@GetMapping
	public CommonResponse<CursorResult<AssetSellResponseDto>> getAllAsset(
		@AuthenticationPrincipal UserDetails userDetails,
		@RequestParam(required = false) Long cursorId,
		@RequestParam(defaultValue = "40") int size

	) {
		try {
			Users users = userService.findByAddress(userDetails.getUsername());
			CursorResult<AssetSellResponseDto> result = assetService.getAllAsset(users.getId(), cursorId, size);
			return CommonResponse.success(result, "전체조회 성공");
		} catch (Exception e) {
			throw new CustomException(ErrorCode.ASSET_LOAD_FAIL);
		}
	}
	// 모든 에셋 조회 정렬
	@GetMapping("/sort")
	public CommonResponse<?> getAllAssetSort(
		@AuthenticationPrincipal UserDetails userDetails,
		@RequestParam(defaultValue = "latest") String orderBy,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "40") int size

	) {
		try {
			Users users = userService.findByAddress(userDetails.getUsername());
			Page<AssetSellResponseDto> result = assetService.getAllAssetSort(users.getId(),orderBy, page, size);
			return CommonResponse.success(result, orderBy+"로 정렬 결과 조회 성공");
		} catch (Exception e) {
			throw new CustomException(ErrorCode.ASSET_LOAD_FAIL);
		}
	}

	//보유중인 에셋 조회
	@GetMapping("/mine")
	public CommonResponse<?> getMyAsset(
		@AuthenticationPrincipal UserDetails userDetails,
		@RequestParam(required = false) Long cursorId,
		@RequestParam(defaultValue = "40") int size
	) {
		try {
			Users users = userService.findByAddress(userDetails.getUsername());
			CursorResult<AssetResponseDto> result = assetService.getMyAsset(users.getId(), cursorId, size);
			return CommonResponse.success(result, "보유한 에셋 가져오기 성공");
		} catch (Exception e) {
			throw new CustomException(ErrorCode.MINE_LOAD_FAIL);
		}

	}

	@GetMapping("/creator")
	public CommonResponse<?> getCreator(
		@AuthenticationPrincipal UserDetails userDetails,
		@RequestParam Long userId,
		@RequestParam(required = false) Long cursorId,
		@RequestParam(defaultValue = "40") int size) {
		try {
			Users users = userService.findByAddress(userDetails.getUsername());
			CursorResult<AssetResponseDto> result = assetService.getUserAsset(users.getId(), userId, cursorId, size);
			return CommonResponse.success(result, "유저생성 에셋 불러오기 성공");
		} catch (Exception e) {
			throw new CustomException(ErrorCode.USERS_LOAD_FAIL);
		}
	}

	@PostMapping("/{assetSellId}/like")
	public CommonResponse<?> like(
		@AuthenticationPrincipal UserDetails userDetails,
		@PathVariable Long assetSellId
	) {
		try {
			Users users = userService.findByAddress(userDetails.getUsername());
			if (assetService.checkLike(users.getId(), assetSellId)) {
				throw new CustomException(ErrorCode.LIKE_ALREADY_EXIST);
			}
			assetService.like(users.getId(), assetSellId);
			AssetDetailResponseDto assetSell = assetService.getAssetSellDetail(users.getId(),assetSellId);
			AssetLikeDto likeDto = new AssetLikeDto(
				assetSellId,
				assetSell.likeCount()
			);
			return CommonResponse.success(likeDto,"좋아요 성공");
		} catch (CustomException e) {
			throw e;
		} catch (Exception e) {
			throw new CustomException(ErrorCode.LIKE_FAIL);
		}
	}

	@DeleteMapping("/{assetSellId}/like")
	public CommonResponse<?> dislike(
		@AuthenticationPrincipal UserDetails userDetails,
		@PathVariable Long assetSellId
	) {
		try {
			Users users = userService.findByAddress(userDetails.getUsername());
			if (!assetService.checkLike(users.getId(), assetSellId)) {
				throw new CustomException(ErrorCode.LIKE_NOT_FOUND_ERROR);
			}
			assetService.dislike(users.getId(), assetSellId);
			AssetDetailResponseDto assetSell = assetService.getAssetSellDetail(users.getId(),assetSellId);
			AssetLikeDto likeDto = new AssetLikeDto(
				assetSellId,
				assetSell.likeCount()-1L
			);
			return CommonResponse.success(likeDto,"좋아요 취소 성공");
		} catch (CustomException e) {
			throw e;
		} catch (Exception e) {
			throw new CustomException(ErrorCode.DISLIKE_FAIL);
		}
	}
	@GetMapping("/like")
	public CommonResponse<?> getLikeAsset(
		@AuthenticationPrincipal UserDetails userDetails,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "40") int size
	){
		Users users = userService.findByAddress(userDetails.getUsername());
		Page<AssetSellResponseDto> result = assetService.getLikeAsset(users,page,size);
		return CommonResponse.success(result,"좋아요 에셋목록 조회 성공");
	}

}
