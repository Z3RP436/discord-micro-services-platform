package com.z3rp436.discord.core;

public enum BotProfile {
    SMALL(256, 2048),
    MEDIUM(512, 5120),
    LARGE(1024, 10240);

    private final int memoryMb;
    private final int storageMb;

    BotProfile(int memoryMb, int storageMb) {
        this.memoryMb = memoryMb;
        this.storageMb = storageMb;
    }

    public int memoryMb() {
        return memoryMb;
    }

    public int storageMb() {
        return storageMb;
    }
}

