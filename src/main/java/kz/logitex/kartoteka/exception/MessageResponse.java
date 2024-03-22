package kz.logitex.kartoteka.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@AllArgsConstructor
@Data
public class MessageResponse {
    private int statusCode;
    private Date timestamp;
    private String message;
    private String description;
}
