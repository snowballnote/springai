package com.example.rag.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RagController {

    // LLM 호출용 클라이언트
    private final ChatClient chatClient;
    // pgvector 기반 벡터 검색용 객체
    private final VectorStore vectorStore;
    
    public RagController(ChatClient.Builder builder, VectorStore vectorStore) {
        this.chatClient = builder.build();
        this.vectorStore = vectorStore;
    }

    @PostMapping("/rag")
    public String ask(@RequestBody String question) {
 
    	// System.out.println(question);
		/*
			vector_store는 자동으로 AI에게 전달되지 않는다
			반드시 similaritySearch() 결과를 프롬프트에 직접 포함
			UserMessage에 참고한 문서내용과 질문을 추가
		*/
    	
        // 1) 질문과 가장 유사한 문서들을 vector DB에서 검색
        List<Document> docs = vectorStore.similaritySearch(
            SearchRequest.builder()
                .query(question)
                .topK(3)                  // 상위 3개 문서만 사용
                .similarityThreshold(0.75) // 유사도 기준
                .build()
        );

        // 2) 검색된 문서들의 내용을 하나의 문자열로 합침
        String context = docs.stream()
                .map(doc -> doc.getText())
                .collect(Collectors.joining("\n\n"));
        System.out.println("context: " + context);
        // 3) 시스템 메시지 (AI의 역할과 규칙 정의)
        SystemMessage systemMessage = new SystemMessage(
            "너는 문서 기반 AI 어시스턴트다. 아래 문서 내용을 참고해서 질문에 답해라.문서에 없는 내용은 모른다고 말해라."
        );

        // 4) 사용자 메시지 (문서 내용 + 실제 질문)
        UserMessage userMessage =
            new UserMessage("[문서내용]" + context + "[질문]" + question);

        // 5) 메시지를 하나의 프롬프트로 구성
        List<Message> messages = new ArrayList<>();
        messages.add(systemMessage);
        messages.add(userMessage);

        Prompt prompt = new Prompt(messages);

        // 6) LLM 호출 후 결과 반환
        return chatClient.prompt(prompt)
                .call()
                .content();
    }
}