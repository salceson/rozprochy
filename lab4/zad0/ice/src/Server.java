// **********************************************************************
//
// Copyright (c) 2003-2011 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************

import Demo.*;
import Ice.Identity;

public class Server 
{
	
    public int run(String[] args)
    {
    	Ice.Communicator communicator = Ice.Util.initialize(args); 

        Ice.ObjectAdapter adapter = communicator.createObjectAdapter("SR");
        
        Identity id1 = communicator.stringToIdentity("obj111");
        HelloI servant1 = new HelloI();
        
        adapter.add(servant1, id1);
        
        adapter.activate();
        
		System.out.println("Entering event processing loop...");
        communicator.waitForShutdown();
        
        return 0;
    }

    public static void
    main(String[] args)
    {
        Server app = new Server();
        int status = app.run(args);
        System.exit(status);
    }
}
