package edu.sr.generated.example1;


/**
* edu/sr/generated/example1/S1Helper.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from sr.idl
* poniedziałek, 4 maj 2015 19:37:41 CEST
*/

abstract public class S1Helper
{
  private static String  _id = "IDL:example1/S1:1.0";

  public static void insert (org.omg.CORBA.Any a, edu.sr.generated.example1.S1 that)
  {
    org.omg.CORBA.portable.OutputStream out = a.create_output_stream ();
    a.type (type ());
    write (out, that);
    a.read_value (out.create_input_stream (), type ());
  }

  public static edu.sr.generated.example1.S1 extract (org.omg.CORBA.Any a)
  {
    return read (a.create_input_stream ());
  }

  private static org.omg.CORBA.TypeCode __typeCode = null;
  private static boolean __active = false;
  synchronized public static org.omg.CORBA.TypeCode type ()
  {
    if (__typeCode == null)
    {
      synchronized (org.omg.CORBA.TypeCode.class)
      {
        if (__typeCode == null)
        {
          if (__active)
          {
            return org.omg.CORBA.ORB.init().create_recursive_tc ( _id );
          }
          __active = true;
          org.omg.CORBA.StructMember[] _members0 = new org.omg.CORBA.StructMember [2];
          org.omg.CORBA.TypeCode _tcOf_members0 = null;
          _tcOf_members0 = org.omg.CORBA.ORB.init ().get_primitive_tc (org.omg.CORBA.TCKind.tk_long);
          _members0[0] = new org.omg.CORBA.StructMember (
            "a",
            _tcOf_members0,
            null);
          _tcOf_members0 = org.omg.CORBA.ORB.init ().get_primitive_tc (org.omg.CORBA.TCKind.tk_char);
          _members0[1] = new org.omg.CORBA.StructMember (
            "b",
            _tcOf_members0,
            null);
          __typeCode = org.omg.CORBA.ORB.init ().create_struct_tc (edu.sr.generated.example1.S1Helper.id (), "S1", _members0);
          __active = false;
        }
      }
    }
    return __typeCode;
  }

  public static String id ()
  {
    return _id;
  }

  public static edu.sr.generated.example1.S1 read (org.omg.CORBA.portable.InputStream istream)
  {
    edu.sr.generated.example1.S1 value = new edu.sr.generated.example1.S1 ();
    value.a = istream.read_long ();
    value.b = istream.read_char ();
    return value;
  }

  public static void write (org.omg.CORBA.portable.OutputStream ostream, edu.sr.generated.example1.S1 value)
  {
    ostream.write_long (value.a);
    ostream.write_char (value.b);
  }

}
