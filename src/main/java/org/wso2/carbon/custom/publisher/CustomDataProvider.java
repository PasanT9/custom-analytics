/*
 *  Copyright (c) 2025, WSO2 LLC. (http://www.wso2.org) All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package org.wso2.carbon.custom.publisher;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext;
import org.wso2.carbon.apimgt.common.analytics.collectors.AnalyticsCustomDataProvider;

import java.util.HashMap;
import java.util.Map;

/**
 * Custom data provider to extract AI guardrail analytics
 */
public class CustomDataProvider implements AnalyticsCustomDataProvider {

    private static final Log log = LogFactory.getLog(CustomDataProvider.class);

    /**
     * Constructor
     */
    public CustomDataProvider() {

        log.info("Successfully initialized");
    }

    /**
     * Extract custom properties for analytics
     *
     * @param context Message context
     * @return Map of custom properties
     */
    @Override
    public Map<String, Object> getCustomProperties(Object context) {

        if (context instanceof MessageContext) {
            MessageContext msgCtx = (MessageContext) context;
            Map<String, Object> customProperties = new HashMap<>();
            // Extract guardrail analytics
            GuardrailInfo info = extractGuardrailInfo(msgCtx);
            if (info != null) {
                if (info.interveningGuardrail != null) {
                    customProperties.put("interveningGuardrail", info.interveningGuardrail);
                }
                if (info.direction != null) {
                    customProperties.put("interveningGuardrailDirection", info.direction);
                }
            }
            return customProperties;
        }
        return null;
    }

    /**
     * Extract guardrail info from the message context
     */
    private GuardrailInfo extractGuardrailInfo(MessageContext context) {

        Object result = context.getProperty("ERROR_MESSAGE");
        if (result == null) {
            return null;
        }

        try {
            JsonObject json = JsonParser.parseString(result.toString()).getAsJsonObject();
            GuardrailInfo info = new GuardrailInfo();

            if (json.has("interveningGuardrail")) {
                info.interveningGuardrail = json.get("interveningGuardrail").getAsString();
            }
            if (json.has("direction")) {
                info.direction = json.get("direction").getAsString();
            }
            return info;
        } catch (Exception e) {
            log.error("Failed to parse AI guardrail analytics JSON using Gson", e);
        }
        return null;
    }

    /**
     * Class to hold guardrail info
     */
    private static class GuardrailInfo {

        String interveningGuardrail;
        String direction;
    }

}
