package com.z3rp436.discord.contracts;

import java.util.Set;

public final class ContractSchema {

    public static final String V1 = "v1";
    public static final Set<String> SUPPORTED_VERSIONS = Set.of(V1);

    private ContractSchema() {
    }

    public static boolean isSupported(String schemaVersion) {
        return SUPPORTED_VERSIONS.contains(schemaVersion);
    }
}

