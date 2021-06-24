## The task definition

Given a file called operations that contains many lines describing operations in json format :

```
$ cat operations
{"account": {"active-card": true, "available-limit": 100}}
{"transaction": {"merchant": "Burger King", "amount": 20, "time": "2019-02-
13T10:00:00.000Z"}}
{"transaction": {"merchant": "Habbib's", "amount": 90, "time": "2019-02-
13T11:00:00.000Z"}}
{"transaction": {"merchant": "McDonald's", "amount": 30, "time": "2019-02-
13T12:00:00.000Z"}}
```
The application should be able to receive the file content through stdin , and for each processed operation return an output equivalent to the business rules:

The program should handle two kinds of operations, deciding on which one execute according to the line that is being processed:
1. Account creation
2. Transaction authorization in the account
   For the sake of simplicity, you can assume the following:
   All monetary values are positive integers using a currency without cents; The transactions will arrive at the Authorizer in chronological order.

### Creating account

Creating an account successfully
Creating an account with an inactive card ( active-card: false ) and an available limit of 750 ( available-limit: 750 ):

``` 
# Input
    {"account": {"active-card": false, "available-limit": 750}}
# Output
    {"account": {"active-card": false, "available-limit": 750}, "violations":
[]}
```

Creating an account that violates the Authorizer logic
Given there is an account with an active card ( active-card: true ) and
the available limit of 175 ( available-limit: 175 ), tries to create another account:

``` 
# Input
    {"account": {"active-card": true, "available-limit": 175}}
    {"account": {"active-card": true, "available-limit": 350}}
# Output
    {"account": {"active-card": true, "available-limit": 175}, "violations":
[]}
    {"account": {"active-card": true, "available-limit": 175}, "violations":
["account-already-initialized"]}
```

### Transaction authorization
Input: Tries to authorize a transaction for a particular merchant , amount and time given the created account's state and last authorized transactions.

Output: The account's current state with any business logic violations. If in the operation processing does not happen any violation, the field violations should return an empty vector [] .

### Business rules
You should implement the following rules, keeping in mind that new rules will appear in the
future:
1. No transaction should be accepted without a properly initialized account: account-not- initialized
2. No transaction should be accepted when the card is not active: card-not-active
3. The transaction amount should not exceed the available limit: insufficient-limit There should not be more than 3 transactions on a 2-minute interval: high-frequency- small-interval
4. There should not be more than 1 similar transactions (same amount and merchant ) in a 2 minutes interval: doubled-transaction

### State
The program should not rely on any external database, and the application's internal state should be handled by an explicit in-memory structure. The application state needs to be reset at the application start.
Authorizer operations that had violations should not be saved in the application's internal state.

### Error handling
Please assume that input parsing errors will not happen.
We will not evaluate your submission against input that contains errors, is bad formatted, or that breaks the contract.
Violations of the business rules are not considered errors as they are expected to happen and should be listed in
the output violations field as described on the output schema in the examples. That means that the program execution should continue normally after any kind of violation.

