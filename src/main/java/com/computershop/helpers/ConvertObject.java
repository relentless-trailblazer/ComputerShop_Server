package com.computershop.helpers;

import com.computershop.dao.Manufacture;
import com.computershop.dao.Product;
import com.computershop.dao.User;
import com.computershop.dto.ManufactureDTO;
import com.computershop.dto.ProductDTO;
import com.computershop.dto.SignUpDTO;
import com.computershop.dto.UserDTO;

public class ConvertObject {

	public static String toSlug(String input) {
		return input.toLowerCase().replaceAll("[á|à|ả|ạ|ã|ă|ắ|ằ|ẳ|ẵ|ặ|â|ấ|ầ|ẩ|ẫ|ậ|ä|å|æ|ą]", "a")
				.replaceAll("[ó|ò|ỏ|õ|ọ|ô|ố|ồ|ổ|ỗ|ộ|ơ|ớ|ờ|ở|ỡ|ợ|ö|ô|œ|ø]", "o")
				.replaceAll("[é|è|ẻ|ẽ|ẹ|ê|ế|ề|ể|ễ|ệ|ě|ė|ë|ę]", "e").replaceAll("[ú|ù|ủ|ũ|ụ|ư|ứ|ừ|ử|ữ|ự]", "u")
				.replaceAll("[i|í|ì|ỉ|ĩ|ị|ï|î|į]", "i").replaceAll("[ù|ú|ü|û|ǘ|ů|ű|ū|ų]", "u")
				.replaceAll("[ß|ş|ś|š|ș]", "s").replaceAll("[ź|ž|ż]", "z").replaceAll("[ý|ỳ|ỷ|ỹ|ỵ|ÿ|ý]", "y")
				.replaceAll("[ǹ|ń|ň|ñ]", "n").replaceAll("[ç|ć|č]", "c").replaceAll("[ğ|ǵ]", "g")
				.replaceAll("[ŕ|ř]", "r").replaceAll("[·|/|_|,|:|;]", "-").replaceAll("[ť|ț]", "t").replaceAll("ḧ", "h")
				.replaceAll("ẍ", "x").replaceAll("ẃ", "w").replaceAll("ḿ", "m").replaceAll("ṕ", "p")
				.replaceAll("ł", "l").replaceAll("đ", "d").replaceAll("\\s+", "-").replaceAll("&", "-and-")
				.replaceAll("[^\\w\\-]+", "").replaceAll("\\-\\-+", "-").replaceAll("^-+", "").replaceAll("-+$", "");
	}
    public static User fromSignUpDTOToUserDAO(SignUpDTO signUpDTO) {
        if (!Validate.checkSignUp(signUpDTO)) {
            return null;
        }
        User user = new User();
        user.setFirstName(signUpDTO.getFirstName().trim().replaceAll("\\s+", " "));
        user.setLastName(signUpDTO.getLastName().trim().replaceAll("\\s+", " "));
        user.setAddress(signUpDTO.getAddress().trim().replaceAll("\\s+", " "));
        user.setUsername(signUpDTO.getUsername());
        user.setRole("MEMBER");
        user.setEmail(signUpDTO.getEmail());
        user.setPhone(signUpDTO.getPhone());
        return user;
    }
    

    public static User fromUserDTOToUserDAO(UserDTO userDTO) {
        User user = new User();
        user.setFirstName(userDTO.getFirstName().trim().replaceAll("\\s+", " "));
        user.setLastName(userDTO.getLastName().trim().replaceAll("\\s+", " "));
        user.setUsername(userDTO.getUsername());
        user.setAddress(userDTO.getAddress().trim().replaceAll("\\s+", " "));
        user.setEmail(userDTO.getEmail());
        user.setPhone(userDTO.getPhone());
        user.setRole(userDTO.getRole());
        return user;
    }
    
    public static Manufacture fromManufactureDTOToDAO(ManufactureDTO manufactureDTO) {
		Manufacture manufacture = new Manufacture();
		manufacture.setName(manufactureDTO.getName());
		manufacture.setNation(manufactureDTO.getNation());
		return manufacture;
    }
    
    
    
    public static String fromSlugToString(String slug) {
    	return slug.replaceAll("-", " ").replaceAll("%20", " ");
    	
    }
    
    public static Product fromProductDTOToProductDAO(ProductDTO productDTO) {
    	Product newProduct = new Product();
    	newProduct.setName(productDTO.getName().trim().replaceAll("\\s+", " "));
    	newProduct.setBrand(productDTO.getBrand().trim().replaceAll("\\s+", " "));
    	newProduct.setDescription(productDTO.getDescription().trim());
    	newProduct.setPrice(productDTO.getPrice());
    	newProduct.setSaleOff(productDTO.getSaleOff());
    	newProduct.setAmount(productDTO.getAmount());
    	newProduct.setWarranty(productDTO.getWarranty());
    	
    	return newProduct;
    }
}

