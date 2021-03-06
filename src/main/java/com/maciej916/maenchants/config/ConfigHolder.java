package com.maciej916.maenchants.config;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public final class ConfigHolder {

	public static final ForgeConfigSpec SERVER_SPEC;
	static final ServerConfig SERVER;
	static {
		{
			final Pair<ServerConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ServerConfig::new);
			SERVER = specPair.getLeft();
			SERVER_SPEC = specPair.getRight();
		}
	}
}
