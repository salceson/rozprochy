package edu.sr.generated.example2;

/**
* edu/sr/generated/example2/ItemNotExistsHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from sr.idl
* poniedziałek, 4 maj 2015 19:37:41 CEST
*/

public final class ItemNotExistsHolder implements org.omg.CORBA.portable.Streamable
{
  public edu.sr.generated.example2.ItemNotExists value = null;

  public ItemNotExistsHolder ()
  {
  }

  public ItemNotExistsHolder (edu.sr.generated.example2.ItemNotExists initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = edu.sr.generated.example2.ItemNotExistsHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    edu.sr.generated.example2.ItemNotExistsHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return edu.sr.generated.example2.ItemNotExistsHelper.type ();
  }

}