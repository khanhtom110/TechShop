package com.example.TechShop.security;

import com.example.TechShop.exception.extended.AppException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {
    public static Long getCurrentUserId(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication==null){
            throw new AppException(401,"Token chưa được xác thực");
        }

        Object principal= authentication.getPrincipal();
        if(principal!=null){
            return ((CustomUserDetails) principal).getUser().getId();
        }

        throw new AppException(401,"Token chưa được xác thực");
    }
}
