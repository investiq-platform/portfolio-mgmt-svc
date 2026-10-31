package com.investiq.portfolio_mgmt_svc.controller;

import com.investiq.portfolio_mgmt_svc.model.Portfolio;
import com.investiq.portfolio_mgmt_svc.service.PortfolioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/v1/portfolio")
public class Controller {
    private final PortfolioService portfolioService;

    public Controller(PortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }

    @GetMapping("/getPortfolio")
    public ResponseEntity<Portfolio> getPortfolio(@RequestParam String userId) {
        Portfolio portfolio = portfolioService.getPortfolio(userId);
        return ResponseEntity.ok(portfolio);
    }

    @PostMapping("/createPortfolio")
    public ResponseEntity<Portfolio> createPortfolio(@RequestBody Portfolio portfolio) {
        Portfolio createdPortfolio = portfolioService.createPortfolio(portfolio);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPortfolio);
    }

    @PutMapping("/updatePortfolio")
    public ResponseEntity<Portfolio> updatePortfolio(@RequestBody Portfolio portfolio) {
        Portfolio updatedPortfolio = portfolioService.updatePortfolio(portfolio);
        return ResponseEntity.ok(updatedPortfolio);
    }

    @DeleteMapping("/deletePortfolio")
    public ResponseEntity<Void> deletePortfolio(@RequestParam String userId) {
        portfolioService.deletePortfolio(userId);
        return ResponseEntity.noContent().build();
    }
}