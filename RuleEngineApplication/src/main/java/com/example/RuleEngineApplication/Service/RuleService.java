package com.example.RuleEngineApplication.Service;

import com.example.RuleEngineApplication.Model.Node;
import com.example.RuleEngineApplication.Repository.RuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class RuleService {
    @Autowired
    private  RuleRepository ruleRepository;

    public Node parseRule(String rule) {

        if (rule.contains("AND")) {
            String[] parts = rule.split("AND", 2);
            Node leftNode = parseRule(parts[0].trim());  // Recursively parse the left side
            Node rightNode = parseRule(parts[1].trim()); // Recursively parse the right side
            return new Node("operator", "AND", leftNode, rightNode);
        }
        // Handle OR condition
        else if (rule.contains("OR")) {
            String[] parts = rule.split("OR", 2);  // Split at the first "OR" occurrence
            Node leftNode = parseRule(parts[0].trim());
            Node rightNode = parseRule(parts[1].trim());
            return new Node("operator", "OR", leftNode, rightNode);
        }
        // Otherwise, it's a simple condition (operand)
        else {
            return new Node("operand", rule.trim());
        }
    }

    // Save the rule (AST) to the MongoDB database
    public Node saveRule(Node rule) {
        return ruleRepository.save(rule);
    }

    // Evaluate the AST against user data
    public boolean evaluateRule(Node node, Map<String, Object> userData) {
        if (node == null) {
            return false;
        }

        System.out.println("Evaluating node: " + node);
        System.out.println("User data: " + userData);

        if (node.getType().equals("operator")) {
            boolean leftResult = evaluateRule(node.getLeft(), userData);
            boolean rightResult = evaluateRule(node.getRight(), userData);

            if (node.getValue().equals("AND")) {
                return leftResult && rightResult;
            } else if (node.getValue().equals("OR")) {
                return leftResult || rightResult;
            }
        } else if (node.getType().equals("operand")) {
            String condition = node.getValue();
            System.out.println("Evaluating condition: " + condition);


            if (condition.contains(">")) {
                String[] parts = condition.split(">");
                String field = parts[0].trim();
                int threshold = Integer.parseInt(parts[1].trim());
                int userValue = (int) userData.get(field);
                return userValue > threshold;
            } else if (condition.contains("=")) {
                String[] parts = condition.split("=");
                String field = parts[0].trim();
                String expectedValue = parts[1].trim().replace("'", "");
                String userValue = (String) userData.get(field);
                return userValue.equals(expectedValue);
            }
        }
        return false;
    }

    public Node getRuleById(String ruleId) {
        return ruleRepository.findById(ruleId).orElse(null);
    }
}

