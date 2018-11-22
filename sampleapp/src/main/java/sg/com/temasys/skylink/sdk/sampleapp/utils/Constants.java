package sg.com.temasys.skylink.sdk.sampleapp.utils;

public final class Constants {
    public static final int TIME_OUT = 30;

    public static final String ROOM_NAME_AUDIO_DEFAULT = "audioRoom";
    public static final String ROOM_NAME_CHAT_DEFAULT = "chatRoom";
    public static final String ROOM_NAME_DATA_DEFAULT = "dataRoom";
    public static final String ROOM_NAME_FILE_DEFAULT = "fileRoom";
    public static final String ROOM_NAME_PARTY_DEFAULT = "partyRoom";
    public static final String ROOM_NAME_VIDEO_DEFAULT = "videoRoom";

    public static final String USER_NAME_AUDIO_DEFAULT = "User-audio";
    public static final String USER_NAME_CHAT_DEFAULT = "User-chat";
    public static final String USER_NAME_DATA_DEFAULT = "User-dataTransfer";
    public static final String USER_NAME_FILE_DEFAULT = "User-fileTransfer";
    public static final String USER_NAME_PARTY_DEFAULT = "User-multiVideosCall";
    public static final String USER_NAME_VIDEO_DEFAULT = "User-video";

    public enum CONFIG_TYPE {
        AUDIO,
        VIDEO,
        CHAT,
        DATA,
        FILE,
        MULTI_PARTY_VIDEO
    }
}
