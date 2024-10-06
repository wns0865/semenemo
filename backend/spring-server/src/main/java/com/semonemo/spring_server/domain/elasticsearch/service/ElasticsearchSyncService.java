package com.semonemo.spring_server.domain.elasticsearch.service;

import java.math.BigInteger;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.semonemo.spring_server.domain.asset.model.AssetImage;
import com.semonemo.spring_server.domain.asset.model.AssetSell;
import com.semonemo.spring_server.domain.asset.model.AssetTag;
import com.semonemo.spring_server.domain.asset.model.Atags;
import com.semonemo.spring_server.domain.asset.repository.assetimage.AssetImageRepository;
import com.semonemo.spring_server.domain.asset.repository.assetsell.AssetSellRepository;
import com.semonemo.spring_server.domain.asset.repository.assettag.AssetTagRepository;
import com.semonemo.spring_server.domain.asset.repository.atags.ATagsRepository;
import com.semonemo.spring_server.domain.elasticsearch.document.AssetSellDocument;
import com.semonemo.spring_server.domain.elasticsearch.document.NFTSellDocument;
import com.semonemo.spring_server.domain.elasticsearch.document.UserDocument;
import com.semonemo.spring_server.domain.elasticsearch.repository.nft.NftSearchRepository;
import com.semonemo.spring_server.domain.elasticsearch.repository.asset.AssetElasticsearchRepository;
import com.semonemo.spring_server.domain.elasticsearch.repository.user.UserSearchRepository;
import com.semonemo.spring_server.domain.nft.entity.NFTMarket;
import com.semonemo.spring_server.domain.nft.entity.NFTTag;
import com.semonemo.spring_server.domain.nft.entity.NFTs;
import com.semonemo.spring_server.domain.nft.entity.Ntags;
import com.semonemo.spring_server.domain.nft.repository.nftmarket.NFTMarketRepository;
import com.semonemo.spring_server.domain.nft.repository.nfttag.NFTTagRepository;
import com.semonemo.spring_server.domain.nft.repository.ntags.NTagRepository;
import com.semonemo.spring_server.domain.user.entity.Users;
import com.semonemo.spring_server.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ElasticsearchSyncService {

	private final AssetImageRepository assetImageRepository;
	private final AssetSellRepository assetSellRepository;
	private final AssetTagRepository assetTagRepository;
	private final ATagsRepository atagsRepository;
	private final AssetElasticsearchRepository assetElasticsearchRepository;

	private final NFTMarketRepository nftMarketRepository;
	private final NFTTagRepository nftTagRepository;
	private final NTagRepository ntagRepository;
	private final NftSearchRepository nftSearchRepository;

	private final UserRepository userRepository;
	private final UserSearchRepository userSearchRepository;

	public void syncAllData() {
		//에셋 업데이트
		List<AssetSell> allAssetSells = assetSellRepository.findAll();
		List<AssetSellDocument> documents = allAssetSells.stream()
			.map(assetSell -> {
				AssetImage assetImage = assetImageRepository.findById(assetSell.getAssetId())
					.orElseThrow(() -> new IllegalArgumentException("Asset Image not found"));
				List<AssetTag> assetTags = assetTagRepository.findByAssetSellId(assetSell.getId());
				List<Atags> tags = assetTags.stream()
					.map(tag -> atagsRepository.findById(tag.getAtagId()).orElse(null))
					.filter(Objects::nonNull)
					.collect(Collectors.toList());
				return convertToDocument(assetImage, assetSell, tags);
			})
			.filter(obj -> true)
			.collect(Collectors.toList());
		assetElasticsearchRepository.saveAll(documents);

		//유저 업데이트
		List<Users> allUsers = userRepository.findAll();
		List<UserDocument> userDocuments = allUsers.stream()
			.map(this::convertToUserDocument)
			.toList();
		userSearchRepository.saveAll(userDocuments);

		List<NFTMarket> allNFTMarkets = nftMarketRepository.findAll();
		List<NFTSellDocument> nftDocuments = allNFTMarkets.stream()
			.map(nftMarket -> {
				NFTs nft = nftMarket.getNftId();
				List<NFTTag> nftTags = nftTagRepository.findByNftId(nft);
				List<Ntags> tags = nftTags.stream()
					.map(NFTTag::getNTagId)
					.collect(Collectors.toList());
				return convertToNftDocument(nft, nftMarket, tags);
			})
			.collect(Collectors.toList());
		nftSearchRepository.saveAll(nftDocuments);
	}

	//에셋 판매 등록시
	public void syncSellAsset(Long assetSellId) {
		AssetSell assetSell = assetSellRepository.findById(assetSellId)
			.orElseThrow(() -> new IllegalArgumentException("Asset Image not found"));
		AssetImage assetImage = assetImageRepository.findById(assetSell.getAssetId())
			.orElseThrow(() -> new IllegalArgumentException("Asset Image not found"));
		List<AssetTag> assetTags = assetTagRepository.findByAssetSellId(assetSell.getId());
		List<Atags> tags = assetTags.stream()
			.map(tag -> atagsRepository.findById(tag.getAtagId()).orElse(null))
			.filter(Objects::nonNull)
			.toList();
		AssetSellDocument document = convertToDocument(assetImage, assetSell, tags);
		assetElasticsearchRepository.save(document);
	}

	//NFT 판매 등록시
	public void syncNFTMarket(NFTs nft, NFTMarket nftMarket, Set<NFTTag> tags) {
		List<Ntags> ntags = tags.stream()
			.map(tag -> ntagRepository.findById(tag.getId()).orElse(null))
			.filter(Objects::nonNull)
			.toList();

		NFTSellDocument document = convertToNftDocument(nft, nftMarket, ntags);
		nftSearchRepository.save(document);
	}

	//유저 회원가입시
	public void syncUser(Users users) {
		userSearchRepository.save(convertToUserDocument(users));
	}

	//에셋 좋아요,조회수,구매수 업데이트
	public void syncData(Long assetSellId, String type) {
		AssetSell assetSell = assetSellRepository.findById(assetSellId)
			.orElseThrow(() -> new IllegalArgumentException("Asset Image not found"));
		assetElasticsearchRepository.updateData(assetSellId, type, assetSell);
	}

	//nft 좋아요 수 업데이트
	public void syncNftData(Long nftSellId, String type) {
		NFTMarket market = nftMarketRepository.findById(nftSellId)
			.orElseThrow(() -> new IllegalArgumentException("NFT Image not found"));
		nftSearchRepository.updateData(nftSellId, type, market);
	}
	//NFT 삭제
	public void deleteNftData(Long marketId) {
		nftSearchRepository.deleteById(marketId);
	}

	//유저 프로필 및 닉네임변경 업데이트
	public void updateUser(Users user) {
		userSearchRepository.updateUser(user);
	}

	// 유저 삭제 업데이트
	public void deleteUser(Users user) {
		userSearchRepository.deleteById(user.getId());
	}

	private AssetSellDocument convertToDocument(AssetImage assetImage, AssetSell assetSell, List<Atags> tags) {
		AssetSellDocument document = new AssetSellDocument();

		document.setAssetSellId(assetSell.getId());
		document.setCreator(assetImage.getCreator());
		document.setImageUrls(assetImage.getImageUrl());
		document.setAssetId(assetImage.getId());
		document.setPrice(assetSell.getPrice());
		document.setHits(assetSell.getHits());
		document.setCreatedAt(assetSell.getCreatedAt());
		document.setLikeCount(assetSell.getLikeCount());
		document.setPurchaseCount(assetSell.getPurchaseCount());

		List<AssetSellDocument.Tag> documentTags = tags.stream()
			.map(tag -> {
				AssetSellDocument.Tag documentTag = new AssetSellDocument.Tag();
				documentTag.setAtagId(tag.getId());
				documentTag.setName(tag.getName());
				return documentTag;
			})
			.collect(Collectors.toList());
		document.setTags(documentTags);

		return document;
	}

	private NFTSellDocument convertToNftDocument(NFTs nft, NFTMarket nftMarket, List<Ntags> tags) {
		NFTSellDocument document = new NFTSellDocument();
		BigInteger bigIntegerTokenId = new BigInteger(String.valueOf(nft.getTokenId()));
		String tokenIdString = bigIntegerTokenId.toString(); // String으로 변환

		document.setNftSellId(nftMarket.getMarketId());
		document.setNftId(nft.getNftId());
		document.setCreator(nft.getCreator().getId());
		document.setSeller(nftMarket.getSeller().getId());
		document.setTokenId(tokenIdString);
		document.setPrice(nftMarket.getPrice());
		document.setCreatedAt(nftMarket.getCreatedAt());
		document.setLikeCount(nftMarket.getLikeCount());

		List<NFTSellDocument.Tag> documentTags = tags.stream()
			.map(tag -> {
				NFTSellDocument.Tag documentTag = new NFTSellDocument.Tag();
				documentTag.setNtagId(tag.getId());
				documentTag.setName(tag.getName());
				return documentTag;
			})
			.collect(Collectors.toList());
		document.setTags(documentTags);

		return document;
	}

	private UserDocument convertToUserDocument(Users user) {
		UserDocument document = new UserDocument();
		document.setId(user.getId());
		document.setNickname(user.getNickname());
		document.setProfileImage(user.getProfileImage());

		return document;
	}
}