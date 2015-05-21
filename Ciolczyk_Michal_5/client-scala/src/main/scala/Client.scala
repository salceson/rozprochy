import java.util.Scanner

import Bank._
import Ice.StringHolder
import Ice.Util.initialize

object Client extends App {
  def printMenu(): Unit = {
    println("Menu:")
    println("\tc: create new account")
    println("\tr: remove account")
    println("\ts: silver account operations")
    println("\tx: exit")
    print("Enter option: ")
  }

  def createAccount(scanner: Scanner, bankManagerProxy: BankManagerPrx): Unit = {
    println("Creating new account:")
    print("\tEnter first name: ")
    val firstName = scanner.nextLine()
    print("\tEnter last name: ")
    val lastName = scanner.nextLine()
    print("\tEnter nationality: ")
    val nationality = scanner.nextLine()
    print("\tEnter national ID number: ")
    val nationalIDNumber = scanner.nextLine()
    var accType: accountType = null
    var entered = false
    while (!entered) {
      print("\tEnter account type (p - premium, s - silver): ")
      scanner.nextLine() match {
        case "p" =>
          accType = accountType.PREMIUM
          entered = true
        case "s" =>
          accType = accountType.SILVER
          entered = true
        case _ =>
          println("\tWrong choice!")
      }
    }
    val personalData = new PersonalData(firstName, lastName, nationality, nationalIDNumber)
    val accountNumberHolder = new StringHolder()
    try {
      bankManagerProxy.createAccount(personalData, accType, accountNumberHolder)
      println("\tAccount created!")
      print("\t\tAccount number: ")
      println(accountNumberHolder.value)
    } catch {
      case e: RequestRejected => println("\tRequest rejected!")
      case e: IncorrectData => println("\tIncorrect data!")
    }
  }

  def deleteAccount(scanner: Scanner, bankManagerProxy: BankManagerPrx): Unit = {
    println("Removing account:")
    print("\tEnter account number: ")
    val accountNumber = scanner.nextLine()
    try{
      bankManagerProxy.removeAccount(accountNumber)
      println("\tAccount removed!")
    } catch {
      case e: NoSuchAccount => println("\tWrong account number!")
      case e: IncorrectData => println("\tIncorrect data!")
    }
  }

  override def main(args: Array[String]) {
    val scanner = new Scanner(System.in)
    val communicator = initialize(args)
    val proxy = communicator.propertyToProxy("BankManager")
    val bankManagerProxy = BankManagerPrxHelper.checkedCast(proxy)
    while (true) {
      printMenu()
      val input = scanner.nextLine()
      input.trim match {
        case "c" =>
          createAccount(scanner, bankManagerProxy)
        case "r" =>
          deleteAccount(scanner, bankManagerProxy)
        case "s" =>
          val sap = new SilverAccountOperations(communicator, scanner)
          sap.accountMenu()
        case "x" =>
          communicator.shutdown()
          System.exit(0)
        case _ =>
          println("Wrong choice!")
      }
    }
  }
}
