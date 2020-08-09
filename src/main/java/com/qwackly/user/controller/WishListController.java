package com.qwackly.user.controller;

import com.qwackly.user.enums.ResponseStatus;
import com.qwackly.user.exception.QwacklyException;
import com.qwackly.user.model.ProductEntity;
import com.qwackly.user.model.UserEntity;
import com.qwackly.user.model.WishListEntity;
import com.qwackly.user.security.CurrentUser;
import com.qwackly.user.security.UserPrincipal;
import com.qwackly.user.service.ProductService;
import com.qwackly.user.service.UserService;
import com.qwackly.user.service.WishListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RequestMapping("/v1")
@RestController
public class WishListController {

    @Autowired
    WishListService wishListService;

    @Autowired
    UserService userService;

    @Autowired
    ProductService productService;

    private static final String NOT_FOUND="Not Found";

    private static final String ADDED = "ADDED";

    private static Map<String,String> response= new HashMap<>();

    @RequestMapping(value = "/wishList", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<List<ProductEntity>> getWishList(@CurrentUser UserPrincipal userPrincipal) {
        List<WishListEntity> wishListEntities;
        List<ProductEntity> productEntities;
        UserEntity userEntity=userService.getUserDetails(userPrincipal.getId());
        try{
            wishListEntities=wishListService.findAllProductsForUser(userEntity);
            productEntities=wishListEntities.stream().map(wishListEntity -> wishListEntity.getProductEntity()).collect(Collectors.toList());
        }
        catch (Exception e){
            throw new QwacklyException(e.getMessage(), ResponseStatus.FAILURE);
        }
        return new ResponseEntity<>(productEntities, HttpStatus.OK);
    }

    @RequestMapping(value = "/wishList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<Map> addToWishList( @RequestBody Map<String, Object> payload, @CurrentUser UserPrincipal userPrincipal) {
       WishListEntity wishListEntity = new WishListEntity();
        Integer userid = userPrincipal.getId();
        UserEntity userEntity= userService.getUserDetails(userid);
       ProductEntity productEntity=productService.getProduct((Integer) payload.get("productId"));
        if(Objects.isNull(userEntity) || Objects.isNull(productEntity)){
            throw new QwacklyException(NOT_FOUND, ResponseStatus.FAILURE);
        }
       wishListEntity.setUserEntity(userEntity);
       wishListEntity.setProductEntity(productEntity);
       wishListEntity.setStatus(ADDED);
        try{
            wishListService.addProduct(wishListEntity);
        }
        catch (Exception e){
            throw new QwacklyException(e.getMessage(), ResponseStatus.FAILURE);
        }
        response.put("status","SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/wishList", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<Map> deleteFromWishList(@RequestBody Map<String, Object> payload, @CurrentUser UserPrincipal userPrincipal) {
        WishListEntity wishListEntity;
        Integer userid = userPrincipal.getId();
        UserEntity userEntity= userService.getUserDetails(userid);
        ProductEntity productEntity=productService.getProduct((Integer) payload.get("productId"));
        if(Objects.isNull(userEntity) || Objects.isNull(productEntity)){
            throw new QwacklyException(NOT_FOUND, ResponseStatus.FAILURE);
        }
        wishListEntity=wishListService.getProductForAUser(userEntity,productEntity);
        if(Objects.isNull(wishListEntity)){
            throw new QwacklyException(NOT_FOUND, ResponseStatus.FAILURE);
        }
        try{
            wishListService.deleteProduct(wishListEntity);
        }
        catch (Exception e){
            throw new QwacklyException(e.getMessage(), ResponseStatus.FAILURE);
        }
        response.put("status","SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
