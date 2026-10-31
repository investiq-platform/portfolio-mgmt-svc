package com.investiq.portfolio_mgmt_svc.repository;

import com.investiq.portfolio_mgmt_svc.model.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PortfolioRepository extends JpaRepository<Portfolio, String> {
}
