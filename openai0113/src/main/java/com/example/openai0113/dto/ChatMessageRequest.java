package com.example.openai0113.dto;

import lombok.Data;

@Data
public class ChatMessageRequest {
	private String userId; // 질문을 하는 사용자 ID
	private String userMessage;
}
