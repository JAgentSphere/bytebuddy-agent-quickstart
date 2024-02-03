package com.jas.quickstart.core.config;

import lombok.*;

/**
 * @author ReaJason
 * @since 2024/1/29
 */
@Data
@ToString
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class AgentConfig {
    private boolean debug;
    private boolean metric;
    private boolean dumpClass;
    private String dumpFolder;
}
