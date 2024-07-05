package com.nnk.springboot.services;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.exceptions.NotFoundException;
import com.nnk.springboot.repositories.RuleNameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RuleService {
    @Autowired
    private RuleNameRepository ruleNameRepository;

    public List<RuleName> findAll() {
        return ruleNameRepository.findAll();
    }

    public RuleName findById(Integer id) {
        return ruleNameRepository.findById(id).orElseThrow(() -> new NotFoundException("Rule with id " + id + " not found"));
    }

    public RuleName save(RuleName ruleName) {
        return ruleNameRepository.save(ruleName);
    }

    public void update(Integer id, RuleName ruleNameToUpdate) {
        ruleNameToUpdate.setId(id);
        ruleNameRepository.save(ruleNameToUpdate);
    }

    public void deleteById(Integer id) {
        ruleNameRepository.findById(id).orElseThrow(() -> new NotFoundException("Rule with id " + id + " not found"));
        ruleNameRepository.deleteById(id);
    }
}
