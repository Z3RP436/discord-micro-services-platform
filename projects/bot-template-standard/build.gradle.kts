plugins {
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
    application
}

application {
    mainClass = "com.z3rp436.discord.bot.template.standard.BotTemplateStandardApplication"
}

dependencies {
    implementation(project(":projects:bot-core"))
    implementation(project(":contracts:bot-contracts"))
    implementation(libs.spring.boot.starter)
    implementation(libs.spring.boot.starter.web)
    implementation(libs.spring.boot.starter.amqp)
    implementation(libs.spring.boot.starter.validation)
    testImplementation(libs.spring.boot.starter.test)
    testImplementation(libs.spring.amqp.test)
}


