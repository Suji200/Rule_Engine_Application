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
    private String id;

    private String type;
    private String value;

    private Node left;
    private Node right;

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
