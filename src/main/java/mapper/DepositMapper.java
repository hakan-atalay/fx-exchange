package mapper;

import dto.response.DepositResponseDTO;
import entity.Deposit;

public class DepositMapper {

    private DepositMapper() {}

    public static DepositResponseDTO toResponse(Deposit deposit) {
        if (deposit == null) return null;

        return new DepositResponseDTO(
                deposit.getId(),
                deposit.getCurrencyCode(),
                deposit.getAmount(),
                deposit.getMethod(),
                deposit.getStatus(),
                deposit.getCreatedAt()
        );
    }
}
