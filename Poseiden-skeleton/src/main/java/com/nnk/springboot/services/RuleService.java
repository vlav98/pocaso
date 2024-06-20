package com.nnk.springboot.services;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.repositories.RuleNameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RuleService {
    @Autowired
    private RuleNameRepository ruleNameRepository;

    public List<RuleName> findAll() {
        return ruleNameRepository.findAll();
    }

    public RuleName findById(Integer id) {
        Optional<RuleName> optionalRuleName =  ruleNameRepository.findById(id);
        return optionalRuleName.orElse(null);
    }

    public RuleName save(RuleName ruleName) {
        return ruleNameRepository.save(ruleName);
    }

    public void update(Integer id, RuleName ruleNameToUpdate) {
        ruleNameToUpdate.setId(id);
        ruleNameRepository.save(ruleNameToUpdate);
    }

    public void deleteById(Integer id) {
        ruleNameRepository.deleteById(id);
    }
}
