# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project overview

"Simple Wish List" is an Android app (Kotlin, Jetpack Compose) that lets a user maintain a wish list and share it with friends without a dedicated backend server. Sharing is currently implemented via Google Drive (each user's wish list is a JSON file in their own Drive; friends are granted read permission on it) and via manual JSON file export/import as a fallback. Root project name is `Simple Wish List`; the only Gradle module is `app` — the Drive/sharing SDK (`noserver`) was extracted into its own repository (see "`noserver` is now an external dependency" below).

## Build system

Gradle with Groovy DSL. AGP 8.12.3, Kotlin 2.2.21, compileSdk/targetSdk 36, minSdk 23, JVM target 17.

Common commands (run from repo root):

```
./gradlew assembleDebug              # build the app module
./gradlew build                      # build everything
./gradlew test                       # run all JVM unit tests
./gradlew :app:testDebugUnitTest     # run app module unit tests only
./gradlew test --tests "*.ClassName"                 # run a single test class
./gradlew test --tests "*.ClassName.methodName"      # run a single test method
./gradlew connectedAndroidTest       # run instrumented tests (needs a device/emulator)
./gradlew lint                       # Android lint
```

There is currently no meaningful unit test coverage in `app` — the only file under `src/androidTest` is the stock Android-Studio-generated `ExampleInstrumentedTest` stub. `app`'s test dependencies (JUnit4, Mockito, MockK, mockito-kotlin, kotlinx-coroutines-test) are wired up in `build.gradle` but unused so far. Treat `./gradlew test` as a build-health check, not a correctness signal, until real tests exist. (`noserver` does have real unit test coverage now — see its own repo.)

No ktlint/detekt/editorconfig is configured; style follows plain Android Studio Kotlin defaults.

### `noserver` is now an external dependency — published tag `1.0.0` is broken

`noserver` used to be a module in this repo (`implementation project(':noserver')`, later `implementation 'com.github.pvtitov:noserver:1.0.0'` resolved from an in-repo `local-repo/`). It has since been extracted to its own standalone repository at `../noserver` (sibling directory, pushed to GitHub as `pvtitov/noserver`) so it can be a real, independently-versioned SDK shared across apps rather than a copy embedded in this monorepo.

`app/build.gradle` depends on `implementation 'com.github.pvtitov:noserver:1.0.0'`, resolved via `settings.gradle`'s `https://jitpack.io` repository. **Tag `1.0.0`'s JitPack build fails** (it tried to read a `GOOGLE_WEB_CLIENT_ID` out of `noserver`'s own `local.properties` at build time, which doesn't exist on JitPack's build box), so `./gradlew :app:assembleDebug` will not resolve the dependency until `../noserver` publishes a fixed tag and this repo's coordinate is bumped to it. See that repo's `README.md`/git history for the fix. Until a new tag is published, to build `app` locally you have two options: (a) wait for the new tag, or (b) temporarily point `settings.gradle` back at a local Maven repo (`mavenLocal()`, after running `./gradlew publishToMavenLocal` inside `../noserver`) as a stopgap.

`noserver` no longer bakes a `GOOGLE_WEB_CLIENT_ID` into its own `BuildConfig` at build time — an OAuth web client ID is tied to a specific app's package name and signing certificate, so a single ID compiled into the published AAR could never work across different consuming apps. Instead, `app` reads its own `GOOGLE_WEB_CLIENT_ID` from its own `local.properties` (via a `buildConfigField` in `app/build.gradle`) and passes it to `com.github.pvtitov.noserver.NoServer.configure(...)` once at startup — currently from `utils/DI.init(activity)`, alongside `ManualRepository` construction, before any `GoogleDriveRepository` use.

## Architecture

One Gradle module:

- **`app`** (`com.github.pvtitov.simplewishlist`) — the UI and app-level domain logic, built with Jetpack Compose and a single shared `MainViewModel` (no navigation library; screens are modeled as a sealed `Screen` state and switched in `NavigationComposable`). Depends on the external `noserver` SDK (see above) for Google-Drive-backed data sync.

### `app` module layering

- `domain/model` — plain `@Serializable` data classes (`User`, `Wish`, `WishList`). `WishList` is the single unit of data downloaded/uploaded as a whole; it bundles `friends`, `wishes`, and `promises` (a friend's login → the `Wish` they've promised to gift).
- `data/CompositeRepository` — the app's single entry point for remote data. It lazily wraps a `GoogleDriveRepository<WishList>` from `noserver`, transparently runs a one-time `initialize()` (creates the Drive file if missing) before the first real operation, and exposes `getMyLogin`, `downloadMine`, `download(login)`, `upload`, `addFriend`, `logout`. Note: `upload()` invokes `initializer?.invoke()` *after* the Drive upload call, unlike every other method here which invokes it first — worth double-checking whether that's intentional before touching this file.
- `data/ManualRepository` — SAF-based (`ActivityResultContracts.OpenDocument`/`CreateDocument`) import/export of a `WishList` as JSON, for sharing without Google Drive. Must be constructed with a `ComponentActivity` **before** the activity reaches `CREATED` state, because it registers activity-result launchers.
- `utils/DI` — a minimal manual DI singleton (no Hilt/Koin). `DI.init(activity)` must be called from the activity's `init {}` block (see `MainActivity`) before `super.onCreate`, specifically because `ManualRepository`'s launcher registration requires it. `DI.compositeRepository` and `DI.jsonParser` are lazy singletons.
- `ui/viewmodel/MainViewModel` — the single ViewModel for the whole app. All screen navigation is driven by mutating `_currentScreenState: StateFlow<Screen>`; all `onClickX` handlers are `suspend fun`s that call into `CompositeRepository` and then push a new `Screen` value. There is no per-screen ViewModel — one god ViewModel backs every Composable screen.
- `ui/model/Screen` — sealed hierarchy of navigation states (`WishListScreen`, `UsersScreen`, `AddFriendScreen`, `WishScreen`, `NewWishScreen`, `EditWishScreen`, `DeleteWishScreen`), each carrying whatever data that screen needs (e.g. `WishListScreen(myWishList)`), consumed by `ui/composable/common/NavigationComposable`.
- `ui/composable/{common,element,item,screen}` — Compose UI, split into shared scaffolding (`common`), small reusable pieces (`element`), list-row items (`item`), and full screens (`screen`).

### `noserver` SDK (external — lives at `../noserver`)

`NoServer.configure(googleWebClientId)` — must be called once (see `utils/DI.init` above) with this app's own OAuth web client ID before any Drive access. `GoogleDriveRepository<T>` — generic Drive-backed store for a single JSON file named `fileName`. Auth/Drive-client access goes through `GoogleDriveAuthorizationManager.runWithGoogleDrive { drive -> ... }`, which wraps `GoogleDriveAuthenticator`/`GoogleDriveAuthorizer` (Credential Manager + Google Sign-In). All I/O runs on a dedicated single-thread executor context and older Drive calls are bridged to coroutines with `suspendCoroutine`. This is effectively its entire public API surface, plus `JsonUtils` (internal JSON (de)serialization helper) and the manifest-declared `NoServerActivity`/`NoServerApplication` used internally for the auth flow. Full details, consumer requirements, and unit tests now live in that repo, not here — see its `README.md`.

An earlier in-progress rewrite of the sharing layer (`protocol/`: `NoServer`, `ShareProtocol`, `SharingManager`, `SigningManager`, `Interactor`, per-backend repositories) was scaffolding that didn't compile and was deleted before extraction (commit `a79c874` in this repo's history, "protocol in progress. might not compile", has the last copy if that rewrite is ever picked back up).

## Repository hygiene

- `private/` (gitignored) holds release signing artifacts, built `.aab`/`.apk` files, and plaintext internal planning/privacy-policy notes — never stage or glob-add anything from it.
- `utils/DI.init(activity)` must run in `MainActivity`'s `init {}` block before `super.onCreate`, and `data/ManualRepository` must be constructed before its hosting activity reaches `CREATED` — both because `ActivityResultLauncher` registration has to happen before that lifecycle point. Getting the order wrong fails at runtime with a lifecycle exception, not a compile error.
- No CI is configured (no `.github/workflows`) — nothing currently gates merges on build/test/lint.
