package mapper;

import dto.response.WithdrawalResponseDTO;
import entity.Withdrawal;

public class WithdrawalMapper {

    private WithdrawalMapper() {}

    public static WithdrawalResponseDTO toResponse(Withdrawal withdrawal) {
        if (withdrawal == null) return null;

        return new WithdrawalResponseDTO(
                withdrawal.getId(),
                withdrawal.getCurrencyCode(),
                withdrawal.getAmount(),
                withdrawal.getIban(),
                withdrawal.getStatus(),
                withdrawal.getCreatedAt()
        );
    }
}
