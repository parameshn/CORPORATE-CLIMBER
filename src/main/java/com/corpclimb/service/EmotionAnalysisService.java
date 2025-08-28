package com.corpclimb.service;

import com.corpclimb.config.AppConfig;
import com.corpclimb.dto.ConversationUploadDTO;
import com.corpclimb.dto.EmotionAnalysisDTO;
import com.corpclimb.entity.ConversationRecord;
import com.corpclimb.entity.EmotionScore;
import com.corpclimb.entity.Peer;
import com.corpclimb.entity.User;
import com.corpclimb.repository.ConversationRecordRepository;
import com.corpclimb.repository.EmotionScoreRepository;
import com.corpclimb.repository.PeerRepository;
import com.corpclimb.repository.UserRepository;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.language.v1.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.FileInputStream;
import java.io.IOException;
import com.corpclimb.exceptions.EntityNotFoundException;
import com.corpclimb.exceptions.ServiceException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmotionAnalysisService {

    private final ConversationRecordRepository conversationRepository;
    private final EmotionScoreRepository emotionScoreRepository;
    private final UserRepository userRepository;
    private final PeerRepository peerRepository;
    private final AppConfig appConfig;

    @Transactional
    public EmotionAnalysisDTO analyzeConversation(ConversationUploadDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Peer peer = peerRepository.findById(dto.getPeerId())
                .orElseThrow(() -> new EntityNotFoundException("Peer not found"));

        ConversationRecord record = ConversationRecord.builder()
                .user(user)
                .peer(peer)
                .conversationText(dto.getConversationText())
                .conversationDate(dto.getConversationDate())
                .platform(dto.getPlatform())
                .processed(false) 
                .build();

        ConversationRecord savedConversation = conversationRepository.save(record);

        // Analyze emotion using Google Cloud NLP
        EmotionAnalysisDTO emotionAnalysis = performEmotionAnalysis(dto.getConversationText());

        // Save emotion score
        EmotionScore emotionScore = EmotionScore.builder()
                .conversation(savedConversation)
                .sentimentScore(emotionAnalysis.getSentimentScore())
                .emotionCategory(emotionAnalysis.getEmotionCategory())
                .confidenceScore(emotionAnalysis.getConfidenceScore())
                .magnitude(emotionAnalysis.getMagnitude())
                .build();

        emotionScoreRepository.save(emotionScore);

        // Update conversation as processed
        savedConversation.setProcessed(true);
        conversationRepository.save(savedConversation);

        return emotionAnalysis;
    }

    private EmotionAnalysisDTO performEmotionAnalysis(String text) {
        try (LanguageServiceClient language = createLanguageClient()) {
            Document doc = Document.newBuilder()
                    .setContent(text)
                    .setType(Document.Type.PLAIN_TEXT)
                    .build();

            AnalyzeSentimentResponse response = language.analyzeSentiment(doc);
            Sentiment sentiment = response.getDocumentSentiment();

            return EmotionAnalysisDTO.builder()
                    .sentimentScore((double) sentiment.getScore())
                    .magnitude((double) sentiment.getMagnitude())
                    .emotionCategory(classifyEmotion(sentiment.getScore()))
                    .confidenceScore(0.85) // Default confidence
                    .analysisTimestamp(LocalDateTime.now())
                    .build();

        } catch (Exception e) {
            log.error("Error analyzing emotion", e);
            throw new ServiceException("Failed to analyze emotion", e);
        }
    }

    private LanguageServiceClient createLanguageClient() throws IOException {
        GoogleCredentials credentials = GoogleCredentials.fromStream(
                new FileInputStream(appConfig.getGoogleCredentialsPath()));
        LanguageServiceSettings settings = LanguageServiceSettings.newBuilder()
                .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                .build();
        return LanguageServiceClient.create(settings);
    }

    private String classifyEmotion(float score) {
        if (score >= 0.6)
            return "JOY";
        if (score >= 0.2)
            return "SATISFACTION";
        if (score >= -0.2)
            return "NEUTRAL";
        if (score >= -0.6)
            return "FRUSTRATION";
        return "ANGER";
    }
}