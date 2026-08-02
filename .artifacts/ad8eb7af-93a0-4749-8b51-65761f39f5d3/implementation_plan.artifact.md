# Implementation Plan - Fix Startup Crash and Compatibility

The application is experiencing a crash on startup on some devices. This plan addresses potential causes including incorrect NavController initialization, Python library dependencies, and improper background thread handling during startup.

## User Review Required

> [!IMPORTANT]
> This update simplifies the Python metrics implementation to remove heavy dependencies (`numpy`, `nltk`) that were causing native loading issues. This ensures the app runs smoothly on all devices while still providing accurate (though slightly simplified) evaluation scores.

## Proposed Changes

### Application Lifecycle

#### [NEW] [LLMEvaluationApplication.kt](file:///C:/Users/navee/AndroidStudioProjects/LLMEvaluation/app/src/main/java/project/llmevaluation/LLMEvaluationApplication.kt)
- Create a custom `Application` class to initialize Chaquopy once at the application level.

#### [MODIFY] [AndroidManifest.xml](file:///C:/Users/navee/AndroidStudioProjects/LLMEvaluation/app/src/main/AndroidManifest.xml)
- Register `LLMEvaluationApplication` in the `<application>` tag.

### UI & Navigation

#### [MODIFY] [MainActivity.kt](file:///C:/Users/navee/AndroidStudioProjects/LLMEvaluation/app/src/main/java/project/llmevaluation/MainActivity.kt)
- Fix the `NavController` initialization by using `supportFragmentManager` to find the `NavHostFragment`. This prevents a common crash when using `FragmentContainerView`.

### Python Logic (Robustness)

#### [MODIFY] [metrics.py](file:///C:/Users/navee/AndroidStudioProjects/LLMEvaluation/app/src/main/python/metrics.py)
- Remove `numpy` and `nltk` imports.
- Implement tokenization using native Python regex.
- Implement simplified BLEU, ROUGE, and Perplexity calculations using pure Python.

#### [MODIFY] [chaquopy_config.gradle](file:///C:/Users/navee/AndroidStudioProjects/LLMEvaluation/app/chaquopy_config.gradle)
- Remove `nltk` and `rouge-score` from pip requirements to avoid native binary alignment issues.

#### [MODIFY] [PythonBridge.kt](file:///C:/Users/navee/AndroidStudioProjects/LLMEvaluation/app/src/main/java/project/llmevaluation/python/PythonBridge.kt)
- Add comprehensive `PyException` handling to prevent Python-level errors from crashing the Android app.

## Verification Plan

### Automated Tests
- Run `gradlew assembleDebug` to verify the build.

### Manual Verification
1. **Startup**: Verify the app launches to the Splash screen and transitions to Home without crashing.
2. **Evaluation**: Run a multi-model comparison and confirm metrics are calculated and displayed correctly.
