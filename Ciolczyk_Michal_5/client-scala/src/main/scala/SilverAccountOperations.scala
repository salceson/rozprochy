import java.util.{InputMismatchException, Scanner}

import Bank._
import Ice.{Communicator, IllegalIdentityException}

class SilverAccountOperations(val communicator: Communicator, val scanner: Scanner) {
  private val ACCOUNT_CATEGORY = "acc"
  private var accountProxy: AccountPrx = null

  def accountMenu(): Unit = {
    print("\tEnter account number: ")
    val acc = scanner.nextLine()
    try {
      setup(acc)
      println("\tAccount menu:")
      println("\t\tb: account balance")
      println("\t\tn: account number")
      println("\t\tt: transfer money")
      println("\t\tanything else: return")
      print("\tEnter choice: ")
      scanner.nextLine() match {
        case "b" =>
          accountBalance()
        case "n" =>
          accountNumber()
        case "t" =>
          transferMoney()
        case _ =>
          println()
      }
    } catch {
      case e: NoSuchAccount =>
        println("\tNo such account!")
    }
  }

  def setup(account: String): Unit = {
    val objectName = ACCOUNT_CATEGORY + "/" + account + communicator.getProperties.getProperty("DefaultEndpoint")
    try {
      val objProxy = communicator.stringToProxy(objectName)
      accountProxy = AccountPrxHelper.checkedCast(objProxy)
      if (accountProxy == null) {
        throw new NoSuchAccount()
      }
    } catch {
      case e: IllegalIdentityException =>
        throw new NoSuchAccount(e)
    }
  }

  def accountBalance(): Unit = {
    println("\t\tAccount balance: " + accountProxy.getBalance)
  }

  def accountNumber(): Unit = {
    println("\t\tAccount number: " + accountProxy.getAccountNumber)
  }

  def transferMoney(): Unit = {
    try {
      print("\tEnter target account number: ")
      val toAccountNumber = scanner.nextLine()
      print("\tEnter amount in PLN: ")
      val amount = scanner.nextInt()
      accountProxy.transferMoney(toAccountNumber, amount)
      println("\t" + amount + " PLN successfully transfered to account: " + toAccountNumber)
    } catch {
      case e: InputMismatchException =>
        println("\tWrong amount!")
      case e: IncorrectAmount =>
        println("\tWrong amount!")
      case e: IncorrectAccountNumber =>
        println("\tWrong account number!")
    }
  }
}
