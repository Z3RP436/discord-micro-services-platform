plugins {
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
    application
}

application {
    mainClass = "com.z3rp436.discord.bot.logging.BotLoggingApplication"
}

dependencies {
    implementation(project(":contracts:bot-contracts"))
    implementation(libs.spring.boot.starter)
    implementation(libs.spring.boot.starter.amqp)
    testImplementation(libs.spring.boot.starter.test)
}

