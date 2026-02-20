package service.interfaces;

import dto.request.WalletCreateDTO;
import dto.response.WalletResponseDTO;

import java.math.BigDecimal;
import java.util.List;

public interface WalletService {
	
	WalletResponseDTO createWallet(Long userId, WalletCreateDTO request);

	List<WalletResponseDTO> getUserWallets(Long userId);

	WalletResponseDTO getWallet(Long userId, String currencyCode);

	void credit(Long userId, String currencyCode, BigDecimal amount);

	void debit(Long userId, String currencyCode, BigDecimal amount);

	WalletResponseDTO ensureWalletExists(Long userId, String currencyCode);
}