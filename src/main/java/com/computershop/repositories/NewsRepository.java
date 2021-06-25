package com.computershop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.computershop.dao.News;

@Repository
public interface NewsRepository extends JpaRepository<News, Long>{

}
