package com.semonemo.spring_server.domain.asset.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.semonemo.spring_server.domain.asset.dto.AssetDetailResponseDto;
import com.semonemo.spring_server.domain.asset.dto.AssetRequestDto;
import com.semonemo.spring_server.domain.asset.dto.AssetResponseDto;
import com.semonemo.spring_server.domain.asset.dto.AssetSellResponseDto;
import com.semonemo.spring_server.global.common.CommonResponse;
import com.semonemo.spring_server.global.common.CursorResult;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "에셋 관리", description = "에셋 관련 API")
public interface AssetApi {

	@Operation(summary = "에셋 이미지 업로드 API", description = "사용자가 에셋 이미지를 업로드하는 API")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "업로드 성공",
			content = @Content(schema = @Schema(implementation = AssetRequestDto.class))),
		@ApiResponse(responseCode = "500", description = "서버 내부 오류",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	})
	CommonResponse<?> upload(
		@AuthenticationPrincipal UserDetails userDetails,
		@RequestPart(value = "image", required = true) MultipartFile file
	);

	@Operation(summary = "에셋 상세 조회 API", description = "특정 에셋의 상세 정보를 조회하는 API")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "에셋 상세 조회 성공",
			content = @Content(schema = @Schema(implementation = AssetResponseDto.class))),
		@ApiResponse(responseCode = "404", description = "에셋을 찾을 수 없음",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	})
	CommonResponse<AssetResponseDto> getAssetDetail(
		@AuthenticationPrincipal UserDetails userDetails,
		@PathVariable Long assetId);

	@Operation(summary = "판매 중인 에셋 상세 조회 API", description = "판매 중인 특정 에셋의 상세 정보를 조회하는 API")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "에셋 상세 조회 성공",
			content = @Content(schema = @Schema(implementation = AssetSellResponseDto.class))),
		@ApiResponse(responseCode = "404", description = "에셋을 찾을 수 없음",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	})
	CommonResponse<AssetDetailResponseDto> getAssetSellDetail(
		@AuthenticationPrincipal UserDetails userDetails,
		@PathVariable Long assetSellId);

	@Operation(summary = "판매 중인 모든 에셋 조회 API", description = "판매 중인 모든 에셋을 조회하는 API")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "전체 조회 성공",
			content = @Content(schema = @Schema(implementation = CursorResult.class))),
		@ApiResponse(responseCode = "500", description = "서버 내부 오류",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	})
	CommonResponse<CursorResult<AssetSellResponseDto>> getAllAsset(
		@AuthenticationPrincipal UserDetails userDetails,
		@RequestParam String orderBy,
		@RequestParam(required = false) Long cursorId,
		@RequestParam(defaultValue = "40") int size
	);

	@Operation(summary = "보유 중인 에셋 조회 API", description = "사용자가 보유 중인 에셋을 조회하는 API")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "보유 중인 에셋 조회 성공",
			content = @Content(schema = @Schema(implementation = CursorResult.class))),
		@ApiResponse(responseCode = "500", description = "서버 내부 오류",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	})
	CommonResponse<?> getMyAsset(
		@AuthenticationPrincipal UserDetails userDetails,
		@RequestParam(required = false) Long cursorId,
		@RequestParam(defaultValue = "40") int size
	);

	@Operation(summary = "사용자가 생성한 에셋 조회 API", description = "특정 사용자가 생성한 에셋을 조회하는 API")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "유저가 생성한 에셋 조회 성공",
			content = @Content(schema = @Schema(implementation = CursorResult.class))),
		@ApiResponse(responseCode = "500", description = "서버 내부 오류",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	})
	CommonResponse<?> getCreator(
		@AuthenticationPrincipal UserDetails userDetails,
		@RequestParam Long userId,
		@RequestParam(required = false) Long cursorId,
		@RequestParam(defaultValue = "40") int size
	);

	@Operation(summary = "에셋 좋아요 API", description = "특정 에셋에 좋아요를 누르는 API")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "좋아요 성공"),
		@ApiResponse(responseCode = "409", description = "이미 좋아요를 누른 에셋",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
		@ApiResponse(responseCode = "500", description = "서버 내부 오류",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	})
	CommonResponse<?> like(
		@AuthenticationPrincipal UserDetails userDetails,
		@PathVariable Long assetSellId
	);

	@Operation(summary = "에셋 좋아요 취소 API", description = "특정 에셋의 좋아요를 취소하는 API")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "좋아요 취소 성공"),
		@ApiResponse(responseCode = "404", description = "좋아요를 찾을 수 없음",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
		@ApiResponse(responseCode = "500", description = "서버 내부 오류",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	})
	CommonResponse<?> dislike(
		@AuthenticationPrincipal UserDetails userDetails,
		@PathVariable Long assetSellId
	);
}
