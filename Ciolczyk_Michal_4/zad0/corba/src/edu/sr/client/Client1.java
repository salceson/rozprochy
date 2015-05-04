package edu.sr.client;

import edu.sr.generated.example1.S1;
import edu.sr.generated.example1.I1;
import edu.sr.generated.example1.I1Helper;
import edu.sr.generated.example1.I2;
import edu.sr.generated.example1.I2Ext;
import edu.sr.generated.example1.I2ExtHelper;
import org.omg.CORBA.ORB;
import org.omg.CORBA.StringHolder;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.InvalidName;
import org.omg.CosNaming.NamingContextPackage.NotFound;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author Michał Ciołczyk
 */
public class Client1 
{
	I1 i1 = null;
	ORB orb = null;
	
	
	// Get the reference to the object based on its IOR and narrow it appropriately
	public void getRef1(String[] args) throws InvalidName, NotFound, CannotProceed, org.omg.CORBA.ORBPackage.InvalidName, IOException {
		// create and initialize the ORB
		orb = ORB.init(args, null);

		// get the object reference from IOR

		BufferedReader br = new BufferedReader(new FileReader("ref1.ior"));
		String iorLine = br.readLine();

		org.omg.CORBA.Object objRef = orb.string_to_object(iorLine);

		// narrow the reference
		i1 = I1Helper.narrow(objRef);
	}

	
	// Get the reference to the object using NS and narrow it appropriately
	public void getRef2(String[] args) throws InvalidName, NotFound, CannotProceed, org.omg.CORBA.ORBPackage.InvalidName, IOException {
		// create and initialize the ORB
		orb = ORB.init(args, null);

		org.omg.CORBA.Object nsRef = null;
				
		// get the reference from IOR (it points to NS Object (NS is a CORBA Object, too!)

//		nsRef = orb.resolve_initial_references("NameService");
//		nsRef = orb.string_to_object("corbaloc:iiop:127.0.0.1:900");
		BufferedReader br = new BufferedReader(new FileReader("ns.ior"));
		String iorLine = br.readLine();
		nsRef = orb.string_to_object(iorLine);

		// narrow the reference appropriately
		NamingContextExt ncRef = NamingContextExtHelper.narrow( nsRef );

		// use the reference calling the object's operations 
		
		// get the Object Reference from NS
		org.omg.CORBA.Object objRef = ncRef.resolve_str("ala_i_tomek1");

		System.out.println("OBJ= " + objRef);
		
		// narrow the reference appropriately
		i1 = I1Helper.narrow(objRef);
	}
	

	void call1()
	{
		StringHolder text2 = new StringHolder("bolek");
		StringHolder text3 = new StringHolder();
		S1 s1 = new S1();
		s1.a = 77;
		s1.b = 'r';

		i1.op1(123);
		
		String res = i1.op2("zenek", text2, text3, s1);

		System.out.println("I1::op2 returned: " + text2.value + " " + text3.value + " " + res);

		short arg = 2;
		long op3 = i1.op3(arg);

		System.out.println("I1::op3 returned: " + op3);
	}
	
	
	void callFactory()
	{
		I2 i21 = i1.getI2();
		System.out.println("I2::op returned: " + i21.op());
		System.out.println("i21 = " + orb.object_to_string(i21));

		I2 i22 = i1.getI2();
		System.out.println("I2::op returned: " + i22.op());	
		if(i22._is_a("IDL:example1/I2Ext:1.0")) 
		{
			I2Ext i22e = I2ExtHelper.narrow(i1.getI2());
			System.out.println("I2Ext::opExt returned: " + i22e.opExt());
		}
		System.out.println("i22 = " + orb.object_to_string(i22));
	}
	
	
	void callDII()
	{
		org.omg.CORBA.Request r = i1._request("op1");
		r.add_in_arg().insert_long(77);
		r.set_return_type(orb.get_primitive_tc(org.omg.CORBA.TCKind.tk_short));

		r.invoke();

		if( r.env().exception() == null )
        {
		    System.out.println("I1::op1 (DII request) returned: " + r.return_value().extract_short() );  
        }
	}
	
	public static void main(String[] args)
			throws InvalidName, NotFound, CannotProceed, org.omg.CORBA.ORBPackage.InvalidName, IOException {
		Client1 c1 = new Client1();
//		c1.getRef1(args);
		c1.getRef2(args);
		c1.call1();
		//c1.callFactory();
		//c1.callDII();
	}

}
