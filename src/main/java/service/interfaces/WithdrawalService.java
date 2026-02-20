package service.interfaces;

import dto.request.WithdrawalCreateDTO;
import dto.response.WithdrawalResponseDTO;


public interface WithdrawalService {
	
	WithdrawalResponseDTO withdraw(Long userId, WithdrawalCreateDTO request);
}