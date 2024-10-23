package com.example.RuleEngineApplication.Controller;

import com.example.RuleEngineApplication.Model.Node;
import com.example.RuleEngineApplication.Repository.RuleRepository;
import com.example.RuleEngineApplication.Service.RuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/rules")
public class RuleController {

    @Autowired
    private RuleRepository ruleRepository;

    @Autowired
    private RuleService ruleService;

    // API to create a rule from a rule string
    @PostMapping("/create")
    public Node createRule(@RequestBody String ruleString) {
        Node rule = ruleService.parseRule(ruleString);
        return ruleService.saveRule(rule);
    }

    // API to evaluate a rule against user data
//    @PostMapping("/evaluate/{ruleId}")
//    public boolean evaluateRule(@PathVariable String ruleId, @RequestBody Map<String, Object> userData) {
//        Node rule = ruleService.saveRule(ruleId); // Fetch rule from DB
//        return ruleService.evaluateRule(rule, userData);
//    }

    @PostMapping("/evaluate/{ruleId}")
    public boolean evaluateRule(@PathVariable String ruleId, @RequestBody Map<String, Object> userData) {
        // Fetch the rule from DB using ruleId

        Node rule = ruleService.getRuleById(ruleId);
        System.out.println("Fetched rule: " + rule);
        if (rule == null) {
            System.out.println("No rule found with id: " + ruleId);
        }
            // Evaluate the fetched rule against user data
        return ruleService.evaluateRule(rule, userData);
    }


}


