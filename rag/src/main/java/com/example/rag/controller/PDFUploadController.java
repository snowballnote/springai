package com.example.rag.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.rag.service.EmbedingService;

// PDF 파일 업로드를 위한 테스트용 컨트롤러
@RestController
public class PDFUploadController {
	
	@Autowired private EmbedingService embedingService;
	
	@PostMapping("/upload")
	public ResponseEntity<String> uploadPdf(@RequestParam("file") MultipartFile file){
		try {
			embedingService.porcessUploadPdf(file);
			return ResponseEntity.ok("PDF 업로드 및 임베딩 완료");
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("처리 중 오류 발생 :" + e.getMessage());
		}
	}
}