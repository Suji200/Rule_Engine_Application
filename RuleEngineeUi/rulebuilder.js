// Rule builder logic
let ruleCount = 1;

document.getElementById('add-condition-btn').addEventListener('click', function () {
    ruleCount++;
    
    const ruleContainer = document.createElement('div');
    ruleContainer.className = 'rule-container';
    ruleContainer.id = `rule-${ruleCount}`;
    
    ruleContainer.innerHTML = `
        <div class="rule-group">
            <select name="field" class="field-dropdown">
                <option value="age">Age</option>
                <option value="department">Department</option>
                <option value="salary">Salary</option>
                <option value="experience">Experience</option>
            </select>
            
            <select name="operator" class="operator-dropdown">
                <option value=">">></option>
                <option value="<"><</option>
                <option value="=">=</option>
            </select>
            
            <input type="text" name="value" class="value-input" placeholder="Value">
        </div>
        
        <div class="rule-group">
            <select name="logical-operator" class="logical-operator-dropdown">
                <option value="AND">AND</option>
                <option value="OR">OR</option>
            </select>
        </div>
    `;
    
    document.getElementById('rule-builder').appendChild(ruleContainer);
});

document.getElementById('submit-btn').addEventListener('click', function () {
    console.log("+++++++++++++++++++++++++++++++++++++++++++++++++");
    const ruleContainers = document.querySelectorAll('.rule-container');
    let ruleString = '';

    ruleContainers.forEach((container, index) => {
        const field = container.querySelector('.field-dropdown').value;
        const operator = container.querySelector('.operator-dropdown').value;
        const value = container.querySelector('.value-input').value;
        const logicalOperator = container.querySelector('.logical-operator-dropdown').value;

        if (index === 0) {
            ruleString += `(${field} ${operator} ${value})`;
        } else {
            ruleString += ` ${logicalOperator} (${field} ${operator} ${value})`;
        }
    });

    // Send rule to the backend
    fetch('http://localhost:8080/rules/create', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ rule: ruleString }),
    })
    .then(response => response.json())
    .then(data => {
        console.log('Rule saved successfully:', data);
    })
    .catch((error) => {
        console.error('Error:', error);
    });

    // Display the rule string
    document.getElementById('rule-output').innerText = ruleString;
});
