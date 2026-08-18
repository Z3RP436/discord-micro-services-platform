plugins {
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
    application
}

application {
    mainClass = "com.z3rp436.discord.bot.api.BotApiApplication"
}

dependencies {
    implementation(project(":projects:bot-core"))
    implementation(project(":contracts:bot-contracts"))
    implementation(libs.spring.boot.starter)
    implementation(libs.spring.boot.starter.web)
    implementation(libs.spring.boot.starter.json)
    implementation(libs.jackson.databind)
    implementation(libs.spring.boot.starter.amqp)
    implementation(libs.spring.boot.starter.data.jpa)
    implementation(libs.spring.boot.starter.validation)
    runtimeOnly(libs.postgresql)
    testImplementation(libs.spring.boot.starter.test)
}



