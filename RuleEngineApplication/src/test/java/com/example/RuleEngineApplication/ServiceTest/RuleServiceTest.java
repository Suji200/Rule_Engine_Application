package com.example.RuleEngineApplication.ServiceTest;

import com.example.RuleEngineApplication.Model.Node;
import com.example.RuleEngineApplication.Repository.RuleRepository;
import com.example.RuleEngineApplication.Service.RuleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class RuleServiceTest {


    @InjectMocks
    private RuleService ruleService;

    @Mock
    private RuleRepository ruleRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testParseRule_withANDCondition() {
        String ruleString = "age > 30 AND department = 'Sales'";
        Node rule = ruleService.parseRule(ruleString);

        assertNotNull(rule);
        assertEquals("operator", rule.getType());
        assertEquals("AND", rule.getValue());
        assertEquals("operand", rule.getLeft().getType());
        assertEquals("operand", rule.getRight().getType());
    }

    @Test
    public void testEvaluateRule_withTrueCondition() {
        Node rule = new Node("operator", "AND",
                new Node("operand", "age > 30"),
                new Node("operand", "department = 'Sales'"));
        Map<String, Object> userData = new HashMap<>();
        userData.put("age", 35);
        userData.put("department", "Sales");

        boolean result = ruleService.evaluateRule(rule, userData);
        assertTrue(result);
    }

    @Test
    public void testEvaluateRule_withFalseCondition() {
        Node rule = new Node("operator", "AND",
                new Node("operand", "age > 30"),
                new Node("operand", "department = 'Sales'"));
        Map<String, Object> userData = new HashMap<>();
        userData.put("age", 25);
        userData.put("department", "Marketing");

        boolean result = ruleService.evaluateRule(rule, userData);
        assertFalse(result);
    }

    @Test
    public void testGetRuleById() {
        Node mockNode = new Node("operator", "AND");
        when(ruleRepository.findById("67188b10da2c933dc4e9bb44")).thenReturn(Optional.of(mockNode));

        Node rule = ruleService.getRuleById("67188b10da2c933dc4e9bb44");
        assertNotNull(rule);
    }

    @Test
    public void testGetRuleById_notFound() {
        when(ruleRepository.findById("invalidId")).thenReturn(Optional.empty());

        Node rule = ruleService.getRuleById("invalidId");
        assertNull(rule);
    }
}

