# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build Commands

```bash
# Build debug APK
./gradlew :client:composeApp:assembleDebug

# Build all modules
./gradlew build

# Run all tests
./gradlew test

# Run tests for a specific module
./gradlew :shared:app:test
./gradlew :shared:feature:search:test

# Run a specific test class
./gradlew :shared:app:test --tests ComposeAppCommonTest

# Install on connected Android device
./gradlew :client:composeApp:installDebug
```

For iOS, open `client/iosApp` in Xcode and build from there.

## Architecture

MovieSafe is a **Kotlin Multiplatform (KMP)** app targeting Android and iOS using **Compose Multiplatform** for shared UI.

### Module Structure

```
client/
  composeApp/       # KMP application — platform entry points + shared App() composable
  iosApp/           # Native iOS wrapper that embeds the KMP framework
shared/
  app/              # Core module: App composable, Platform interface
  feature/
    search/         # Movie search feature
    watchlist/      # Watchlist management
    preferences/    # User preferences
```

Each feature module follows: `api/` → `repository/` → `usecase/` → `ui/` (Compose screens).

### Key Architectural Points

- **Entry points:** Android uses `MainActivity` → `setContent { App() }`; iOS uses `MainViewController` → `ComposeUIViewController { App() }`.
- **DI:** Koin
- **Source sets:** Common code lives in `commonMain`; platform-specific code in `androidMain` / `iosMain`. Tests go in `commonTest`.
- **Architecture:** Clean architecture with MVI pattern.

### Tech Stack

| Concern | Library |
|---|---|
| UI | Compose Multiplatform, Material3 |
| DI | Koin |
| Async | Kotlin Coroutines |
| Testing | kotlin-test, coroutines-test, koin-test |

### Dependency Management

All versions are centralized in `gradle/libs.versions.toml`. Use the version catalog accessors (e.g., `libs.koin.core`) rather than hardcoded strings.