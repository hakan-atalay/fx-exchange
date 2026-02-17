package mapper;

import dto.response.WalletResponseDTO;
import entity.Wallet;

public class WalletMapper {

    private WalletMapper() {}

    public static WalletResponseDTO toResponse(Wallet wallet) {
        if (wallet == null) return null;

        return new WalletResponseDTO(
                wallet.getId(),
                wallet.getCurrencyCode(),
                wallet.getBalance()
        );
    }
}
