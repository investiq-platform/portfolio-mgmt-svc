package com.investiq.portfolio_mgmt_svc.service;

import com.investiq.portfolio_mgmt_svc.model.*;
import com.investiq.portfolio_mgmt_svc.repository.PortfolioRepository;
import com.investiq.portfolio_mgmt_svc.api.PortfolioView;
import com.investiq.portfolio_mgmt_svc.api.TransactionInput;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;

    public PortfolioService(PortfolioRepository portfolioRepository) {
        this.portfolioRepository = portfolioRepository;
    }

    @Transactional(readOnly = true)
    public Portfolio getOwned(long id, String ownerId) {
        return portfolioRepository.findByIdAndOwnerId(id, ownerId).orElseThrow(() -> new IllegalArgumentException("Portfolio not found"));
    }
    @Transactional
    public Portfolio create(String ownerId, String name) {
        if (name == null || name.isBlank() || name.length() > 120) throw new IllegalArgumentException("Portfolio name is required and must be at most 120 characters");
        return portfolioRepository.save(new Portfolio(ownerId, name.trim()));
    }
    @Transactional
    public Portfolio record(String ownerId, TransactionInput input) {
        Portfolio portfolio = getOwned(input.portfolioId(), ownerId);
        validate(input);
        BigDecimal remaining = portfolio.getTransactions().stream().filter(t -> t.getSchemeCode() == input.schemeCode())
            .map(t -> t.getTransactionType() == TransactionType.REDEMPTION ? t.getUnits().negate() : t.getUnits()).reduce(BigDecimal.ZERO, BigDecimal::add);
        if (input.type() == TransactionType.REDEMPTION && remaining.compareTo(input.units()) < 0) throw new IllegalArgumentException("Redemption units exceed current holding");
        portfolio.addTransaction(new InvestmentTransaction(portfolio, input.schemeCode(), input.schemeName().trim(), input.type(), input.units(), input.nav(), input.occurredOn() == null ? LocalDate.now() : input.occurredOn()));
        return portfolio;
    }
    @Transactional(readOnly = true)
    public List<Portfolio> list(String ownerId, int page, int size) {
        if (page < 0 || size < 1 || size > 100) throw new IllegalArgumentException("Invalid page request");
        return portfolioRepository.findByOwnerIdOrderByCreatedAtDesc(ownerId, org.springframework.data.domain.PageRequest.of(page, size));
    }
    public PortfolioView view(Portfolio p, List<com.investiq.portfolio_mgmt_svc.api.HoldingView> holdings) { return new PortfolioView(p.getId(), p.getName(), holdings); }
    private void validate(TransactionInput input) {
        if (input.schemeCode() <= 0 || input.schemeName() == null || input.schemeName().isBlank() || input.units() == null || input.nav() == null || input.units().signum() <= 0 || input.nav().signum() <= 0) throw new IllegalArgumentException("Fund, units, and NAV must be positive");
    }
}
