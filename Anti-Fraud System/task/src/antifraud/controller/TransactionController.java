package antifraud.controller;

import antifraud.model.Transaction;
import antifraud.model.enums.TypeOfTransaction;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import java.util.Map;


@RestController
public class TransactionController {

    @PostMapping("/api/antifraud/transaction")
    public Map<String, String> analyzeTrans(@RequestBody Transaction transaction) {

        if (transaction.getAmount() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        if (transaction.getAmount() <= 200) {
            return Map.of("result",
                    TypeOfTransaction.ALLOWED.name());
        } else if (transaction.getAmount() > 200 &&
                transaction.getAmount() <= 1500) {
            return Map.of("result",
                    TypeOfTransaction.MANUAL_PROCESSING.name());
        } else {
            return Map.of("result",
                    TypeOfTransaction.PROHIBITED.name());
        }
    }
}