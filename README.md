# Expense Tracker (Java)

A simple console application to record daily expenses and see where the money goes.

## Features
- Add an expense with amount, category, date and note
- View all expenses in a table
- Delete an expense
- Show total spent
- Show total by category
- Show the highest expense
- Data is saved in expenses.txt and loaded again on the next run

## Java concepts used
- Classes and objects (`Expense`)
- Encapsulation (private fields with getters)
- `ArrayList to store the expenses
- `HashMap to calculate the total for each category
- File handling (FileReader, FileWriter, BufferedReader, PrintWriter)
- Exception handling (try-catch) for wrong input
- `LocalDate for dates

## How to run
```
javac Expense.java ExpenseTracker.java
java ExpenseTracker
```

## What I learned
how the HashMap total works, or a bug you fixed.

## Possible improvements
- Filter expenses by month
- Edit an existing expense
- Store the data in a database using JDBC
