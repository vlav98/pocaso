package com.nnk.springboot.services;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.repositories.RuleNameRepository;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

@SpringBootTest
public class RuleServiceTests {
    @Autowired
    private RuleService ruleService;
    @MockBean
    private RuleNameRepository ruleNameRepository;
    
    @Test
    public void testFindAll() {
        ruleService.findAll();
        verify(ruleNameRepository).findAll();
    }

    @Test
    public void testFindById_Successfully() {
        when(ruleNameRepository.findById(anyInt())).thenReturn(Optional.of(new RuleName()));

        ruleService.findById(anyInt());
        verify(ruleNameRepository).findById(anyInt());
    }

    @Test
    public void testFindById_InvalidId() {
        when(ruleNameRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> ruleService.findById(anyInt()));
        verify(ruleNameRepository).findById(anyInt());
    }

    @Test
    public void testSaveRuleName() {
        RuleName mockRuleName = new RuleName();
        ruleService.save(mockRuleName);
        verify(ruleNameRepository).save(mockRuleName);
    }

    @Test
    public void testUpdateRuleName_Successfully() {
        RuleName mockRuleName = new RuleName();
        ruleService.update(anyInt(), mockRuleName);
        verify(ruleNameRepository).save(mockRuleName);
    }

    @Test
    public void testDeleteRuleName_Successfully() {
        when(ruleNameRepository.findById(anyInt())).thenReturn(Optional.of(new RuleName()));
        ruleService.deleteById(anyInt());
        verify(ruleNameRepository).deleteById(anyInt());
    }

    @Test
    public void testDeleteRuleName_InvalidId() {
        when(ruleNameRepository.findById(anyInt())).thenReturn(Optional.empty());
        assertThrows(Exception.class, () -> ruleService.deleteById(anyInt()));
    }
}
