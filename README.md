# Matchmaker 2.0

A matchmaking app written in Java, with a console version and a website. Users create a profile, get matched with other users by shared interests or distance, and can be paired up for single or double dates at hosted events. Admins approve events, handle reports, and manage accounts.

**Authors:** Ashley Zhong, Joyce Lin, Kathy Wu. Built for ICS4U1.

## Running the program

You need a Java JDK (8 or newer, or 11 or newer for the website). From the project folder:

```bash
javac *.java
```

```bash
java MatchmakerRunner
```

Run it from the project root so it can find `Accounts.txt`, `event.txt` and the `matches/` folder.

## Running the website

After compiling, start the website instead of the console menus:

```bash
java MatchmakerWeb
```

Then open **http://localhost:8080** in your browser. Press `Ctrl+C` in the terminal to stop it. To use a different port, add it at the end, for example `java MatchmakerWeb 9090`.

The website uses the same accounts, events and matches as the console version, and adds:
- Five colour themes (Blush, Powder Blue, Sage, Lavender and Midnight) and nine avatars, picked in **Settings**.
- Profile pages that show the differences between Teen, Adult and Senior accounts.
- Admin pages for users (filter by age group), reports, event review and all events.

### Sample logins

| Role  | Username       | Password     |
|-------|----------------|--------------|
| Admin | `admin1`       | `adminpass1` |
| Teen  | `skatergirl16` | `pass123`    |
| Adult | `teacheremma`  | `emma123`    |
| Senior | `retiredbob`  | `bobpass`    |
| Teen  | `musicfan18` (Noah) | `song888` |

More accounts are in `Accounts.txt`. The newer sample accounts (for example `jakeskates`, `mayaswims`, `danielfit`, `priyareads`, `margaretknits` and `haroldchess`) all use the password `pass123`.

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

Data is saved when you log out or exit the program. The website saves after every change.

| File / folder            | Holds |
|--------------------------|-------|
| `Accounts.txt`           | All admin and user accounts. The first line is the account count. |
| `event.txt`              | All events. The first line is the event count. |
| `matches/matches_<username>.txt` | Each user's current matches and match history. |
| `Test.txt`               | A sample accounts file for the **Load from File** option. |
| `appeals.txt`            | Reports sent to admins (website). |
| `preferences.txt`        | Each user's theme and avatar (website). |

## Code overview

| Area | Classes |
|------|---------|
| Entry point and UI | `MatchmakerRunner`, `Menu`, `EventMenu`, `LoginResults` |
| Website | `MatchmakerWeb` (web server and actions), `UserPages`, `AdminPages`, `Html`, `Preferences` |
| Core system | `Matchmaker` connects the account, event, and appeal databases. |
| Accounts | `Account` (abstract), `Admin`, `User`, `Teen`, `Adult`, `Senior`, `Tag` |
| Events | `Event` (abstract), `Indoor`, `Outdoor`, `Virtual` |
| Matches | `Match` (abstract), `PersonMatch` → `InterestMatch`, `DistanceMatch`; `EventMatch` → `SingleEventMatch`, `DoubleEventMatch` |
| Storage | `AccountsDatabase`, `EventDatabase`, `AppealDatabase`, `MatchFileManager` |
| Reports | `Appeal` |
| Test drivers | `TestEventMatch`, `TestManualEvent` |

The website's styles are in `web/style.css`, the characters and avatars in `images/`, and the fonts (Cormorant Garamond, DM Sans and Caveat, free under the SIL Open Font License) in `fonts/`.
