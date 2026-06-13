# CI/CD

## Workflows

One workflow file: `.github/workflows/build.yml` — **KMP CI**

**Triggers:**
- Push to `main` or `develop`
- Pull request (opened, synchronize, reopened, ready_for_review) targeting `main` or `develop`

The workflow runs two independent parallel jobs.

---

### Job: Android Build & Tests (`ubuntu-latest`)

| Step | What it does |
|---|---|
| Checkout | `actions/checkout@v4` |
| Setup JDK 17 | Temurin distribution via `actions/setup-java@v4` |
| Setup Android SDK | `android-actions/setup-android@v3` |
| Restore `secrets.properties` | Writes the file from `IOS_LOCAL_SECRETS` secret |
| Restore `google-services.json` | Writes `composeApp/src/google-services.json` from `FIREBASE_ANDROID_JSON` secret |
| Decode keystore | Base64-decodes `RELEASE_KEY` → `keystore/release_key.jks`; decodes `RELEASE_KEY_PROPERTIES` → `keystore/keystore.properties` |
| Cache Gradle | Caches `~/.gradle/caches` and `~/.gradle/wrapper` keyed on Gradle file hashes |
| Lint + Tests | `./gradlew ktlintCheck detekt test` |
| Build debug APK | `./gradlew assembleDebug` |
| Upload to Firebase App Distribution | `./gradlew appDistributionUploadDebug` (requires `FIREBASE_TOKEN`) |
| Upload APK artifact | Uploads `composeApp/build/outputs/apk/debug/*.apk` as `debug-apk` artifact |

---

### Job: iOS Build & Tests (`macos-latest`)

| Step | What it does |
|---|---|
| Checkout | `actions/checkout@v4` |
| Setup JDK 17 | Temurin distribution |
| Select Xcode | `sudo xcode-select -s /Applications/Xcode.app` |
| Restore `secrets.properties` | Writes from `IOS_LOCAL_SECRETS` secret |
| Restore `GoogleService-Info.plist` | Writes `iosApp/iosApp/GoogleService-Info.plist` from `FIREBASE_IOS_PLIST` secret |
| Decode keystore | Same as Android job (required by Gradle even on iOS builds) |
| Cache Gradle | Same cache strategy as Android job |
| iOS Tests | `./gradlew iosSimulatorArm64Test` |
| Build iOS app | `xcodebuild` Debug build for iOS Simulator; Xcode drives the Gradle KMP framework compile |
| Upload iOS artifact | Uploads the built `.app` bundle as `ios-debug-build` artifact |

**Note:** iOS Firebase App Distribution upload is present in the workflow file but commented out. Archive and IPA export steps are also commented out.

---

## Key commands

```shell
# Lint and static analysis (both jobs, Android only executes)
./gradlew ktlintCheck detekt

# All common + Android unit tests
./gradlew test

# iOS simulator tests
./gradlew iosSimulatorArm64Test

# Debug APK
./gradlew assembleDebug

# Upload debug APK to Firebase App Distribution
./gradlew appDistributionUploadDebug

# iOS build (via Xcode, which drives Gradle internally)
xcodebuild \
  -project iosApp/iosApp.xcodeproj \
  -scheme iosApp \
  -configuration Debug \
  -destination 'generic/platform=iOS Simulator' \
  build
```

---

## Secrets and environment

| Secret name | Used by | Description |
|---|---|---|
| `IOS_LOCAL_SECRETS` | both jobs | Contents of `secrets.properties` in project root; required by `GenerateSecretsTask` at compile time |
| `FIREBASE_ANDROID_JSON` | Android job | Contents of `composeApp/src/google-services.json`; required by Firebase Android SDK |
| `FIREBASE_IOS_PLIST` | iOS job | Contents of `iosApp/iosApp/GoogleService-Info.plist`; required by Firebase iOS SDK |
| `RELEASE_KEY` | both jobs | Base64-encoded `release_key.jks` keystore file |
| `RELEASE_KEY_PROPERTIES` | both jobs | Base64-encoded `keystore.properties` file with keystore alias, password, and key password |
| `FIREBASE_TOKEN` | Android job | Firebase CLI token used by `appDistributionUploadDebug` Gradle task |
