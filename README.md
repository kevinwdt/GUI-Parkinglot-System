# YorkU Parking System

**A desktop parking management application built with Java Swing.**

YorkU Parking System is a university-themed parking management prototype that brings together account registration, parking-space management, reservations, simulated payments, and administrative controls in one graphical interface. The project emphasizes object-oriented design, role-based workflows, and maintainable Java components.

## Highlights

- **Desktop GUI:** Navigate between login, registration, parking, booking, payment, and administration screens.
- **Multiple user roles:** Supports students, faculty members, non-faculty members, visitors, admins, and superadmins.
- **Parking reservations:** Select an available space, enter a license plate and booking times, and request changes or cancellation.
- **Parking availability:** View parking lots and spaces using status data stored in CSV files.
- **Administrative controls:** Approve/remove users, inspect payment records, enable/disable lots or spaces, add spaces, and generate admin accounts (superadmin).
- **Flexible payment options:** Demonstrates credit-card, debit-card, and mobile-payment strategies.
- **Lightweight persistence:** Uses local CSV files rather than a database server.

> **Project status:** Academic prototype / demonstration application. Payment methods are simulated; this is not an integrated payment gateway or a production parking service.

## Tech Stack

| Area | Technology |
| --- | --- |
| Programming language | Java |
| GUI | Java Swing / AWT |
| Persistence | CSV files |
| Design approaches | Object-oriented programming, Factory, Strategy, Singleton, Proxy |
| Dependencies | Java standard library; bundled `javacsv.jar` |

## Application Flow

```text
                Login / Registration
                        |
                   Main Menu
                        |
          +-------------+-------------+
          |             |             |
     View Spaces   Reserve / Edit   Pay for Parking
          |             |             |
     Parking CSV   Booking CSV    Payment Strategy
                        |
                  CSV Records

    Admin / Superadmin -> Approvals, lots, spaces, records
```

## Getting Started

### Requirements

- **JDK 17 or newer** (the source uses modern switch expressions).
- A desktop environment that can display Java Swing windows.
- A terminal, or a Java IDE such as IntelliJ IDEA, Eclipse, or VS Code.

### 1. Clone

```bash
git clone https://github.com/kevinwdt/GUI-Parkinglot-System.git
cd GUI-Parkinglot-System/"Parkinglot code"/code
```

**Important:** Run the application from the `code` directory. The program reads relative paths such as `data/users.csv` and `data/parking_spaces.csv`.

### 2. Compile

On **macOS / Linux**:

```bash
mkdir -p out
javac -cp "lib/javacsv.jar" -d out $(find src -name "*.java")
```

On **Windows PowerShell**:

```powershell
New-Item -ItemType Directory -Force out | Out-Null
$files = Get-ChildItem -Recurse src -Filter *.java | ForEach-Object FullName
javac -cp "lib/javacsv.jar" -d out $files
```

### 3. Launch

On **macOS / Linux**:

```bash
java -cp "out:lib/javacsv.jar" UserLoginGUI
```

On **Windows PowerShell**:

```powershell
java -cp "out;lib/javacsv.jar" UserLoginGUI
```

The application entry point is `src/UserLoginGUI.java`, which opens the Swing login window. The compile/run steps above reflect the repository structure and have not been executed as an end-to-end validation.

## How to Use

1. Start the application and **log in** with an existing local demo account, or select **Register**.
2. On the main menu, choose **Check parking spaces** to inspect availability.
3. Select **Reserve/Modify Parking** to enter a space, license plate, and start/end time in `HH:mm` format.
4. Use **Paying for parking** to demonstrate the available payment-method options.
5. If you have an administrator account, open **Admin Page** for user and parking management.

The repository includes `login_instructions.txt` and sample CSV account data for local demonstration. **Do not reuse demo credentials for a real service.**

## Core Business Rules and Workflows

- **Booking validation:** Required fields are checked, times use the `HH:mm` format, and the end time must follow the start time.
- **Reservation state:** Booking operations read and update records in `data/parking_spaces.csv`.
- **Deposit messaging:** The booking interface currently presents a **$15 deposit** notice. This is prototype behavior, not a real charge.
- **Role-based fee examples:** The payment screen displays sample rates: student $5, faculty $8, non-faculty $10, and visitor $15.
- **Administration:** Admin operations manage local user, payment, lot, and space records; superadmins can generate additional admin accounts.

These are behaviors represented in the application code, not guarantees of production-grade reservation concurrency, refunds, or financial processing.

## Design and Code Organization

The source demonstrates several object-oriented patterns:

| Pattern | Example | Purpose |
| --- | --- | --- |
| Factory | `user/UserFactory.java` | Constructs different user types |
| Strategy | `utils/PaymentStrategy.java` and implementations | Chooses a simulated payment mechanism |
| Singleton | `models/Manager.java` | Provides a shared manager instance |
| Proxy | `models/AdminAccessProxy.java` | Wraps access to administrative operations |

### Repository Layout

```text
GUI-Parkinglot-System/
├── README.md
├── Report.pdf
├── login_instructions.txt
└── Parkinglot code/
    └── code/
        ├── data/
        │   ├── users.csv
        │   ├── approved_users.csv
        │   ├── parking_data.csv
        │   ├── parking_lots.csv
        │   ├── parking_spaces.csv
        │   └── payments.csv
        ├── lib/
        │   └── javacsv.jar
        └── src/
            ├── UserLoginGUI.java
            ├── gui/        # Swing windows
            ├── models/     # Manager, payment and access-control models
            ├── user/       # User types, factory and account handling
            └── utils/      # CSV helpers and payment strategies
```

See [Report.pdf](Report.pdf) for the accompanying project report.

## Limitations and Possible Improvements

This repository is a **learning project** built around local CSV files and simulated actions. Before real-world deployment, it would need stronger authentication/password handling, a transactional database, concurrency-safe reservations, real payment integrations, improved validation and tests, and appropriate privacy and security controls.

Additional future improvements could include automated tests, a build tool (Maven/Gradle), visual screenshots, and persistent reporting dashboards.

## Maintainer

[kevinwdt](https://github.com/kevinwdt)
