package com.example.RuleEngineApplication.Repository;

import com.example.RuleEngineApplication.Model.Node;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RuleRepository extends MongoRepository<Node, String> {
}
