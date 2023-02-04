# IronBank :bank:

![Logo IronBank](/images/ironBank.jpg "Logo IronBank")


IronBank is a full backend software to manage a Bank System.
This software is developed in Java 17, with Maven and SpringBoot 3.0.1 framework.
The database is MySql database.

The application runs in port 8080 as default, but you can change it in the `application.yaml` file.
You can manage some settings in file `settings/Settings.java`

In the first Run of Applicaction, set `ddl-auto: create` and `profile: active: load-data`. Later, change to `ddl-auto: validate` and erase the load-data profile.

Show-sql is "false" for keep clean the Run Console, `show-sql: false` 

For work with the application, you need to make requests to the endpoints. These endpoints can be Public or Private. Endpoints have CRUD methods to manage all system. The security of Endpoints is in `SecurityConfig.java`

In IronBank you can register as AccountHolder or ThirdParty users. You have a first Admin to create another Admins. To manage security, the users must be logged in by `BasicAuth`.


The AccountHolders are the principal clients of the Bank and they can create accounts, make payments with credit cards, make transfers to other accounts, deposit and withdraw money from accounts, and have savings accounts to increase their balances.
The ThirdPartyUsers are the secondary clients. They can be registered in the system or can work without registration.
Account Numbers and Secret Keys are Auto Generated.


Every money movement in accounts is registered as `Transaction`. Every transaction has a unique number, date, account, amount, currency, type, and observations.


The system has a Fraud Detection when some accounts have 2 transactions in less than 2 seconds (edit from Settings), and some accounts try to decrease their balance by 150% of the high daily transaction every. This happens only when the amounts are over 1.000.


Every month is auto executed the payment of the Monthly Maintenance, and the apply of Interest to accounts. You can set the day to apply both in settings. 
If something happen, and the Interests or Monthly Maintenance doesn't apply automatic, Admins can apply manually.

If some error happens or some action is not supported, the system shows a specific error but continues working.


## ACCOUNTHOLDERS:

Account Holder User: Account holder users are customers of the bank who have one or more accounts with the bank.

### User Functions:
Register if they are +18. In case of -18, they need to go to the Bank to register.
View and Update their Personal Info.
“Forgot Password” function to get the link to regenerate the password.

### Account Functions:
Create accounts: CheckingAccounts (or StudentAccount if they are -24), SavingAccounts, CreditAccounts(CreditCards).
Set or Delete Secondary Owner to Accounts.
Deposit: Deposit in CheckingAccounts, deposit in SavingAccounts, deposit in SavingAccounts from CheckingAccounts.
Withdraw: Withdraw from SavingAccounts or CheckingAccounts.
Buy with CreditCard.
View: List of All Accounts or specific Account.
View: All transactions ordered from Account.
Transfer: Make a transfer to another Account.



## THIRDPARTY:


Third Party User: Third party users are external entities that have access to the system to perform specific actions. They can make a debit from SavingAccounts if they have the Account Number and Account Secret Key. Third Party can Register in the system and add Accounts to their own AccountMap (is a list of accounts with secret key).
ThirdParty User Registered Functions:
Register ThirdPartyUser, but their user must be activated for Admin.
Add to their AccountMap the accounts.
View list of the accounts registered.
Debit service from an account registered.

ThirdParty Not Registered Functions:
Charge service in Account, with AccountNumber and SecretKey.
Transfer to Account.



## ADMIN:

Admin User: Admin users have a direct relationship with the bank and are able to access the system using their employee information and credentials, such as a username and password. They can view and manage user, accounts, and transaction information.
Admin Functions in Users:
Register Admins, AccountHolders and ThirdParty Users.
Search AccountHolders and ThirdParty Users.
Modify Users Data.
Activate or Deactivate Users.
Delete Users.

### Admin Functions in Accounts:
View All Accounts, or view accounts from specific Users.
View Specific Account with All Information.
Update Account settings (like MonthlyMaintenance, Interests, PenaltyFeeAmount, CreditLimit, MinimumBalance, SecondaryOwner)
Freeze, UnFreeze and Delete Accounts.
Apply Penalty, Interests, Monthly Maintenance, and debit CreditCards from SavingAccount.
Create Accounts to Specific AccountHolder.




## SYSTEM AUTOMATIC:

The system makes automatic functions. This function is to apply Interests to Accounts and apply Monthly Maintenance. The Bank can set the Day to apply these functions.


## ENDPOINTS

As default, your application will run in port 8080, so your endpoints will start as `http://localhost:8080/`


### AccountHolders Endpoints:

| # | Endpoint| Method| Action | Login |
| :--- | :--- | :--- | :--- | :--- |
| 1 | /holders/register | POST | Register AccountHolder | NO |
| 2 | /holders/info | GET | Personal User Info | YES |
| 3 | /holders/update/ | PATCH | Update User Data | YES |
| 4 | /holders/forgotpassword/ | POST | Message to restore Password | NO |
| 5 | /holders/setSecondaryOwner/{account} | PUT | Modify SecondaryOwner in Account | YES |
| 6 | /holders/holders/create/checking | POST | Create Checking Account | YES |
| 7 | /holders/holders/create/saving | POST | Create Saving Account | YES |
| 8 | /holders/holders/create/credit | POST | Create Credit Account | YES |
| 9 | /holders/deposit | PUT | Deposit in Checking Account | YES |
| 10 | /holders/deposit/saving/{account} | PUT | Deposit in Saving Account | YES |
| 11 | /holders/deposit/savingfromchecking/{account} | PUT | Deposit in Saving Account from Checking Account | YES |
| 12 | /holders/withdraw | GET | Withdraw from Checking Account | YES |
| 13 | /holders/withdraw/saving/{account} | GET | Withdraw from Saving Account | YES |
| 14 | /holders/buywithcredit | PUT| Buy With Credit Account | YES |
| 15 | /holders/all | GET | View All Accounts | YES |
| 16 | /holders/account | GET | View Account | YES |
| 17 | /holders/transaction | GET | View all Transactions of Account | YES |
| 18 | /holders/transfer/{account} | PUT | Transfer to another Account | YES |


1. Register AccountHolder: Post method, sending a json body with this format:
```
{
"username":"USERNAME",
"password" : "PASSWORD",
"nif":"X-11111111-X",
"firstName":"First Name",
"lastName" : "LastName",
"dateOfBirth" : "1995-01-15",
"address" : "Carrer Catalunya 1, Barcelona.",
"email" : "firstname@mail.com",
"phone" : "+342312322323"
}
```
The endpoint has a validation of values.
Return a Json with the information of a new User.

2. Personal info: Get method, returns a Json with Personal User Info.
3. Update Personal info: Patch Method. Update user data sending optional params: username, password, address, phone, email. Return User Info with Update.
4. Forgot Password: Post Method. Need params “nif” and “email”, and if they match in users database, return a message with “Mail sent to…”
5. Set SecondaryOwner: Put Method. Need param “secondaryOwnerNif”, and the AccountNumber like variable in URL. If “secondaryOwnerNif” equals 0, delete the Secondary Owner. Return Json with the Account with info of SecondaryOwner.
6. Create Checking Account: Post Method. Return Account with details. The AccountHolders can only have 1 Checking Account. If the AccountHolder is over 24, the account is going to be CheckingAccount, if it is less than 24, the account is going to be StudentAccount.
7. Create Saving Account. Post Method. Requires Param of “amount” to debit from CheckingAccount. Make Fraud & Balance validations. Return the Transaction of creation of a new Saving Account.
8. Create Credit Account. Post Method. Create a new CreditAccount to AccountHolder User. Return the new Account.
9. Deposit Checking Account. Put Method. Deposit money in a Checking Account. Requires param “amount” to deposit. Return Transaction.
10. Deposit Saving Account. Put Method. Deposit money in a Saving Account. Requires param “amount” to deposit and the number of accounts in URL as variable. Return Transaction.
11. Deposit Saving Account From Checking Account. Put Method. Deposit money in a Saving Account with money from a Checking Account. Make Fraud & Balance validations . Requires param “amount” to deposit and the number of accounts in URL as variable. Return Transaction.
12. Withdraw from CheckingAccount. Get Method. Withdraw Money from CheckingAccount. Make Fraud & Balance validations . Requires param “amount” to withdraw. Return Transaction.
13. Withdraw from SavingAccount. Get Method. Withdraw Money from SavingAccount. Make Fraud & Balance validations . Requires param “amount” to withdraw and the number of accounts in URL. Return Transaction.
14. Buy With Credit Account. Put Method. Buy in commerce with CreditAccount. Make Fraud & Balance validations . Requires param “amount” to withdraw and the name of “store” in Headers of request. Return Transaction.
15. View All Accounts. Get Method. Return list of All Accounts and their specifications.
16. View Account. Get Method. Return Account and their specifications.
17. View all Transactions of Account. Get Method. Return list of Transactions of Account order by date. Requires “account” as param.
18. Transfer to Another Account. Put Method. Transfer to Another Account, this account could be from this Bank (register the credit and transaction).  Make Fraud & Balance validations. Return Transaction.


### ThirdParty Endpoints:

| # | Endpoint| Method| Action | Login |
| :--- | :--- | :--- | :--- | :--- |
| 1 | /thirdparty/register | POST | Register ThirdParty | NO |
| 2 | /thirdparty/account | POST | Register Account to ThirdParty AccountMap | YES |
| 3 | /thirdparty/account | GET| Get list of AccountMap | YES |
| 4 | /thirdparty/debitservice | PATCH | Debit Service | YES |
| 5 | /thirdparty/chargeservice | PATCH | Charge Service from Account | NO |
| 6 | /thirdparty/transfer | POST | Transfer to Account | NO |



1. Register ThirdParty: Post method, sending a json body with this format:
```
{
"username" : "USERNAME",
"password" : "PASSWORD",
"nif" : "E-23232323",
"companyName" : "ThirdParty Company SA",
"address" : "Polígono Industrial 15, Madrid",
"email" : "thirdpartymail@mail.com",
"phone" : "+342323423323"
}
```
The endpoint has a validation of values.
When ThirdParty registers himself, the user must be activated for Admin user.
Return a Json with the information of a new User.

2. Register Account to ThirdParty AccountMap: Post method. Requires “account” and “secretKey” as params. The system verifies if credentials are valid and saves the data in ThirdParty AccountMap. Return the Account and SecretKey as Json format.
3. Get a list of all AccountMaps.
4. Debit Service: Patch Method. Debit service from Account. Requires the “account” in Headers, and “amount” in Params. The account must be registered with their secretKey. Return Transaction.
5. Charge Service: Patch Method. Debit service from Account. Requires the “account”, “secretKey” and “company” in Headers, and “amount” in Params. For this Endpoint is not necessary authentication. This is the public endpoint, the security is with the SecretKey of the account. Return Transaction.
6. Transfer: Post Method. Transfer to Account of Bank.  Requires a Json body with this format:
```
{
"toAccount":"IB95230128102233",
"amount": 155.66
}
```
Requires a “bank” and “thirdClient” in Headers. For this Endpoint is not necessary authentication. Make a NotFreeze validation in account. Return Transaction.


### Admin Endpoints:

| # | Endpoint| Method| Action | Login |
| :-- | :--- | :--- | :--- | :--- |
| 1 | /admin/register/AH | POST | Register AccountHolder | YES |
| 2 | /admin/register/TP | POST | Register ThirdParty| YES |
| 3 | /admin/register/ADMIN | POST | Register Admin | YES |
| 4 | /admin/AH | GET | Search AccountHolders | YES |
| 5 | /admin/TP | GET | Search ThirdParty| YES |
| 6 | /admin/AH/{id} | PATCH | Update AccountHolder Data | YES |
| 7 | /admin/TP/{id} | PATCH | Update ThirdParty Data | YES |
| 8 | /admin/ADMIN/{id} | PATCH | Update Admin Data | YES |
| 9 | /admin/activate/{id} | PATCH | Activate User | YES |
| 10 | /admin/deactivate/{id} | PATCH | Deactivate User | YES |
| 11 | /admin/user/{id} | DELETE | Delete User | YES |
| 12 | /admin/accounts/ | GET | Search Accounts | YES |
| 13 | /admin/accounts/{accountNumber} | GET | View Account Details | YES |
| 14 | /admin/accounts/{accountNumber} | PATCH | Update Account | YES |
| 15 | /admin/accounts/freeze/{accountNumber} | PATCH | Freeze/Unfreeze Account | YES |
| 16 | /admin/accounts/delete/{accountNumber} | DELETE | Delete Account | YES |
| 17 | /admin/accounts/penalty/{accountNumber} | PUT | Apply Penalty Fee | YES |
| 18 | /admin/accounts/interests/credit | PUT | Apply Interests To Credit Accounts | YES |
| 19 | /admin/accounts/interests/saving | PUT | Apply Interests To saving Accounts | YES |
| 20 | /admin/accounts/maintenance | PUT | Apply Monthly Maintenance to All Accounts | YES |
| 21 | /admin/accounts/debitcredit | PUT | Debit All Credit Accounts | YES |
| 22 | /admin/accounts/create/checking/{id} | POST | Create Checking Account | YES |
| 23 | /admin/accounts/create/credit/{id} | POST | Create Credit Account | YES |
| 24 | /admin/accounts/create/saving/{id} | POST | Create Saving Account | YES |
| 25 | /admin/deposit/{account} | PUT | Deposit to Account | YES |
| 26 | /admin/withdraw/{account} | GET | Withdraw from Account | YES |
| 27 | /admin/transfer/{accountFrom}-{accountTo} | POST | Transfer Between Accounts | YES |



1. Register AccountHolder: Post method, sending a json body with this format:
```
{
"username":"USERNAME",
"password" : "PASSWORD",
"nif":"X-11111111-X",
"firstName":"First Name",
"lastName" : "LastName",
"dateOfBirth" : "1995-01-15",
"address" : "Carrer Catalunya 1, Barcelona.",
"email" : "firstname@mail.com",
"phone" : "+342312322323"
}
```
The endpoint has a validation of values.
Return a Json with the information of a new User.

2. Register ThirdParty: Post method, sending a json body with this format:
```
{
"username" : "USERNAME",
"password" : "PASSWORD",
"nif" : "E-23232323",
"companyName" : "ThirdParty Company SA",
"address" : "Polígono Industrial 15, Madrid",
"email" : "thirdpartymail@mail.com",
"phone" : "+342323423323"
}
```
The endpoint has a validation of values.
Return a Json with the information of a new User.

3. Register Admin : Post method, sending a json body with this format:
```
{
"username":"USERNAME",
"password":"PASSWORD",
"firstName":"Administrator",
"lastName":"Secondary",
"email":"secondAdmin@mail.com"
}
```
The endpoint has a validation of values.
Return a Json with the information of a new User.

4. Search AccountHolders. Get Method. The method has Optional Params to search AccountHolders. The optional Params are: username, firstName, lastName, nif, phone. Returns a List of AccountHolders with a match of the search.
5. Search ThirdParty. Get Method. The method has Optional Params to search ThirdParty. The optional Params are: username, companyName, nif. Returns a List of ThirdPartys with matches of the search.
6. Update AccountHolder Data. Patch Method. Requires ID of AccountHolder to update as Variable in URL. The method has Optional Params to update: username, firstName, lastName, nif, dateOfBirth, address, phone, email. Returns the AccountHolder updated.
7. Update ThirdParty Data. Patch Method. Requires ID of ThirdParty to update as Variable in URL. The method has Optional Params to update: username, firstName, lastName, nif, dateOfBirth, address, phone, email. Returns the updated ThirdParty.
8. Update Admin Data. Patch Method. Requires ID of Admin to update as Variable in URL. The method has Optional Params to update: username, firstName, lastName, email. Returns the Admin updated.
9. Activate User. Patch Method. Require ID of User. Return String with confirmation.
10. Deactivate Users. Patch Method. Require ID of User. Return String with confirmation.
11. Delete User. Delete Method. Require ID of User. Return String with confirmation. When a User is deleted, all their accounts have been deleted too.
12. Search Accounts: Get Method. Return list of Accounts. Optional param to search by username.
13. View Account Details. Get Method. Return Account with all details.
14. Update Account. Patch Method. Requires Account Number as Variable in URL. Optional Params: secondaryOwnerId, minimumBalance, creditLimit, interests. Put secondaryOwnerId 0 to remove the secondary owner.
15. Freeze/Unfreeze Account. Patch Method. Require Account Number as Variable in URL and “action” as param with values 0 (FREEZE ACCOUNT) 1 (ACTIVE ACCOUNT). Return Account with all information.
16. Delete Account. Delete Method. Require Account Number as Variable in URL. When an Account is deleted, all their Transactions are deleted too. Return confirmation.
17. Apply Penalty Fee. Put Method. Require Account Number as Variable in URL. Return Transaction.
18. Apply Interests to Credit Accounts. Put Method. Return List of Transactions.
19. Apply Interests to Saving Accounts. Put Method. Return List of Transactions.
20. Apply Monthly Maintenance to All Accounts. Put Method. Return List of Transactions.
21. Debit All Credit Accounts from Checking Account. Put Method. Return List of Transactions.
22. Create Checking Account: Post Method. Request ID Number of AccountoHolder to create Checking Account. Return Account.
23. Create Credit Account: Post Method. Request ID Number of AccountoHolder to create Credit Account. Return Account.
24. Create Saving Account: Post Method. Request ID Number of AccountoHolder to create Checking Account, and “amount” as Param, this amount will be debited from the Checking Account of this User. Return Account.
25. Deposit Account: Post Method. Request Account Number "account"as Varible in URL, and "amount", as Param, this amount will be accredited to the Account. Return Transaction.
26. Withdraw Account: Get Method. Request Account Number "account" as Varible in URL, and "amount" as Param, this amount will be debited from the Account. Return Transaction.
27. Transfer To Account: Post Method. Request Account Number "accountFrom" and "amountTo" as Varible in URL,and "amount" as Param, this amount will be debited from the AccountFrom, and if AccountTo is from this Bank, the amount will be accredited. Return Transaction from Debit.




### Automatic Endpoints:

| # | Endpoint| Method| Action | Login |
| :--- | :--- | :--- | :--- | :--- |
| 1 | /auto/interests/credit | PUT | Apply Interests to Credit Account | NO |
| 2 | /auto/interests/saving | PUT | Apply Interests to Saving Account | NO |
| 3 | /auto/maintenance | PUT | Apply Monthly Maintenance to All Accounts | NO |


These endpoints are with public access, but it's only used for applying automatic interests or maintenance.

---

#### Developed by [Andres](https://github.com/o-andres-m) 
