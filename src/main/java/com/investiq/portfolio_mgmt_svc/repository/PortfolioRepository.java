package com.investiq.portfolio_mgmt_svc.repository;

import com.investiq.portfolio_mgmt_svc.model.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
    Optional<Portfolio> findByIdAndOwnerId(Long id, String ownerId);
    List<Portfolio> findByOwnerIdOrderByCreatedAtDesc(String ownerId, org.springframework.data.domain.Pageable pageable);
}
