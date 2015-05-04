// **********************************************************************
//
// Copyright (c) 2003-2011 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************

import Demo.*;

public class HelloI extends _HelloDisp
{
    public void
    sayHello(int delay, int times, Ice.Current current)
    {
        if(delay > 0)
        {
            try
            {
                Thread.currentThread().sleep(delay);
            }
            catch(InterruptedException ex1)
            {
            }
        }

        for(int i = 0; i < times; i++) {
            System.out.println("Hello World!");
        }
    }

    public void
    shutdown(Ice.Current current)
    {
        System.out.println("Shutting down...");
        current.adapter.getCommunicator().shutdown();
    }
}
