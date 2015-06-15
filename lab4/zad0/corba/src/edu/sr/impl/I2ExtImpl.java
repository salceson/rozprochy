package edu.sr.impl;

import edu.sr.generated.example1.I2ExtPOA;

public class I2ExtImpl extends I2ExtPOA
{

	@Override
	public int opExt() {
		System.out.println("I2Ext::opExt");
		return 123000;
	}

	@Override
	public int op() {
		// TODO Auto-generated method stub
		return 0;
	}

}
