package com.loan.controller;

import com.loan.dto.LoanDecisionDto;
import com.loan.model.Agent;
import com.loan.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/agents")
public class AgentController {
    private final LoanService loanService;

    @Autowired
    public AgentController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping("/")
    public Agent createAgent(@RequestBody Agent agent) {
        return loanService.createAgent(agent);
    }
    @GetMapping("/{id}")
    public Agent getAgentById(@PathVariable Long id) {
        return loanService.getAgentById(id);
    }

    @GetMapping("/")
    public Iterable<Agent> getAllAgents() {
        return loanService.getAllAgents();
    }

    @PutMapping("/{agentId}/loans/{loanId}/decision")
    public void agentDecision(@PathVariable Long agentId,
                              @PathVariable Long loanId,
                              @RequestBody LoanDecisionDto dto) {
        loanService.agentDecision(agentId, loanId, dto.getDecision());
    }
}
