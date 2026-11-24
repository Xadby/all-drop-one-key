# Publishing checklist

Short summary (Modrinth/CurseForge "summary" field, max ~100 chars):
- EN: Drop your entire inventory with one key (U by default). Client-side.
- RU: Выбросить весь инвентарь одной клавишей (по умолчанию U). Только клиент.

Project settings:
- Loader: Fabric · Game version: 26.3 · Environment: client required, server unsupported
- Dependency: Fabric API (required)
- Categories: Utility, Storage
- License: MIT · Source: https://github.com/Xadby/all-drop-one-key
- Icon: `src/main/resources/assets/adok/icon.png`
- Long description: `description.md`

Releasing a new version:
1. Bump `version` in `gradle.properties` (optional; the tag wins in CI).
2. `git tag v1.0.1 && git push origin v1.0.1`
3. The `release` workflow builds the jar and attaches it to a GitHub Release.
4. Upload that jar to Modrinth and CurseForge.
