package com.computershop.controllers;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.computershop.dao.News;
import com.computershop.dto.NewsDTO;
import com.computershop.exceptions.NotFoundException;
import com.computershop.repositories.NewsRepository;
import com.computershop.services.CloudinaryService;

@RestController
@RequestMapping(value = "/api/news")
public class NewsController {
	@Autowired
	private NewsRepository newsRepository;
	
	@Autowired
	private CloudinaryService cloudinaryService;
	
	
	@GetMapping
	public ResponseEntity<?> getAllNews(@RequestParam(name = "page", required = false) Integer pageNum) {
		if (pageNum != null) {
			Page<News> page = newsRepository.findAll(PageRequest.of(pageNum.intValue(), 10));
			if (page.getNumberOfElements() == 0) {
				return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
			}
			return ResponseEntity.ok().body(page.getContent());
		}

		List<News> news = newsRepository.findAll();
		if (news.size() == 0) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok().body(news);
	}
	
	@GetMapping("/{newsId}")
    public ResponseEntity<?> getNewsById(@PathVariable("newsId") Long newsId) {
        Optional<News> news = newsRepository.findById(newsId);
        if (!news.isPresent()) {
            throw new NotFoundException("News not found");
        }
        return ResponseEntity.ok().body(news);
    }
	
	@PostMapping("/with-link")
    @PreAuthorize("@authorizeService.authorizeAdmin(authentication, 'ADMIN')")
    public ResponseEntity<?> createNews(@RequestBody NewsDTO newsDTO) {
		News news = new News();
		news.setContent(newsDTO.getContent());
		news.setTitle(newsDTO.getTitle());
		news.setImageLink(newsDTO.getImageLink());
		News saveNews = newsRepository.save(news);
		return ResponseEntity.status(HttpStatus.CREATED).body(saveNews);
    }
	
	@PostMapping("/with-image")
    @Transactional(rollbackFor = Exception.class)
    @PreAuthorize("@authorizeService.authorizeAdmin(authentication, 'ADMIN')")
    public ResponseEntity<?> createNewsWithImage(@RequestBody NewsDTO newsDTO,
                                             @RequestParam("file") MultipartFile file) throws IOException {
		News news = new News();
		news.setContent(newsDTO.getContent());
		news.setTitle(newsDTO.getTitle());
		Map<?, ?> uploadResult = cloudinaryService.uploadFile(file);
		news.setImageLink(uploadResult.get("url").toString());
		News saveNews = newsRepository.save(news);
 
        return ResponseEntity.status(201).body(saveNews);
    }
}
