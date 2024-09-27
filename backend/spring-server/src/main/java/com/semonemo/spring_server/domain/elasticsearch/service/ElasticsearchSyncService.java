package com.semonemo.spring_server.domain.elasticsearch.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.semonemo.spring_server.domain.elasticsearch.repository.AssetElasticsearchRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ElasticsearchSyncService {

    private final AssetImageRepository assetImageRepository;
    private final AssetSellRepository assetSellRepository;
    private final AssetTagRepository assetTagRepository;
    private final ATagsRepository atagsRepository;
    private final AssetElasticsearchRepository assetElasticsearchRepository;
    private final ElasticsearchIndexChecker indexChecker;

    public void syncAllData() {
        List<AssetSell> allAssetSells = assetSellRepository.findAll();
        List<AssetSellDocument> documents = allAssetSells.stream()
            .map(assetSell -> {
                AssetImage assetImage= assetImageRepository.findById(assetSell.getAssetId())
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
    }
    //판매 등록시
    public void syncSellAsset(Long assetSellId) {
        AssetSell assetSell = assetSellRepository.findById(assetSellId)
            .orElseThrow(() -> new IllegalArgumentException("Asset Image not found"));
        AssetImage assetImage= assetImageRepository.findById(assetSell.getAssetId())
            .orElseThrow(() -> new IllegalArgumentException("Asset Image not found"));
        List<AssetTag> assetTags = assetTagRepository.findByAssetSellId(assetSell.getId());
        List<Atags> tags = assetTags.stream()
            .map(tag -> atagsRepository.findById(tag.getAtagId()).orElse(null))
            .filter(Objects::nonNull)
            .toList();
        AssetSellDocument document= convertToDocument(assetImage, assetSell, tags);
        assetElasticsearchRepository.save(document);
    }
    //좋아요,조회수,구매수 업데이트
    public void syncData(Long assetSellId,String type){
        AssetSell assetSell = assetSellRepository.findById(assetSellId)
            .orElseThrow(() -> new IllegalArgumentException("Asset Image not found"));
        assetElasticsearchRepository.updateData(assetSellId, type, assetSell);
    }
    // //판매물품 클릭시
    // public void synchit(Long assetSellId){
    //     AssetSell assetSell = assetSellRepository.findById(assetSellId)
    //         .orElseThrow(() -> new IllegalArgumentException("Asset Image not found"));
    //     assetElasticsearchRepository.updateHit(assetSellId,assetSell.getHits());
    // }

    // public void syncSingleAsset(Long assetId) {
    //     AssetImage assetImage = assetImageRepository.findById(assetId)
    //         .orElseThrow(() -> new RuntimeException("Asset not found"));
    //     AssetSell assetSell = assetSellRepository.findByAssetId(assetId);
    //     List<AssetTag> assetTags = assetTagRepository.findByAssetSellId(assetSell.getId());
    //     List<Atags> tags = assetTags.stream()
    //         .map(tag -> atagsRepository.findById(tag.getAtagId()).orElse(null))
    //         .filter(Objects::nonNull)
    //         .collect(Collectors.toList());
    //     AssetSellDocument document = convertToDocument(assetImage, assetSell, tags);
    //     assetElasticsearchRepository.save(document);
    // }


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
}