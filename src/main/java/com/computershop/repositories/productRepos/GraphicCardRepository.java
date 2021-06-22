package com.computershop.repositories.productRepos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.computershop.dao.product.GraphicCard;

@Repository
public interface GraphicCardRepository extends JpaRepository<GraphicCard, Long>{

	List<GraphicCard> findByNameContainingIgnoreCase(String searchConvert);

	Optional<GraphicCard> findByGraphicCardId(Long graphicCardId);
	
	void deleteByGraphicCardId(Long graphicCardId);

}
