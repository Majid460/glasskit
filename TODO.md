# TODO (in order)
1. Open this folder in Android Studio, let Gradle sync. Bump versions in `gradle/libs.versions.toml` if sync fails.
2. `./gradlew :sample:assembleDebug` (Android) and `./gradlew :glasskit:compileKotlinIosSimulatorArm64` (iOS Kotlin).
3. Open `sample/iosApp/iosApp.xcodeproj` in Xcode 26 and run on an iOS 26 and an iOS 18 simulator.
4. Verify: glass look, blur behind bar, bar height/safe area, tab switching, rotation, dark mode.
5. Decide behavior on iOS < 26 (keep UITabBar or Material bar).
6. Native `UINavigationBar` bridge driven by the Kotlin back stack.
7. DONE (unverified): GlassApp + GlassNavigator + drawer; verify Nav3 API names compile against navigation3-ui 1.1.1.
8. Add LICENSE (Apache-2.0, via GitHub license picker), binary-compatibility-validator, Maven Central publishing, klibs.io listing.
9. README GIF + ProAndroidDev article.
