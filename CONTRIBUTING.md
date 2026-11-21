# Contributing
- Build Android: `./gradlew :sample:assembleDebug`
- iOS compile check (macOS): `./gradlew :glasskit:compileKotlinIosSimulatorArm64`
- Tests: `./gradlew :glasskit:testDebugUnitTest`
- Public API must stay explicit (`explicitApi()` is on). Keep new types `internal` unless users need them.
- Include a screenshot or GIF for any visual change, and say which iOS versions you tested.
