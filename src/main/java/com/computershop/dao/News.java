package com.computershop.dao;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Nationalized;
import org.hibernate.validator.constraints.Length;

@Table(name = "news")
@Entity
public class News {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "order_item_id")
	private Long id;
	
	@Column(name = "title")
	@Nationalized
	@Length(max = 5000)
	private String title;
	
	@Column(name = "content")
	@Nationalized
	@Length(max = 300000)
	private String content;
	
	@Column(name = "imageLink")
	private String imageLink;
	
	@CreationTimestamp
	private Timestamp createAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Timestamp getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Timestamp createAt) {
		this.createAt = createAt;
	}

	
	public News(Long id, @Length(max = 5000) String title, @Length(max = 300000) String content, String imageLink,
			Timestamp createAt) {
		super();
		this.id = id;
		this.title = title;
		this.content = content;
		this.imageLink = imageLink;
		this.createAt = createAt;
	}

	public String getImageLink() {
		return imageLink;
	}

	public void setImageLink(String imageLink) {
		this.imageLink = imageLink;
	}

	public News() {
		super();
	}
	
	
}
