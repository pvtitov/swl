# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project overview

"Simple Wish List" is an Android app (Kotlin, Jetpack Compose) that lets a user maintain a wish list and share it with friends without a dedicated backend server. Sharing is currently implemented via Google Drive (each user's wish list is a JSON file in their own Drive; friends are granted read permission on it) and via manual JSON file export/import as a fallback. Root project name is `Simple Wish List`; the Gradle module directories are `app` and `noserver`.

## Build system

Gradle with Groovy DSL for `app`, Kotlin DSL for `noserver`. AGP 8.12.3, Kotlin 2.2.21, compileSdk/targetSdk 36, minSdk 23, JVM target 17.

Common commands (run from repo root):

```
./gradlew assembleDebug              # build the app module
./gradlew build                      # build everything (app + noserver)
./gradlew test                       # run all JVM unit tests
./gradlew :app:testDebugUnitTest     # run app module unit tests only
./gradlew :noserver:testDebugUnitTest    # run noserver module unit tests only
./gradlew test --tests "*.ClassName"                 # run a single test class
./gradlew test --tests "*.ClassName.methodName"      # run a single test method
./gradlew connectedAndroidTest       # run instrumented tests (needs a device/emulator)
./gradlew lint                       # Android lint
```

There is currently no meaningful unit test coverage in either module — the only files under `src/test`/`src/androidTest` are the stock Android-Studio-generated `ExampleUnitTest`/`ExampleInstrumentedTest` stubs. `app`'s test dependencies (JUnit4, Mockito, MockK, mockito-kotlin, kotlinx-coroutines-test) are wired up in `build.gradle` but unused so far. Treat `./gradlew test` as a build-health check, not a correctness signal, until real tests exist.

No ktlint/detekt/editorconfig is configured; style follows plain Android Studio Kotlin defaults.

### Required local configuration

The `noserver` module reads `GOOGLE_WEB_CLIENT_ID` out of `local.properties` (via `gradleLocalProperties`) and injects it as a `BuildConfig` field for Google Sign-In / Drive API auth. A build will fail without this key set in `local.properties` (not checked into git).

### `noserver` is consumed as a published AAR, not a source dependency

`app` depends on `noserver` via Maven coordinates (`implementation 'com.github.pvtitov:noserver:1.0.0'` in `app/build.gradle`), resolved from an in-repo local Maven repository at `local-repo/` (declared as a `dependencyResolutionManagement` repository in `settings.gradle`). It is **not** `implementation project(':noserver')` — that project dependency was deliberately removed so `noserver` behaves like a real standalone SDK artifact (publishable, versioned) even though it still lives in this repo for convenience.

Practical consequence: editing code under `noserver/` has **no effect on `app`** until you republish it:

```
./gradlew :noserver:publishReleasePublicationToLocalRepoRepository   # regenerates local-repo/
```

Bump the `version` in `noserver/build.gradle.kts`'s `publishing {}` block when you do this (Gradle/Maven will otherwise happily reuse a stale cached resolution of the same version), then update the version string in `app/build.gradle` to match, then rebuild `app`. `local-repo/` is checked into git (small — one AAR + POM/module metadata per version) so a clean checkout builds without anyone needing to run the publish task first.

In `noserver/build.gradle.kts`, `kotlinx-serialization-json` is declared `api` (not `implementation`) deliberately: `GoogleDriveRepository`/`JsonUtils` expose `public inline fun <reified T>` members whose bodies reference `kotlinx.serialization.json.Json`, so consumers need that dependency on their own compile classpath, not just noserver's.

## Architecture

Two Gradle modules:

- **`app`** (`com.github.pvtitov.simplewishlist`) — the UI and app-level domain logic, built with Jetpack Compose and a single shared `MainViewModel` (no navigation library; screens are modeled as a sealed `Screen` state and switched in `NavigationComposable`).
- **`noserver`** (`com.github.pvtitov.noserver`) — an Android library module providing "serverless" data sync/sharing backends (Google Drive today; manual file export as a fallback), packaged as a small SDK and consumed by `app` as a published AAR (see "`noserver` is consumed as a published AAR" above), not a source/project dependency.

### `app` module layering

- `domain/model` — plain `@Serializable` data classes (`User`, `Wish`, `WishList`). `WishList` is the single unit of data downloaded/uploaded as a whole; it bundles `friends`, `wishes`, and `promises` (a friend's login → the `Wish` they've promised to gift).
- `data/CompositeRepository` — the app's single entry point for remote data. It lazily wraps a `GoogleDriveRepository<WishList>` from `noserver`, transparently runs a one-time `initialize()` (creates the Drive file if missing) before the first real operation, and exposes `getMyLogin`, `downloadMine`, `download(login)`, `upload`, `addFriend`, `logout`. Note: `upload()` invokes `initializer?.invoke()` *after* the Drive upload call, unlike every other method here which invokes it first — worth double-checking whether that's intentional before touching this file.
- `data/ManualRepository` — SAF-based (`ActivityResultContracts.OpenDocument`/`CreateDocument`) import/export of a `WishList` as JSON, for sharing without Google Drive. Must be constructed with a `ComponentActivity` **before** the activity reaches `CREATED` state, because it registers activity-result launchers.
- `utils/DI` — a minimal manual DI singleton (no Hilt/Koin). `DI.init(activity)` must be called from the activity's `init {}` block (see `MainActivity`) before `super.onCreate`, specifically because `ManualRepository`'s launcher registration requires it. `DI.compositeRepository` and `DI.jsonParser` are lazy singletons.
- `ui/viewmodel/MainViewModel` — the single ViewModel for the whole app. All screen navigation is driven by mutating `_currentScreenState: StateFlow<Screen>`; all `onClickX` handlers are `suspend fun`s that call into `CompositeRepository` and then push a new `Screen` value. There is no per-screen ViewModel — one god ViewModel backs every Composable screen.
- `ui/model/Screen` — sealed hierarchy of navigation states (`WishListScreen`, `UsersScreen`, `AddFriendScreen`, `WishScreen`, `NewWishScreen`, `EditWishScreen`, `DeleteWishScreen`), each carrying whatever data that screen needs (e.g. `WishListScreen(myWishList)`), consumed by `ui/composable/common/NavigationComposable`.
- `ui/composable/{common,element,item,screen}` — Compose UI, split into shared scaffolding (`common`), small reusable pieces (`element`), list-row items (`item`), and full screens (`screen`).

### `noserver` module

- `GoogleDriveRepository<T>` — generic Drive-backed store for a single JSON file named `fileName`. Auth/Drive-client access goes through `GoogleDriveAuthorizationManager.runWithGoogleDrive { drive -> ... }`, which wraps `GoogleDriveAuthenticator`/`GoogleDriveAuthorizer` (Credential Manager + Google Sign-In). All I/O runs on a dedicated single-thread executor context and older Drive calls are bridged to coroutines with `suspendCoroutine`. Several low-level members (`isFileExists`, `getMyFile`, `createFile`) are `@Deprecated` "public but don't call directly" — they exist only because Kotlin requires `reified` inline call sites to see them; treat them as private implementation detail.
- This is effectively the module's entire public API surface today, plus `JsonUtils` (internal JSON (de)serialization helper) and the manifest-declared `NoServerActivity`/`NoServerApplication` used internally for the auth flow.
- An in-progress rewrite of the sharing layer (`protocol/`: `NoServer`, `ShareProtocol`, `SharingManager`, `SigningManager`, `Interactor`, per-backend repositories) was removed from the working tree — it was unused scaffolding that didn't compile (`NoServer.kt` referenced an undefined type `T`, `ShareProtocolImpl`/`SigningManagerImpl` didn't implement their abstract members) and was breaking the build of the whole module, hence `app`, entirely. It's recoverable from git history (commit `a79c874`, "protocol in progress. might not compile") if that rewrite is picked back up — at that point it should probably live in its own Gradle module so its build state can't take down the published SDK again.

## Repository hygiene

- `private/` (gitignored) holds release signing artifacts, built `.aab`/`.apk` files, and plaintext internal planning/privacy-policy notes — never stage or glob-add anything from it.
- `utils/DI.init(activity)` must run in `MainActivity`'s `init {}` block before `super.onCreate`, and `data/ManualRepository` must be constructed before its hosting activity reaches `CREATED` — both because `ActivityResultLauncher` registration has to happen before that lifecycle point. Getting the order wrong fails at runtime with a lifecycle exception, not a compile error.
- No CI is configured (no `.github/workflows`) — nothing currently gates merges on build/test/lint.
