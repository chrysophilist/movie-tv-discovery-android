# 🧪 Unit Test Cases Document

This document describes the unit test scenarios implemented and validated for the **Movie & TV Show Discovery App**. The focus of testing is on **ViewModel logic and state management**, ensuring correct behavior for success and failure scenarios when fetching data from the Watchmode API.

UI rendering and animations (Jetpack Compose, shimmer effects) are intentionally excluded from unit testing, as the primary goal is to validate **business logic, API handling, and state transitions**.

---

## 🎯 Test Scope

- **Layer Tested:** ViewModel (MVVM)
- **Primary Class Under Test:** `HomeViewModel`
- **Reactive Framework:** RxJava / RxKotlin
- **State Holder:** `UiState`
- **Test Framework:** JUnit

The `HomeViewModel` fetches Movies and TV Shows simultaneously using `Singles.zip` and exposes UI state through the `UiState` sealed class.

---

## 🧩 UiState Definition (Reference)

The ViewModel emits the following states:

- `UiState.Loading` – emitted when data fetching starts
- `UiState.Success(data)` – emitted when data is fetched successfully
- `UiState.Error(error)` – emitted when an error occurs

---

## 📋 Test Cases Overview

| Test Case ID | Scenario | Expected Result |
|------------|---------|----------------|
| [TC-01](#-tc-01:-successful-api-response) | Successful API Response | UiState.Success with data |
| [TC-02](tc-02:-movies-api-failure) | Movies API Failure | UiState.Error |
| [TC-03](#-tc-04:-empty-api-response) | Empty API Response | UiState.Success with empty lists |
| [TC-04](#-tc-03:-network-timeout) | Network Timeout | UiState.Error (Timeout) |

---

## ✅ TC-01: Successful API Response

**Objective**  
Verify that the ViewModel emits a success state when both Movies and TV Shows APIs return valid data.

**Input Conditions**
- Movies API returns a non-empty list
- TV Shows API returns a non-empty list
- `Single.zip` completes successfully

**Repository Output**
- `Result.Success(HomeContent)`

**Expected UiState Flow**
- `UiState.Success(HomeContent)`

**Expected Outcome**
- Movies list is populated
- TV Shows list is populated
- No error state is emitted

---

## ❌ TC-02: Movies API Failure

**Objective**  
Ensure that an error state is emitted when the Movies API fails, even if the TV Shows API succeeds.

**Input Conditions**
- Movies API throws an exception (e.g., Bad Request or Unauthorized)
- TV Shows API returns valid data
- `Single.zip` fails due to one source error

**Repository Output**
- `Result.Error(AppError)`

**Expected UiState Flow**
- `UiState.Error(AppError)`

**Expected Outcome**
- No partial success is emitted
- Error is propagated correctly to the UI layer

---

## ⚠️ TC-03: Empty API Response

**Objective**  
Ensure that empty API responses are treated as valid success cases.

**Input Conditions**
- Movies API returns an empty list
- TV Shows API returns an empty list

**Repository Output**
- `Result.Success(HomeContent(movies = emptyList(), tvShows = emptyList()))`

**Expected UiState Flow**
- `UiState.Success(HomeContent)`

**Expected Outcome**
- UI receives a valid success state
- Empty lists are handled without errors or crashes

---

## ⏱️ TC-04: Network Timeout

**Objective**  
Validate that network timeout errors are handled gracefully and mapped correctly to UI state.

**Input Conditions**
- One or both API calls result in a timeout exception

**Repository Output**
- `Result.Error(AppError.Timeout())`

**Expected UiState Flow**
- `UiState.Error(AppError.Timeout)`

**Expected Outcome**
- Timeout error message is available for UI display
- Application does not crash

---

## 📝 Notes

- All test cases focus on **state transitions and business logic**.
- These test scenarios are reused in the **testing walkthrough video**, demonstrating real runtime behavior with the same inputs.

---
