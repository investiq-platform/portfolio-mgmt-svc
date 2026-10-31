package com.investiq.portfolio_mgmt_svc.service;

import com.investiq.portfolio_mgmt_svc.model.Portfolio;
import com.investiq.portfolio_mgmt_svc.repository.PortfolioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;

    public PortfolioService(PortfolioRepository portfolioRepository) {
        this.portfolioRepository = portfolioRepository;
    }

    public Portfolio getPortfolio(String userId) {
        return portfolioRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Portfolio not found"));
    }

    public Portfolio createPortfolio(Portfolio portfolio) {
        return portfolioRepository.save(portfolio);
    }

    public Portfolio updatePortfolio(Portfolio portfolio) {
        String userId = portfolio.getUserId();
        if (userId == null || !portfolioRepository.existsById(userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Portfolio not found");
        }
        return portfolioRepository.save(portfolio);
    }

    public void deletePortfolio(String userId) {
        portfolioRepository.deleteById(userId);
    }
}
