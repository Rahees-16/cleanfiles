# CleanFiles — File Manager & Cleaner

A powerful file manager and storage cleaner for Android, built with Kotlin and Jetpack Compose.

## Features

- **File Browser** — Grid/list view, sort by name/date/size/type, breadcrumb navigation, multi-select
- **Storage Analysis** — Visual pie chart breakdown of storage usage by category
- **Junk Cleaner** — Scan and clean: app cache, temp files, old APKs, empty folders, thumbnails
- **Duplicate Finder** — Find duplicate files using MD5 hashing with preview before delete
- **Category View** — Browse by type: Images, Videos, Audio, Documents, APKs, Downloads
- **File Operations** — Copy, move, rename, delete, compress (zip), extract, share
- **Search** — File name search with type and size filters
- **Favorites & Recent** — Bookmark folders and access recent files quickly

## Tech Stack

- **Language:** Kotlin
- **UI:** Jetpack Compose + Material3 (Material You dynamic colors)
- **Architecture:** MVVM (ViewModel + Repository)
- **DI:** Hilt
- **Database:** Room
- **Images:** Coil (thumbnails)
- **Charts:** Custom Compose Canvas pie chart
- **Settings:** DataStore Preferences
- **Min SDK:** 26 | **Target SDK:** 35

## Building

```bash
./gradlew assembleDebug
```

## Screenshots

_Coming soon_

## License

All rights reserved.
