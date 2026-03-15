# CleanFiles — File Manager & Cleaner

A powerful file manager and storage cleaner for Android.

## Features

### File Browser
- Grid/list toggle view with sort by name, date, size, type
- Breadcrumb navigation with folder sidebar on tablets
- Multi-select for batch operations (copy, move, delete, zip, share)
- New folder creation, rename, paste

### Storage Analysis
- Visual pie chart breakdown (Compose Canvas with animation)
- Category-by-category storage usage
- Available vs used space indicator

### Cleaning Tools
- **Junk Cleaner**: Scan and clean app cache, temp files, old APKs, empty folders, thumbnails
- **Duplicate Finder**: MD5 hash-based duplicate detection with preview, keep newest/oldest
- **WhatsApp Cleaner**: Scan WhatsApp media across 7 categories (Images, Videos, Voice Notes, Documents, Stickers, GIFs, Status)

### File Management
- **Category Views**: Images, Videos, Audio, Documents, APKs, Downloads
- **Trash Bin**: 30-day recovery with restore and permanent delete
- **Search**: File name search with type and size filters
- **Quick Actions**: Copy, move, rename, delete, compress (zip), extract, share

### User Experience
- Onboarding screen (3-slide first launch)
- Storage tips on home screen (cache warnings, APK alerts)
- Responsive 2x2 quick action grid
- Material3 + dynamic colors, Light/Dark mode
- Responsive tablet layouts (folder sidebar, adaptive grids)
- In-app language selector (14 languages)
- AdMob banner ad

### Internationalization
14 languages: English, Spanish, French, German, Hindi, Arabic, Malay, Marathi, Tamil, Malayalam, Telugu, Kannada, Gujarati, Punjabi

## Tech Stack
Kotlin, Jetpack Compose, Material3, Hilt, Room, Coil, DataStore, AdMob

**Min SDK:** 26 | **Target SDK:** 35

## Building
```bash
./gradlew assembleDebug
```
