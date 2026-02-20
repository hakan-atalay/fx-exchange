package service.interfaces;

import dto.request.DepositCreateDTO;
import dto.response.DepositResponseDTO;

public interface DepositService {

	DepositResponseDTO deposit(Long userId, DepositCreateDTO depositDTO);
}