package com.investiq.portfolio_mgmt_svc.repository;
import com.investiq.portfolio_mgmt_svc.model.InvestmentTransaction;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
public interface InvestmentTransactionRepository extends JpaRepository<InvestmentTransaction, Long> {
    List<InvestmentTransaction> findByPortfolioIdIn(List<Long> portfolioIds);
}
