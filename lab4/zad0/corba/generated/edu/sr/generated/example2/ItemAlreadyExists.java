package edu.sr.generated.example2;


/**
* edu/sr/generated/example2/ItemAlreadyExists.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from sr.idl
* poniedziałek, 4 maj 2015 19:37:41 CEST
*/

public final class ItemAlreadyExists extends org.omg.CORBA.UserException
{

  public ItemAlreadyExists ()
  {
    super(ItemAlreadyExistsHelper.id());
  } // ctor


  public ItemAlreadyExists (String $reason)
  {
    super(ItemAlreadyExistsHelper.id() + "  " + $reason);
  } // ctor

} // class ItemAlreadyExists
