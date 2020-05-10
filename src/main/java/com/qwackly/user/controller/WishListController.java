package com.qwackly.user.controller;

import com.qwackly.user.enums.ResponseStatus;
import com.qwackly.user.exception.QwacklyException;
import com.qwackly.user.model.ProductEntity;
import com.qwackly.user.model.UserEntity;
import com.qwackly.user.model.WishListEntity;
import com.qwackly.user.service.ProductService;
import com.qwackly.user.service.UserService;
import com.qwackly.user.service.WishListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("/v1")
@RestController
public class WishListController {

    @Autowired
    WishListService wishListService;

    @Autowired
    UserService userService;

    @Autowired
    ProductService productService;

    @RequestMapping(value = "/wishList/{userid}", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<List<WishListEntity>> getWishList(@PathVariable Integer userid) {
        List<WishListEntity> wishListEntities;
        UserEntity userEntity=userService.getUserDetails(userid);
        try{
            wishListEntities=wishListService.findAllProductsForUser(userEntity);
        }
        catch (Exception e){
            throw new QwacklyException(e.getMessage(), ResponseStatus.FAILURE);
        }
        return new ResponseEntity<>(wishListEntities, HttpStatus.OK);
    }

    @RequestMapping(value = "/wishList/{userid}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<WishListEntity> addToWishList(@PathVariable Integer userid, @RequestBody Map<String, Object> payload) {
       WishListEntity wishListEntity = new WishListEntity();
       UserEntity userEntity= userService.getUserDetails(userid);
       ProductEntity productEntity=productService.getProduct((Integer) payload.get("productId"));
       wishListEntity.setUserEntity(userEntity);
       wishListEntity.setProductEntity(productEntity);
        try{
            wishListService.addProduct(wishListEntity);
        }
        catch (Exception e){
            throw new QwacklyException(e.getMessage(), ResponseStatus.FAILURE);
        }
        return new ResponseEntity<>(wishListEntity, HttpStatus.OK);
    }

    @RequestMapping(value = "/wishList/{userid}", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<WishListEntity> deleteFromWishList(@PathVariable Integer userid, @RequestBody Map<String, Object> payload) {
        WishListEntity wishListEntity = new WishListEntity();
        UserEntity userEntity= userService.getUserDetails(userid);
        ProductEntity productEntity=productService.getProduct((Integer) payload.get("productId"));
        wishListEntity=wishListService.getProductForAUser(userEntity,productEntity);
        try{
            wishListService.deleteProduct(wishListEntity);
        }
        catch (Exception e){
            throw new QwacklyException(e.getMessage(), ResponseStatus.FAILURE);
        }
        return new ResponseEntity<>(wishListEntity, HttpStatus.OK);
    }
}
