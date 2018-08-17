# bank-rest

##Information
- Refer to /swagger-ui.html for API endpoints and information

##Assumptions:
- Initially 4 accounts are created by default - 111111, 222222, 333333, 444444
- You need to register using the registration API
- You need to login with username and password to generate token
- You need to be Authenticated and possess a token for accessing your account.
- Token should be sent in the `Authorization` header in the format `Bearer <token>`.
- Beneficiary `SHOULD` have an account in the database.
- Transaction can be either a debit or a credit and can only happen to a beneficiary.
- Transaction amount cannot be more than transfer limit for that beneficiary.
- Any one with a valid token can Add an account
- Once a Account Number is added it can't be added again