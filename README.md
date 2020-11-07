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

- [F] Essence: Introduce the concept of Payments
    - Do we need a `Payments` class to delegate methods like `groupBySpending`?
- [R] Should we have to introduce the class `Spendings` to describe Payments grouped by spending?
    - Then the private method `UnsualSpending#findSpending` should be moved to that class
- [F] Essence: Send a notification with a Message that contains all the spending with an amount at least the 50% more of the previous one.
[F] Essence: Introduce the concept of the User

