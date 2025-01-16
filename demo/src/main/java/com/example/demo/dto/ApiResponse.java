
package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
@Setter
@Getter
@Schema(description = "Standard API Response")
public class ApiResponse {

    @Schema(description = "Indicates if the request was successful")
    private boolean success;

    @Schema(description = "Error or success message")
    private String message;

    @Schema(description = "Return data")
    private Object data;

    // Constructors, Getters, Setters

    public ApiResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public ApiResponse(boolean success, String message,Object data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

}
