# KCF MVP Notes

## Implemented in this pass
- Added reusable `:core` KMP module with `storage`, `theme`, and `settings` packages.
- Wired `composeApp` to consume `:core` and launch `KCFSettingsScreen` as MVP.
- Added KSafe initialization in Android, iOS, and JVM entry points.
- Added ThemeManager + KCF wrapper + dynamic color support stubs.
- Added reusable grouped settings list + settings card + dialogs.

## Deferred for next iteration
- Verify and harden exact KSafe API calls (`getDirect`/`putDirect`) against runtime edge cases.
- Refine app-level API to support `KCFThemeGroup(model)` in external custom screen composition exactly as desired.
- Add polished reusable widgets (`KSwitch`, button styles) and richer spacing/animation tokens.
- Add tests for settings persistence restoration and dialog cancel rollback behavior.
- Add explicit docs/snippets for app integration under 10 lines.

