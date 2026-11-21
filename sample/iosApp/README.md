# iosApp (Xcode wrapper for the sample)

The Xcode project is included. To run:

1. Open `sample/iosApp/iosApp.xcodeproj` in Xcode 26+.
2. Pick an iPhone simulator (iOS 26 for Liquid Glass, iOS 18 for the classic look) and press Cmd+R.
   The first build runs `./gradlew :sample:embedAndSignAppleFrameworkForXcode` and takes a few minutes.
3. Real device only: put your Apple Team ID in `Configuration/Config.xcconfig` (`TEAM_ID=`). Do not commit it.

If the build can't find Java, launch Xcode from Terminal (`open -a Xcode`) with `JAVA_HOME` set, or set it in
`~/.gradle/gradle.properties` as `org.gradle.java.home=...`.
