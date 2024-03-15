@echo off

if exist .gradle (
    rmdir /s /q .gradle
)

if exist app (
    rmdir /s /q app
)

if exist gradle (
    rmdir /s /q gradle
)

if exist .gitignore (
    del .gitignore
)

if exist build.gradle.kts (
    del build.gradle.kts
)

if exist gradle.properties (
    del gradle.properties
)

if exist gradlew (
    del gradlew
)

if exist gradlew.bat (
    del gradlew.bat
)

if exist local.properties (
    del local.properties
)

if exist settings.gradle.kts (
    del settings.gradle.kts
)