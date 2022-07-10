package antifraud.service;


import antifraud.model.dto.ResultDto;
import antifraud.model.dto.TransactionDto;
import antifraud.model.enums.TransactionStatus;
import antifraud.security.exeption.BadAmountException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

@Component
public class TransactionService {
    private final CardAndIpService cardAndIpService;
    private static final String AMOUNT_IS_INCORRECT = "amount";
    private static final String CARD_IS_STOLEN = "card-number";
    private static final String IP_IS_BLOCKED = "ip";
    private static final String ALL_IS_CORRECT = "none";

    @Autowired
    public TransactionService(CardAndIpService cardAndIpService) {
        this.cardAndIpService = cardAndIpService;
    }

    public ResultDto resolve(TransactionDto transactionDto) {

        TransactionStatus status = retrieveTransactionStatus(transactionDto.getAmount());

        Set<String> info = new TreeSet<>();
        if (status != TransactionStatus.ALLOWED) {
            info.add(AMOUNT_IS_INCORRECT);
        }

        if (cardAndIpService.isCardStolen(transactionDto.getNumber())) {
            clearInfoIfThereMoreSevereStatus(status, info);
            status = TransactionStatus.PROHIBITED;
            info.add(CARD_IS_STOLEN);
        }

        if (cardAndIpService.isIpBanned(transactionDto.getIp())) {
            clearInfoIfThereMoreSevereStatus(status, info);
            status = TransactionStatus.PROHIBITED;
            info.add(IP_IS_BLOCKED);
        }

        if (status == TransactionStatus.ALLOWED) {
            info.add(ALL_IS_CORRECT);
        }
        return new ResultDto(status.name(),
                             String.join(", ", info));
    }

    private void clearInfoIfThereMoreSevereStatus(TransactionStatus status,
                                                  Set<String> info) {
        if (status == TransactionStatus.MANUAL_PROCESSING) {
            info.clear();
        }
    }

    public TransactionStatus retrieveTransactionStatus(long amount) {
        return Arrays
                .stream(TransactionStatus.values())
                .filter(val -> Math.max(val.getMin(), amount) ==
                               Math.min(amount, val.getMax()))
                .findFirst()
                .orElseThrow(BadAmountException::new);
    }
}
