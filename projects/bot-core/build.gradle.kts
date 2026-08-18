plugins {
    `java-library`
}

dependencies {
    api(project(":contracts:bot-contracts"))
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly(libs.junit.platform.launcher)
}



