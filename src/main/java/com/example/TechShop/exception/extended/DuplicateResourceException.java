package com.example.TechShop.exception.extended;

public class DuplicateResourceException extends AppException {
    public DuplicateResourceException(String resource, String field, Object value) {
        super(409, String.format("%s đã tồn tại với %s: '%s'", resource, field, value));
    }
}
