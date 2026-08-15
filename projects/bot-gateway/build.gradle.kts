plugins {
    application
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
}

dependencies {
    implementation(libs.spring.boot.starter)
}

application {
    mainClass = "com.z3rp436.discord.gateway.BotGatewayApplication"
}