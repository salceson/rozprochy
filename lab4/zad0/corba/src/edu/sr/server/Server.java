package edu.sr.server;

import java.io.*;

import edu.sr.generated.example1.I1;
import edu.sr.generated.example1.I1Helper;
import org.omg.CORBA.ORB;
import org.omg.CORBA.Policy;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.NotFound;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import org.omg.PortableServer.POAManagerPackage.AdapterInactive;
import org.omg.PortableServer.POAPackage.ObjectAlreadyActive;
import org.omg.PortableServer.POAPackage.ServantAlreadyActive;
import org.omg.PortableServer.POAPackage.ServantNotActive;
import org.omg.PortableServer.POAPackage.WrongPolicy;

import edu.sr.impl.I1Impl;

/**
 * @author Michał Ciołczyk
 */
public class Server {

	ORB orb = null;
	POA rootpoa = null;
	
	public Server()
	{
	}

	public void base(String[] args) 
			throws InvalidName, AdapterInactive
	{
		// create and initialize the ORB
		orb = ORB.init( args, null );
		
		// get reference to rootpoa & activate the POAManager
		rootpoa = POAHelper.narrow(orb.resolve_initial_references( "RootPOA" ));

		rootpoa.the_POAManager().activate();

	}

	public void inst1() 
		throws InvalidName, ServantAlreadyActive, WrongPolicy, ServantNotActive, IOException
	{
		// create servant
		I1Impl servant1 = new I1Impl(rootpoa);

		// register it with the POA and get the object reference
		//rootpoa.activate_object_with_id("moj_pierwszy_serwant".getBytes(), i1Impl);
		rootpoa.activate_object(servant1);
		
		org.omg.CORBA.Object ref = rootpoa.servant_to_reference(servant1);

		// export object reference to a file
		PrintWriter pw = new PrintWriter(new FileWriter("ref1.ior"));
		pw.print(orb.object_to_string(ref));
		pw.close();
		
		// wait for invocations from clients
		System.out.println("Entering event processing loop...");
		orb.run();
	}

	
	public void inst2() throws InvalidName, ServantNotActive, WrongPolicy, IOException, org.omg.CosNaming.NamingContextPackage.InvalidName, NotFound, CannotProceed, ServantAlreadyActive, ObjectAlreadyActive
	{
		// create servants
		I1Impl servant1 = new I1Impl(rootpoa);
		I1Impl servant2 = new I1Impl(rootpoa);
	
		// register them with the POA and get the object reference
		//rootpoa.activate_object_with_id("moj_pierwszy_serwant".getBytes(), i1Impl);
		rootpoa.activate_object(servant1);
		rootpoa.activate_object(servant2);
		
		org.omg.CORBA.Object ref1 = rootpoa.servant_to_reference(servant1);
		org.omg.CORBA.Object ref2 = rootpoa.servant_to_reference(servant2);

		org.omg.CORBA.Object nsRef = null;
		
//		nsRef = orb.resolve_initial_references("NameService");
//		nsRef = orb.string_to_object("corbaloc:iiop:127.0.0.1:900");
 		BufferedReader br = new BufferedReader(new FileReader("ns.ior"));
		String iorLine = br.readLine();
		nsRef = orb.string_to_object(iorLine);
		
		//narrow NS reference
		NamingContextExt ncRef = NamingContextExtHelper.narrow( nsRef );

		// bind the Object Reference in Naming
		NameComponent path1[] = ncRef.to_name("ala_i_tomek1");
		NameComponent path2[] = ncRef.to_name("ala_i_tomek2");

		ncRef.rebind(path1, ref1);
		ncRef.rebind(path2, ref2);
		
		// wait for invocations from clients
		System.out.println("Entering event processing loop...");
		orb.run();

	}
	
	public static void main(String[] args) throws InvalidName, AdapterInactive, ServantNotActive, WrongPolicy, IOException, org.omg.CosNaming.NamingContextPackage.InvalidName, NotFound, CannotProceed, ServantAlreadyActive, ObjectAlreadyActive 
	{
		Server s = new Server();
		s.base(args);
//		s.inst1();
		s.inst2();
	}

}
