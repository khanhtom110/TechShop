package com.example.TechShop.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record OrderRequest(
        @NotBlank(message = "Tên không được để trống")
        @Size(min = 2, max = 100, message = "Tên phải từ 2-100 ký tự")
        String customerName,

        @NotBlank(message = "SĐT không được để trống")
        @Pattern(regexp = "^0[0-9]{9}$", message = "Số điện thoại phải có 10 chữ số và bắt đầu bằng 0")
        String customerPhone,

        @NotBlank(message = "Email không được để trống")
        @Size(max = 100, message = "Email không quá 100 ký tự")
        @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Email không đúng định dạng")
        String customerEmail,

        @NotBlank(message = "Địa chỉ không được để trống")
        @Size(max = 500,message = "Địa chỉ không quá 500 ký tự")
        String customerAddress,

        @Size(max = 1000, message = "Mô tả không quá 1000 ký tự")
        String description
) {
}
