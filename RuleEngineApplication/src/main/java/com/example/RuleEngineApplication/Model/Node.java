package com.example.RuleEngineApplication.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
@Getter
@Setter


@Document("Nodes")
public class Node {

    @Id
    private String id;  // MongoDB uses String for IDs

    private String type;  // "operator" or "operand"
    private String value; // condition for operand nodes ("age > 30")

    private Node left;    // left child node
    private Node right;   // right child node

    public Node(String type, String value){
        this.type = type;
        this.value = value;
    }
    public Node(String type, String value, Node left, Node right) {
        this.type = type;
        this.left = left;
        this.right = right;
        this.value  = value;
    }



}
