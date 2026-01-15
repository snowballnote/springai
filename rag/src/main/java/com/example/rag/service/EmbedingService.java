package com.example.rag.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class EmbedingService {

	private final VectorStore vectorStore;
	public EmbedingService(VectorStore vectorStore) {
		this.vectorStore = vectorStore; // 벡터데이터 쓰기
	}
	
	public void porcessUploadPdf(MultipartFile file) throws IOException {
		//1. 사용자가 업로드한 PDF파일을 임시 파일로 생성
		File tmpFile = File.createTempFile("uploaded", ".pdf"); // uploaded.pdf 빈파일
		file.transferTo(tmpFile); // MultipartFile -> uploaded.pdf
		
		// uploaded.pdf 불러오기
		Resource fileResource = new FileSystemResource(tmpFile);
		
		try {
			PdfDocumentReaderConfig config = PdfDocumentReaderConfig.builder()
					.withPageTopMargin(0) // PDF 페이지 상단에 여백을 설정
					.withPageExtractedTextFormatter( //페이지에서 추출된 텍스를 포매칭 방식 지정
							ExtractedTextFormatter.builder()
								.withNumberOfBottomTextLinesToDelete(0) //페이지 상단에서 삭제할 텍스트 줄수
								.build())
					.withPagesPerDocument(1)// 한번에 처리할 수 있는 페이지를 지정
					.build();
			
			PagePdfDocumentReader pdfReader = new PagePdfDocumentReader(fileResource, config);
			
			List<Document> documents = pdfReader.get(); // 
			
			/*
			 * 문서를 분할하기 위한 TokenTextSpliter 
			 */
			TokenTextSplitter splitter = new TokenTextSplitter(1000, 400, 10, 5000, true);
			List<Document> splitDocumets = splitter.apply(documents);
			
			// PGvector Store에 저장 -> vector_store 테이블에 메타정보 + 임베딩된 정보(벡터정보)
			vectorStore.accept(splitDocumets); 
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			tmpFile.delete();
		}
	}
}