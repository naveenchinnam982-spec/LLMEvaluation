# Task List - Fix Startup Crash and Compatibility

- [x] **Phase 1: Application Lifecycle Fix**
    - [x] Create `LLMEvaluationApplication.kt`.
    - [x] Register in `AndroidManifest.xml`.
- [x] **Phase 2: Navigation Fix**
    - [x] Update `MainActivity.kt` with robust `NavController` lookup.
- [x] **Phase 3: Python Robustness**
    - [x] Simplify `metrics.py` (remove native deps).
    - [x] Update `chaquopy_config.gradle`.
    - [x] Add `PyException` handling in `PythonBridge.kt`.
- [x] **Phase 4: Verification**
    - [x] Successful Gradle Sync.
    - [x] Successful `assembleDebug`.
