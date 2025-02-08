package org.kybe;

import org.rusherhack.client.api.RusherHackAPI;
import org.rusherhack.client.api.plugin.Plugin;

public class MaceSwap extends Plugin {
	@Override
	public void onLoad() {
		final MaceSwapModule maceSwapModule = new MaceSwapModule();
		RusherHackAPI.getModuleManager().registerFeature(maceSwapModule);
	}

	@Override
	public void onUnload() {

	}
	
}