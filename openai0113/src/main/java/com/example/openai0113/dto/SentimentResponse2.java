package com.example.openai0113.dto;

import lombok.Data;

@Data
public class SentimentResponse2 {
	private Sentiment myAge;
	private String sentiment;
	private String reason;
	
	enum Sentiment{
		POSITIVE, NEUTRAL, NEGATIVE
	}
}
