package jp.thingy.thingymcconfig.model;

import com.google.gson.annotations.SerializedName;

public class StatusResponse {

    public Network network;

    public static class Network {

        public ConfigState config_state;
        public Supplicant supplicant;
        public DHCP4 dhcp4;

        public enum ConfigState {
            @SerializedName("unconfigured")
            UNCONFIGURED,
            @SerializedName("inprogress")
            INPROGRESS,
            @SerializedName("configured")
            CONFIGURED;
        }

        public static class Supplicant {
            public boolean connected;
        }

        public static class DHCP4 {
            public State state;
            public Lease lease;

            public enum State {
                @SerializedName("configured")
                CONFIGURED
            }

            public static class Lease {
                public String ip;
                public String subnetmask;
                public String defaultgw;
                public String[] nameservers;
            }
        }
    }
}
