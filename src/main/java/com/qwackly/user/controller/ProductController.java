package com.qwackly.user.controller;

import com.qwackly.user.enums.ResponseStatus;
import com.qwackly.user.exception.QwacklyException;
import com.qwackly.user.model.OrderEntity;
import com.qwackly.user.model.ProductEntity;
import com.qwackly.user.model.UserEntity;
import com.qwackly.user.model.WishListEntity;
import com.qwackly.user.security.CurrentUser;
import com.qwackly.user.security.UserPrincipal;
import com.qwackly.user.service.ProductService;
import com.qwackly.user.service.UserService;
import com.qwackly.user.service.WishListService;
import com.qwackly.user.util.WishListProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RequestMapping("/v1")
@RestController
public class ProductController {

    public static final String PRODUCT_NOT_FOUND="Product Not Found";

    @Autowired
    ProductService productService;


    @Autowired
    WishListService wishListService;

    @Autowired
    UserService userService;

    public static final String ADDED = "ADDED";

    List<ProductEntity> productEntities ;

    @RequestMapping(value = "/products", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<List<ProductEntity>> getProducts(){
        try {
            productEntities=productService.getAllAvailableProducts();
        }
        catch (Exception e){
            throw new QwacklyException(e.getMessage(), ResponseStatus.FAILURE);
        }
        return new ResponseEntity<>(productEntities, HttpStatus.OK);
    }

    @RequestMapping(value = "/products/users", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<List<WishListProduct>> getProductsforAUser(@CurrentUser UserPrincipal userPrincipal){
        List<WishListProduct> wishListProducts = new ArrayList<>();
        try {
            productEntities=productService.getAllAvailableProducts();

            for (ProductEntity productEntity: productEntities){
                WishListProduct wishListProduct = new WishListProduct();
                Integer userId = userPrincipal.getId();
                UserEntity userEntity= userService.getUserDetails(userId);
                boolean isWished = false;
                if (wishListService.ifProductIsWishedByUser(productEntity,userEntity)){
                    isWished=true;
                }
                wishListProduct.setWished(isWished);
                wishListProduct.setProductEntity(productEntity);
                wishListProducts.add(wishListProduct);
            }
        }
        catch (Exception e){
            throw new QwacklyException(e.getMessage(), ResponseStatus.FAILURE);
        }
        return new ResponseEntity<>(wishListProducts, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/products", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<ProductEntity> addProduct(@RequestBody ProductEntity input){
        try {
            productService.addProduct(input);
        }
        catch (Exception e){
            throw new QwacklyException(e.getMessage(), ResponseStatus.FAILURE);
        }
        return new ResponseEntity<>(productService.getProduct(input.getId()), HttpStatus.OK);
    }

    @RequestMapping(value = "/products/{id}", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<ProductEntity> getProduct(@PathVariable Integer id){
        ProductEntity productEntity;
        try {
            productEntity= productService.getProduct(id);
        }
        catch (Exception e){
            throw new QwacklyException(e.getMessage(), ResponseStatus.FAILURE);
        }
        return new ResponseEntity<>(productEntity, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/products", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<ProductEntity> modifyProduct(@RequestBody ProductEntity input){
        try {
            if (Objects.nonNull(productService.getProduct(input.getId())))
                productService.addProduct(input);
            else
                throw new QwacklyException(PRODUCT_NOT_FOUND, ResponseStatus.FAILURE);
        }
        catch (Exception e){
            throw new QwacklyException(e.getMessage(), ResponseStatus.FAILURE);
        }
        return new ResponseEntity<>(productService.getProduct(input.getId()), HttpStatus.OK);
    }
}
