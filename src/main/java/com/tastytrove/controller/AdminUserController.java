package com.tastytrove.controller;

import com.tastytrove.entity.Product;
import com.tastytrove.payload.request.ProductRequest;
import com.tastytrove.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
public class AdminUserController {

    private ProductRepository productRepository;

    @GetMapping("/")
    public String home(){
        return "Welcome to the homepage";
    }

    @GetMapping("/public/product")
    public ResponseEntity<?> getAllProducts(){
        return ResponseEntity.ok(productRepository.findAll());
    }

    @PostMapping("/admin/saveproduct")
    public ResponseEntity<?> saveProduct(@RequestBody ProductRequest productRequest){
        Product product = Product.builder()
                .name(productRequest.getName())
                .build();

        return ResponseEntity.ok(productRepository.save(product));
    }

    @GetMapping("/user/alone")
    public ResponseEntity<?> userAlone(){
        return ResponseEntity.ok("Users alone can access this ApI only");
    }

    @GetMapping("/adminuser/both")
    public ResponseEntity<?> bothAdminaAndUsersApi(){
        return ResponseEntity.ok("Both Admin and Users Can access the api");
    }

    /** You can use this to get the details(name,email,role,ip, e.t.c) of user accessing the service*/
    @GetMapping("/public/email")
    public String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication); //get all details(name,email,password,roles e.t.c) of the user
        System.out.println(authentication.getDetails()); // get remote ip
        System.out.println(authentication.getName()); //returns the email because the email is the unique identifier
        return authentication.getName(); // returns the email
    }
}
