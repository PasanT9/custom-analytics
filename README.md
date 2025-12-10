# Custom Analytics Data Provider for WSO2 API Manager

This module provides a **custom analytics data provider** for WSO2 API Manager to extract and publish **AI Guardrail intervention details** as part of API Analytics.

It captures:

- `interveningGuardrail` – which guardrail triggered an intervention  
- `direction` – whether the intervention happened on the request or response path  

This allows operators to better understand **why guardrails blocked, modified, or intervened in AI API flows**.

---

## 🔧 Features

- Extracts guardrail metadata from Synapse message context  
- Publishes custom analytics fields:
  - `interveningGuardrail`
  - `interveningGuardrailDirection`

---

## 📁 Project Structure

```

org.wso2.carbon.custom.publisher
└── CustomDataProvider.java

## 📦 Example Guardrail Event

The guardrail mediator adds JSON like this:

```json
{
  "interveningGuardrail": "WordCount GuardRail",
  "action": "GUARDRAIL_INTERVENED",
  "actionReason": "Violation of applied word count constraints detected.",
  "direction": "REQUEST"
}
````

This provider extracts:

* `interveningGuardrail = "WordCount GuardRail"`
* `direction = "REQUEST"`

These fields appear in analytics logs or downstream monitoring systems (ELK, Splunk, etc.)

---

## ⚙️ Configuration (deployment.toml)

Add the following to enable the custom analytics provider:

```toml
[apim.analytics]
enable = true
type = "log"
properties."publisher.custom.data.provider.class" = "org.wso2.carbon.custom.publisher.CustomDataProvider"
```

---

## 🛠️ Building the Project

Run:

```bash
mvn clean install
```

Output JAR is generated at:

```
target/org.wso2.carbon.custom.publisher-1.0-SNAPSHOT.jar
```

---

## 🚀 Deployment

Copy the generated JAR into the API-M Gateway:

```
<API-M_HOME>/repository/components/dropins/
```

Restart API Manager:

```bash
sh api-manager.sh
```
