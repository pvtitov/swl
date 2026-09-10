# noserver

A small Android library ("serverless" data sync/sharing SDK) providing Google-Drive-backed
storage for a single JSON file per user, plus a manual export/import fallback. See the repo
root `CLAUDE.md` for the architecture; this file covers how to *consume* the published
artifact, from this repo or from another app.

`noserver` is published as `com.github.pvtitov:noserver:1.0.0` (a real AAR + POM, not a
source/project dependency) via the Gradle `maven-publish` plugin, configured in
`noserver/build.gradle.kts`.

## Consuming it

### From this repo (already set up)

`app` resolves `noserver` from the in-repo Maven directory `local-repo/` at the project root,
declared as a repository in the root `settings.gradle`. That directory is committed to git, so
a clean checkout of this repo builds without any extra steps. If you change `noserver`, you
must republish before `app` sees the change:

```
./gradlew :noserver:publishReleasePublicationToLocalRepoRepository
```

Bump `version` in the `publishing {}` block in `noserver/build.gradle.kts` first (Gradle/Maven
otherwise happily reuses a stale resolution of the same version number), update the version in
`app/build.gradle` to match, then rebuild `app`.

### Consumer requirements (any app, any distribution channel)

Validated by building a separate throwaway Android app against the published artifact.
Whichever way you consume `noserver` (`local-repo`, `mavenLocal`, or JitPack later), the same
thing applies, because it comes from the AAR's own metadata, not from how it was resolved:

- `android.useAndroidX=true` must be set in the consuming app's `gradle.properties` —
  `noserver` transitively pulls in AndroidX (`androidx.core`, `androidx.credentials`,
  `androidx.lifecycle`, etc. via Credential Manager/Drive auth). Virtually every modern Android
  project already has this, but a brand-new project scaffolded without it will fail at
  `:app:checkDebugAarMetadata` with a clear error telling you to set it. This isn't something
  `noserver` can opt consumers out of — `useAndroidX` is a project-wide flag, not a per-library
  one, so any app depending on any AndroidX-based library needs it regardless.

`noserver` does **not** require core library desugaring in consumers — verified by inspecting
the published AAR's `META-INF/com/android/build/gradle/aar-metadata.properties`
(`coreLibraryDesugaringEnabled=false`). It was previously enabled in `noserver`'s own build as
unused template boilerplate (no source in the module actually calls a desugarable API) and has
been removed.

You do **not** need to set `GOOGLE_WEB_CLIENT_ID` anywhere in the consumer — confirmed
empirically (built and compiled a consumer with no such key defined). It's only read at
`noserver`'s own build/publish time and gets baked into the published AAR's compiled
`BuildConfig`.

### From a different app, on this machine (validated)

The `maven-publish` plugin gives every publication a free `publishToMavenLocal` task that
writes to `~/.m2/repository` — a location any Gradle project on the machine can see, no extra
config needed on the `noserver` side:

```
./gradlew :noserver:publishToMavenLocal
```

In the other app's `settings.gradle`(`.kts`), add `mavenLocal()` to
`dependencyResolutionManagement.repositories`:

```kotlin
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        mavenLocal()
    }
}
```

Then add the dependency as usual in that app's module:

```kotlin
implementation("com.github.pvtitov:noserver:1.0.0")
```

Caveats: this only works on machines where someone has actually run `publishToMavenLocal`
locally — it is **not** reproducible for a teammate or CI who hasn't. As with `local-repo`,
bump the version (or pass `--refresh-dependencies`) after republishing, or a consumer may keep
resolving a cached copy of the old `1.0.0`.

### From anywhere (CI, other machines, other people) — not yet set up

For a version usable outside this machine, extract `noserver` into its own git repository
(own history, own release cadence — decoupled from the wishlist app) and publish it somewhere
reachable without a local path. The lowest-effort option for a personal/public project is
**JitPack**:

1. Push the extracted `noserver` repo to a public GitHub repo.
2. Tag a release, e.g. `git tag 1.0.0 && git push --tags`. JitPack builds the AAR on demand
   from that tag the first time someone requests it — no separate publish step on your side.
3. Consumers add the JitPack repository and the dependency:

   ```kotlin
   // settings.gradle.kts
   dependencyResolutionManagement {
       repositories {
           maven { url = uri("https://jitpack.io") }
       }
   }
   ```

   ```kotlin
   // app/build.gradle.kts
   implementation("com.github.<your-github-username>:noserver:1.0.0")
   ```

If the repo needs to stay private instead, use GitHub Packages (requires a Personal Access
Token configured in consumers' `gradle.properties` or environment) rather than JitPack, which
only builds from public repos.
