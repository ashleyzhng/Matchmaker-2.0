# Matchmaker 2.0

A console-based matchmaking app written in Java. Users create a profile, get matched with other users by shared interests or distance, and can be paired up for single or double dates at hosted events. Admins approve events, handle reports, and manage accounts.

**Authors:** Ashley Zhong, Joyce Lin, Kathy Wu. Built for ICS4U1.

## Running the program

You need a Java JDK (8 or newer). From the project folder:

```bash
javac *.java
```

```bash
java MatchmakerRunner
```

Run it from the project root so it can find `Accounts.txt`, `event.txt` and the `matches/` folder.

### Sample logins

| Role  | Username       | Password     |
|-------|----------------|--------------|
| Admin | `admin1`       | `adminpass1` |
| User  | `skatergirl16` | `pass123`    |

More accounts are in `Accounts.txt`.

## Features

### Accounts
- Log in, reset your password, or create an account from the main menu. Accounts can also be created in bulk with **Create Account → Load from File** (try `Test.txt`).
- Your account type depends on your age, worked out from your birthday:
  - **Teen**: 13 to 17
  - **Adult**: 18 to 64. Adds occupation, marital status, and a preferred age range.
  - **Senior**: 65 and up. Same as Adult, plus mobility limitations.
- Edit your profile details, verify your email, or delete your account.
- Report another user. Each report becomes an appeal for an admin to review.

### Matching
From the **Match Menu**, users can create a new match or manage their current ones (accept, reject, or complete).

| Match type        | How it works |
|-------------------|--------------|
| Interest Match    | Pairs you with a user who shares your interest tags. |
| Distance Match    | Pairs you with a nearby user. |
| Single Date Match | Pairs two users and picks the best event for them. |
| Double Date Match | Pairs two couples and picks one event for all four. |

### Events
- Users can host an event, choose an existing event, or edit their pending events.
- Events can be **Indoor**, **Outdoor** or **Virtual**, and each type has its own details (room and floor, weather, or platform and meeting ID).
- New events stay pending until an admin approves them.

### Admin tools
- View all users, a single user's info, all appeals, and all events.
- Approve pending events.
- Promote a user to admin.

## Data files

Data is saved when you log out or exit the program.

| File / folder            | Holds |
|--------------------------|-------|
| `Accounts.txt`           | All admin and user accounts. The first line is the account count. |
| `event.txt`              | All events. The first line is the event count. |
| `matches/matches_<username>.txt` | Each user's current matches and match history. |
| `Test.txt`               | A sample accounts file for the **Load from File** option. |

## Code overview

| Area | Classes |
|------|---------|
| Entry point and UI | `MatchmakerRunner`, `Menu`, `EventMenu`, `LoginResults` |
| Core system | `Matchmaker` connects the account, event, and appeal databases. |
| Accounts | `Account` (abstract), `Admin`, `User`, `Teen`, `Adult`, `Senior`, `Tag` |
| Events | `Event` (abstract), `Indoor`, `Outdoor`, `Virtual` |
| Matches | `Match` (abstract), `PersonMatch` → `InterestMatch`, `DistanceMatch`; `EventMatch` → `SingleEventMatch`, `DoubleEventMatch` |
| Storage | `AccountsDatabase`, `EventDatabase`, `AppealDatabase`, `MatchFileManager` |
| Reports | `Appeal` |
| Test drivers | `TestEventMatch`, `TestManualEvent` |
