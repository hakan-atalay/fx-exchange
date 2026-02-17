package mapper;

import dto.response.TransactionResponseDTO;
import entity.Transaction;

public class TransactionMapper {

    private TransactionMapper() {}

    public static TransactionResponseDTO toResponse(Transaction t) {
        if (t == null) return null;

        return new TransactionResponseDTO(
                t.getId(),
                t.getFromCurrencyCode(),
                t.getToCurrencyCode(),
                t.getAmountFrom(),
                t.getAmountTo(),
                t.getExchangeRate(),
                t.getTransactionType(),
                t.getStatus(),
                t.getCreatedAt()
        );
    }
}
