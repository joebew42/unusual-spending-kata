# Unusual Spending Kata

[source](https://kata-log.rocks/unusual-spending-kata)

You work at a credit card company and as a value-add they want to start providing alerts to users when their spending in any particular spending is higher than usual.

- A Payment is a simple value object with a price, description, and spending.

- A Category is an enumerable type of a collection of things like “entertainment”, “restaurants”, and “golf”.

- For a given userId, fetch the payments for the current month and the previous month.

- Compare the total amount paid for the month, grouped by spending; filter down to the categories for which the user spent at least 50% more this month than last month.

- Compose an e-mail message to the user that lists the categories for which spending was unusually high, with a subject like “Unusual spending of $1076 detected!” and this body:

```
Hello card user!

We have detected unusually high spending on your card in these categories:

* You spent $148 on groceries
* You spent $928 on travel

Love,

The Credit Card Company
```

# TODO

- [F] Essence: Send a notification with the spending that has the current amount at least the 50% more of the previous one.
    - [R] Find better names for `current` and `previous` variables in `UnusualSpending#evaluate`
    - [R] Maybe rename `UnusualSpending#evaluate` to `UnsusualSpending#evaluateExpenses`
- [F] Essence: Send a notification with a Message that contains the spending that have the current amount at least the 50% more of the previous one.
- [F] Essence: Send a notification with a Message that contains all the categories that have the current amount at least the 50% more of the previous one.
- [F] Essence: Introduce the concept of the User

