package edu.sr.generated.example2;


/**
* edu/sr/generated/example2/ItemBusy.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from sr.idl
* poniedziałek, 4 maj 2015 19:37:41 CEST
*/

public final class ItemBusy extends org.omg.CORBA.UserException
{

  public ItemBusy ()
  {
    super(ItemBusyHelper.id());
  } // ctor


  public ItemBusy (String $reason)
  {
    super(ItemBusyHelper.id() + "  " + $reason);
  } // ctor

} // class ItemBusy
