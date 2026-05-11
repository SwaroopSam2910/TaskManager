package com.example.product_backend.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class apiResponse <T>{

    private boolean sucess;
    private String message;
    private T data;

    public static <T> apiResponse<T> sucess(String message, T data){
        return new apiResponse<>(true,message,data);
    }

    public static <T> apiResponse<T> success(String message) {
        return new apiResponse<>(true, message, null);
    }

    // Failure
    public static <T> apiResponse<T> error(String message) {
        return new apiResponse<>(false, message, null);
    }

}
