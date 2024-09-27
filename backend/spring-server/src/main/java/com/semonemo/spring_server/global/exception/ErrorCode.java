package com.semonemo.spring_server.global.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
	// Server
	INTERNAL_SERVER_ERROR("S001", "서버 내부 오류", HttpStatus.INTERNAL_SERVER_ERROR),
	RESOURCE_NOT_FOUND_ERROR("S002", "요청한 리소스를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
	BAD_REQUEST_ERROR("S003", "잘못된 요청입니다.", HttpStatus.BAD_REQUEST),
	METHOD_NOT_ALLOWED_ERROR("S004", "허용되지 않은 HTTP 메서드입니다.", HttpStatus.METHOD_NOT_ALLOWED),
	INVALID_CREDENTIALS_ERROR("S005", "잘못된 인증 정보입니다.", HttpStatus.UNAUTHORIZED),
	INSUFFICIENT_AUTHENTICATION_ERROR("S006","인증 정보가 부족합니다.", HttpStatus.UNAUTHORIZED),

	// Auth
	INVALID_USER_DATA_ERROR("AU001", "유효하지 않은 값이 입력되었습니다.", HttpStatus.BAD_REQUEST),
	INVALID_NICKNAME_ERROR("AU002", "유효하지 않은 닉네임 형식입니다.", HttpStatus.BAD_REQUEST),
	AUTHENTICATION_FAIL_ERROR("AU003", "사용자 인증에 실패했습니다.", HttpStatus.UNAUTHORIZED),
	EXPIRED_TOKEN_ERROR("AU004", "만료된 토큰입니다.", HttpStatus.UNAUTHORIZED),
	INVALID_TOKEN_ERROR("AU005", "유효하지 않은 토큰입니다.", HttpStatus.UNAUTHORIZED),
	EXISTS_ADDRESS_ERROR("AU006", "이미 존재하는 주소입니다.", HttpStatus.BAD_REQUEST),

	// User
	USER_NOT_FOUND_ERROR("U001", "사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
	SELF_FOLLOW_ERROR("U002", "자기 자신은 팔로우할 수 없습니다.", HttpStatus.BAD_REQUEST),
	ALREADY_FOLLOW_ERROR("U003", "이미 팔로우한 사용자입니다.", HttpStatus.BAD_REQUEST),
	NOT_FOLLOW_ERROR("U004", "팔로우 목록에 없는 사용자입니다.", HttpStatus.NOT_FOUND),
	CHECK_SELF_FOLLOW_ERROR("U005", "자기 자신은 확인할 수 없습니다.", HttpStatus.BAD_REQUEST),
	;

	private final String code;
	private final String message;
	private final HttpStatus httpStatus;
}
