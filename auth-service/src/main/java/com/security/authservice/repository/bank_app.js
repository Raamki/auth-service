// Define the Bank class
class Bank {
    constructor() {
      this.checkingAccounts = new Map();
      this.savingsAccounts = new Map();
      this.loans = [];
    }

    authenticateUser (customer, account) {
        if (customer!= null && Validator.authCustomer(account, password) ) {
            console.log(`Account authentication success`);
        } else {
            console.log(`Account authentication failed`);
        }
    }

    getAccount (accountNumber) {
        return savingsAccounts.get(accountNumber);
    }
    getCustomer (customerName) {
        return savingsAccounts.get(email);
    }
  
    // Create a checking account for a customer
    createCheckingAccount(customerName) {
      if (!this.checkingAccounts[customerName]) {
        this.checkingAccounts[customerName] = 0;
        console.log(`${customerName} has opened a checking account`);
      } else {
        console.log(`${customerName} already has a checking account`);
      }
    }
  
    // Create a savings account for a customer
    createSavingsAccount(customerName) {
      if (!this.savingsAccounts[customerName]) {
        this.savingsAccounts[customerName] = 0;
        console.log(`${customerName} has opened a savings account`);
      } else {
        console.log(`${customerName} already has a savings account`);
      }
    }
  
    // Deposit amount to a customer's account
    deposit(customerEmail, password, accountType, amount, accountNumber) {
         account = getAccount(accountNumber);
         if (authenticateUser(getCustomer(customerEmail), getAccount(accountNumber), password)) {
          if (accountType === "checking") {
            account.amount += amount;
            account.push({
                      userName: customerName,
                      amount: amount,
                    });
            this.checkingAccounts[customerName] += amount;
            console.log(`${customerName} has deposited $${amount} to their checking account`);
          } else if (accountType === "savings") {
            this.savingsAccounts[customerName] += amount;
            console.log(`${customerName} has deposited $${amount} to their savings account`);
          }
      } else {
         console.log(`Account authentication failed`);
      }
    }
  
    // Withdraw amount from a customer's account
    withdraw(customerName, password, accountType, amount) {
      account = getAccount(accountNumber);
        if (authenticateUser(getCustomer(customerEmail), getAccount(accountNumber), password)) {
            if (accountType === "checking") {
            if (this.checkingAccounts[customerName] >= amount) {
                account.amount -= amount;
                account.push({
                          userName: customerName,
                          amount: amount,
                        });
              this.checkingAccounts[customerName] -= amount;
              console.log(`${customerName} has withdrawn $${amount} from their checking account`);
            } else {
              console.log(`Insufficient funds in ${customerName}'s checking account`);
            }
          } else if (accountType === "savings") {
            if (this.savingsAccounts[customerName] >= amount) {
              this.savingsAccounts[customerName] -= amount;
              console.log(`${customerName} has withdrawn $${amount} from their savings account`);
            } else {
              console.log(`Insufficient funds in ${customerName}'s savings account`);
            }
          }
        }
    }
  
    // Apply for a loan
    applyForLoan(customerName, amount, income, expenses) {
      const expenditureRatio = expenses / income;
      if (expenditureRatio <= 0.36) {
        const emi = amount * 0.1; // assuming 10% interest rate
        this.loans.push({
          customerName: customerName,
          amount: amount,
          emi: emi
        });
        console.log(`${customerName} has been approved for a loan of $${amount} with a monthly EMI of $${emi}`);
        return true;
      } else {
        console.log(`${customerName} is not eligible for a loan`);
        return false;
      }
    }
  
    // Pay back a loan
    payLoan(customerName, amount) {
      const loan = this.loans.find(l => l.customerName === customerName);
      if (loan) {
        if (amount >= loan.emi) {
          loan.amount -= loan.emi;
          console.log(`${customerName} has paid back $${loan.emi} towards their loan`);
        } else {
          console.log(`The amount paid by ${customerName} is less than the EMI due`);
     }
}
}
}

class Customer {
      constructor(userName, address, phone, email, password) {
        this.userName = userName;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.password = password;
    }
}

class Account {
    constructor(accountNumber, userName, type, amount, loan, password) {
            this.accountNumber = accountNumber;
            this.name = name;
            this.email = email;
            this.password = password;
        }
}

class Validator {
    authCustomer(account, password) {
        if (account.password === password) {
            return true;
        } else {
            return false;
        }
    }

}
}