package com.spring.client.product.dao;

import java.util.List;

import com.spring.admin.image.vo.AdminImageVO;
import com.spring.client.product.vo.ProductVO;

public interface ProductDao {
   public List<ProductVO> productList(ProductVO pvo);
   
   
   
   public ProductVO productDetail(ProductVO pvo);
   
   public List<AdminImageVO> productDetailImage(ProductVO pvo);
   
   public ProductVO productMain(ProductVO pvo);
   
   
   public int productUpdate(ProductVO pvo);
   
   public int productDelete(ProductVO pvo);
   
   public int productInsert(ProductVO pvo);
   
   public List<ProductVO> bcategory();
      
   public List<ProductVO> scategory();

   public List<ProductVO> productThumbnail(ProductVO pvo);
   
    public int inventoryCount(ProductVO pvo);
   
   
   

   
   
}