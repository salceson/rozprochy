package edu.sr.generated.example1;


/**
* edu/sr/generated/example1/I2ExtHelper.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from sr.idl
* poniedziałek, 4 maj 2015 19:37:41 CEST
*/

abstract public class I2ExtHelper
{
  private static String  _id = "IDL:example1/I2Ext:1.0";

  public static void insert (org.omg.CORBA.Any a, edu.sr.generated.example1.I2Ext that)
  {
    org.omg.CORBA.portable.OutputStream out = a.create_output_stream ();
    a.type (type ());
    write (out, that);
    a.read_value (out.create_input_stream (), type ());
  }

  public static edu.sr.generated.example1.I2Ext extract (org.omg.CORBA.Any a)
  {
    return read (a.create_input_stream ());
  }

  private static org.omg.CORBA.TypeCode __typeCode = null;
  synchronized public static org.omg.CORBA.TypeCode type ()
  {
    if (__typeCode == null)
    {
      __typeCode = org.omg.CORBA.ORB.init ().create_interface_tc (edu.sr.generated.example1.I2ExtHelper.id (), "I2Ext");
    }
    return __typeCode;
  }

  public static String id ()
  {
    return _id;
  }

  public static edu.sr.generated.example1.I2Ext read (org.omg.CORBA.portable.InputStream istream)
  {
    return narrow (istream.read_Object (_I2ExtStub.class));
  }

  public static void write (org.omg.CORBA.portable.OutputStream ostream, edu.sr.generated.example1.I2Ext value)
  {
    ostream.write_Object ((org.omg.CORBA.Object) value);
  }

  public static edu.sr.generated.example1.I2Ext narrow (org.omg.CORBA.Object obj)
  {
    if (obj == null)
      return null;
    else if (obj instanceof edu.sr.generated.example1.I2Ext)
      return (edu.sr.generated.example1.I2Ext)obj;
    else if (!obj._is_a (id ()))
      throw new org.omg.CORBA.BAD_PARAM ();
    else
    {
      org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate ();
      edu.sr.generated.example1._I2ExtStub stub = new edu.sr.generated.example1._I2ExtStub ();
      stub._set_delegate(delegate);
      return stub;
    }
  }

  public static edu.sr.generated.example1.I2Ext unchecked_narrow (org.omg.CORBA.Object obj)
  {
    if (obj == null)
      return null;
    else if (obj instanceof edu.sr.generated.example1.I2Ext)
      return (edu.sr.generated.example1.I2Ext)obj;
    else
    {
      org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate ();
      edu.sr.generated.example1._I2ExtStub stub = new edu.sr.generated.example1._I2ExtStub ();
      stub._set_delegate(delegate);
      return stub;
    }
  }

}
