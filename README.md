# authorizer

The test challenge for NDA company

## Explanation

The task describes different cases when we should print a proper message
depending on the state of an account. 

So I decided to implement a state-machine (it's tricky to name the machine finite-state-machine)

Account data structure looks like - {Limit Active Violations History} 
Where Violations are PersistentVector and History are PersistentList
The implementation you could find in account.clj 

Actually Violations is not used for saving data, only for showing when we print to stdout. 
But could be used for future changes. History Record has own Data type (Record) and includes fields like merchant, time and amount.
Could be easily to expand in future to add more information about transactions.
We need the history to have possibility to calculate double transactions and frequent transactions. 

The main definition of the state machine are in the file state.clj. 
Also I have 2 files. First describes conditions - decisions what we should do and second one describes
actions - actual things we do according the decision.

Conditions are just simple functions which returns true or false. 
And could be combined in different orders if business logic would be changed

Under the hood we have really simple things.
For instance, the real change the state - spend action just subtracts without questions or conditions
and doesn't include any checks for account. 
All the state depends only on "external" conditions (defined in conditions.clj).

We easily could add more business rules. Just add new condition. 

We easily could add new operations for example convert currency,
top up the account or show statistic. 

time.clj - a wrapper to handle date and time. 

There are not a configuration file or args to configure the app. 
But we have vars to contain INTERVAL and FREQUENCY. (state.clj)
First one used to configure how far back we look (2 min currently)
and second one to check how many transactions should be done (3 per 2 min currently) 

### Requirements 

The app build and run requires:

- JDK 
- GNU Make
- Bash
- leiningen
- Linux or Macos

### Tested Requirements versions 

 - OpenJDK version 16.0.1
 - GNU Make 3.81
 - Bash 3.2
 - Leiningen 2.9.6
 - MacOS Big Sur 11.2
  
### Installation

Unpack archive and change directory

    $ mkdir authorizer
    $ unzip authorizer.zip -d autjorizer
    $ cd authorizer
    $ export PATH=$PATH:$(pwd)/bin

Important: will write files to ```/usr/local/bin``` folder

### Build

    $ make build

### Tests

    $ make test

### End-2-End tests

    $ make e2e

### Test, Build and run end-2-end test

    $ make

### Usage

    $ authorizer < file 

also possible to pass data through pipe

    $ cat file | authorizer
