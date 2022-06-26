package antifraud.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class TransactionController {
    public static final String AMOUNT = "amount";
    public static final String RESULT = "result";

    @PostMapping("/api/antifraud/transaction")
    public ResponseEntity<Object> validateTransaction(@RequestBody Map<String, Long> amount) {
        if (amount.get(AMOUNT) == null || amount.get(AMOUNT) <= 0) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        Long transactionAmount = amount.get(AMOUNT);

        if (transactionAmount <= 200) {
            return new ResponseEntity<>(Map.of(RESULT, "ALLOWED"),
                    HttpStatus.OK);
        }
        if (transactionAmount <= 1500) {
            return new ResponseEntity<>(Map.of(RESULT, "MANUAL_PROCESSING"),
                    HttpStatus.OK);
        }
        return new ResponseEntity<>(Map.of(RESULT, "PROHIBITED"),
                HttpStatus.OK);
    }
}