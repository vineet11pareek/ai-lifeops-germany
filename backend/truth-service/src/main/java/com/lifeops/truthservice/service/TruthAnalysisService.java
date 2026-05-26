package com.lifeops.truthservice.service;

import com.lifeops.truthservice.dto.AnalyzeTruthRequest;
import com.lifeops.truthservice.dto.CreateTruthAnalysisRequest;
import com.lifeops.truthservice.dto.TruthAnalysisResponse;
import com.lifeops.truthservice.dto.TruthAnalysisResult;
import com.lifeops.truthservice.entity.RiskLevel;
import com.lifeops.truthservice.entity.TruthAnalysis;
import com.lifeops.truthservice.exception.TruthAnalysisNotFoundException;
import com.lifeops.truthservice.repository.TruthAnalysisRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class TruthAnalysisService {

    private final static Logger log = LoggerFactory.getLogger(TruthAnalysisService.class);

    private final TruthAnalysisRepository truthAnalysisRepository;
    private final TruthAnalysisMapper truthAnalysisMapper;
    private final AiServiceClient aiServiceClient;

    public TruthAnalysisService(TruthAnalysisRepository truthAnalysisRepository, TruthAnalysisMapper truthAnalysisMapper, AiServiceClient aiServiceClient) {
        this.truthAnalysisRepository = truthAnalysisRepository;
        this.truthAnalysisMapper = truthAnalysisMapper;
        this.aiServiceClient = aiServiceClient;
    }

    @Transactional
    public TruthAnalysisResponse createTruthAnalysis(CreateTruthAnalysisRequest request) {
        log.info("Creating truth analysis request title={}", request.title());

        TruthAnalysis truthAnalysis = new TruthAnalysis(
                null,
                request.title(),
                request.content()
        );

        TruthAnalysis savedAnalysis = truthAnalysisRepository.save(truthAnalysis);

        log.info("Truth analysis request created analysisId={}", savedAnalysis.getId());

        return truthAnalysisMapper.toResponse(savedAnalysis);
    }

    @Transactional(readOnly = true)
    public List<TruthAnalysisResponse> getRecentAnalyses() {
        return truthAnalysisRepository.findTop20ByOrderByCreatedAtDesc()
                .stream()
                .map(truthAnalysisMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public TruthAnalysisResponse getAnalysisById(UUID id) {
        TruthAnalysis truthAnalysis = truthAnalysisRepository.findById(id)
                .orElseThrow(() -> new TruthAnalysisNotFoundException(id));

        return truthAnalysisMapper.toResponse(truthAnalysis);
    }


    @Transactional
    public TruthAnalysisResponse analyzeTruth(AnalyzeTruthRequest request) {
        log.info("Creating truth analysis for AI processing title={}", request.title());

        TruthAnalysis truthAnalysis = new TruthAnalysis(
                null,
                request.title(),
                request.content()
        );

        truthAnalysis.markAnalyzing();
        TruthAnalysis savedAnalysis = truthAnalysisRepository.save(truthAnalysis);

        try {
            TruthAnalysisResult result = aiServiceClient.analyzeTruth(
                    savedAnalysis.getTitle(),
                    savedAnalysis.getContent()
            );

            RiskLevel riskLevel = parseRiskLevel(result.riskLevel());

            savedAnalysis.markAnalyzed(
                    result.claimSummary(),
                    result.trustScore(),
                    riskLevel,
                    result.explanation(),
                    result.suggestedVerificationSteps()
            );

            TruthAnalysis analyzed = truthAnalysisRepository.save(savedAnalysis);

            log.info("Truth analysis completed successfully analysisId={}", analyzed.getId());

            return truthAnalysisMapper.toResponse(analyzed);
        } catch (Exception exception) {
            savedAnalysis.markFailed();
            truthAnalysisRepository.save(savedAnalysis);
            throw exception;
        }
    }

    private RiskLevel parseRiskLevel(String value) {
        try {
            return RiskLevel.valueOf(value);
        } catch (Exception exception) {
            return RiskLevel.UNKNOWN;
        }
    }


}
