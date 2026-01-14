package com.example.openai0113.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChatMessageResponse {
	private String who; // 나, AI
	private String message;
}
