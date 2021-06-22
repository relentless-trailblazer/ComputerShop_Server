package com.computershop.dto.productsDetail;

import java.util.List;

import com.computershop.dao.ProductImage;
import com.computershop.dao.product.GraphicCard;

public class GraphicCardDetail {
	private GraphicCard graphicCard;
	private List<ProductImage> productImages;

	public GraphicCard getGraphicCard() {
		return graphicCard;
	}

	public void setGraphicCard(GraphicCard graphicCard) {
		this.graphicCard = graphicCard;
	}

	public List<ProductImage> getProductImages() {
		return productImages;
	}

	public void setProductImages(List<ProductImage> productImages) {
		this.productImages = productImages;
	}

	public GraphicCardDetail(GraphicCard graphicCard, List<ProductImage> productImages) {
		super();
		this.graphicCard = graphicCard;
		this.productImages = productImages;
	}

	public GraphicCardDetail() {
		super();
	}

}
