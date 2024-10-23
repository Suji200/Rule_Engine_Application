package com.example.RuleEngineApplication.ControllerTest;

import com.example.RuleEngineApplication.Controller.RuleController;
import com.example.RuleEngineApplication.Model.Node;
import com.example.RuleEngineApplication.Service.RuleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RuleController.class)
public class RuleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private RuleService ruleService;

    @InjectMocks
    private RuleController ruleController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateRule() throws Exception {
        Node mockNode = new Node("operator", "AND");
        when(ruleService.parseRule(anyString())).thenReturn(mockNode);
        when(ruleService.saveRule(mockNode)).thenReturn(mockNode);

        mockMvc.perform(post("/rules/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("\"age > 30 AND department = 'Sales'\""))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testEvaluateRule() throws Exception {
        Node mockNode = new Node("operator", "AND");
        when(ruleService.getRuleById("67188b10da2c933dc4e9bb44")).thenReturn(mockNode);
        when(ruleService.evaluateRule(mockNode, anyMap())).thenReturn(true);

        mockMvc.perform(post("/rules/evaluate/67188b10da2c933dc4e9bb44")
                        .contentType(MediaType.APPLICATION_JSON)
                        .contentType("{\"age\": 35, \"department\": \"Sales\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    public void testEvaluateRule_notFound() throws Exception {
        when(ruleService.getRuleById("invalidId")).thenReturn(null);

        mockMvc.perform(post("/rules/evaluate/invalidId")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"age\": 35, \"department\": \"Sales\"}"))
                .andExpect(status().isNotFound());
    }
}
