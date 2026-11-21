# Architecture

## Goals
1. One source of truth for navigation state, in Kotlin.
2. Use the *system* bar so iOS 26 draws Liquid Glass itself (no imitation shader).
3. Zero Swift code for library users.

## Public API (explicit API mode)
- `GlassTab`: immutable tab description (id, title, SF Symbol, fallback vector).
- `GlassScaffold`: hosts content and the tab bar, passes bottom `PaddingValues` to content.

Everything else is `internal`.

## Platform bridge
`internal expect fun GlassTabBar(...)`
- **androidMain**: Material 3 `NavigationBar`.
- **iosMain**: a stock `UITabBar` embedded with Compose `UIKitView`. A `UITabBarDelegateProtocol` object forwards
  `didSelectItem` to Compose as the tab id. The selected item is set from Compose state in `update`.
  The delegate is kept alive with `remember` because `UITabBar.delegate` is weak.

## Data flow
Compose state (selected id) -> `GlassScaffold` -> `UITabBar.selectedItem`
User tap -> delegate -> `onTabSelected(id)` -> your state. Nothing is stored in the library.

## Known open questions
- Does a `UIKitView`-hosted `UITabBar` show the full iOS 26 floating/glass look and blur content behind it? Needs device test.
- Bar height and safe-area handling on iOS 26.
- Native `UINavigationBar` bridge (planned, same pattern).
