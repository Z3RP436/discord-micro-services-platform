# Development IntelliJ Setup

## Goal

Ensure all Gradle modules are recognized as Java projects in IntelliJ (with `src/main/java` and `src/main/resources`) and provide shared run configurations.

## What Is Included In This Repository

- Shared IntelliJ run configurations in `.run/`.
- Gradle multi-project setup in `settings.gradle.kts`.
- Java plugin is active through module build scripts.

## One-Time IntelliJ Import Steps

1. Close the project in IntelliJ if already opened as a plain folder.
2. Open IntelliJ and select **Open**.
3. Choose the root file `settings.gradle.kts` (or the repository root and then import as Gradle project).
4. In the import dialog:
   - Use **Gradle from wrapper**
   - Use project JDK: `C:\Users\nicop\.jdks\openjdk-24.0.1`
5. Wait for full Gradle sync.

After sync, IntelliJ should mark these automatically:

- `src/main/java` as Sources Root
- `src/main/resources` as Resources Root
- `src/test/java` as Test Sources Root
- `src/test/resources` as Test Resources Root

## If Folders Are Still Plain

Use this recovery sequence:

1. Open **Gradle** tool window.
2. Click **Reload All Gradle Projects**.
3. Go to **File > Invalidate Caches / Restart > Invalidate and Restart**.
4. Reopen via `settings.gradle.kts`.

## Shared Run Configurations

The repository now provides these run configs:

- `.run/00-build-all.run.xml`
- `.run/01-test-all.run.xml`
- `.run/10-bot-api-bootrun.run.xml`
- `.run/11-bot-orchestrator-bootrun.run.xml`
- `.run/12-bot-template-standard-bootrun.run.xml`
- `.run/13-bot-identity-bootrun.run.xml`
- `.run/14-bot-logging-bootrun.run.xml`

They appear in IntelliJ Run/Debug configurations after project refresh.

## Notes

- `*.iml` and `.idea/` are intentionally not committed.
- The authoritative project model is Gradle; IntelliJ metadata is generated locally.

