package de.bxrchii.betterapi;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.bukkit.entity.Player;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.UUID;

public class LabyModAPI {

    @SuppressWarnings("unused")
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("labymod3:main")) {
            return;
        }

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(message));

        ByteBuf buf = Unpooled.wrappedBuffer(message);
        String key = LabyModProtocol.readString(buf, Short.MAX_VALUE);
        String json = LabyModProtocol.readString(buf, Short.MAX_VALUE);

        // LabyMod user joins the server
        if (key.equals("INFO")) {
            // Handle the json message
        }
    }

    public void update(Player player, Integer Cash, Integer Bank) {
        updateBalanceDisplay(player, EnumBalanceType.CASH, true, Cash);
        updateBalanceDisplay(player, EnumBalanceType.BANK, true, Bank);
    }

    /**
     * Just send this packet to update the value of the balance or to show/hide it
     */
    public void updateBalanceDisplay(Player player, EnumBalanceType type, boolean visible, int balance) {
        JsonObject economyObject = new JsonObject();
        JsonObject cashObject = new JsonObject();

        // Visibility
        cashObject.addProperty( "visible", visible );

        // Amount
        cashObject.addProperty( "balance", balance );

	    /*
	    // Icon (Optional)
	    cashObject.addProperty( "icon", "<url to image>" );

	    // Decimal number (Optional)
	    JsonObject decimalObject = new JsonObject();
	    decimalObject.addProperty("format", "##.##"); // Decimal format
	    decimalObject.addProperty("divisor", 100); // The value that divides the balance
	    cashObject.add( "decimal", decimalObject );
	    */

        // The display type can be "cash" or "bank".
        economyObject.add(type.getKey(), cashObject);

        // Send to LabyMod using the API
        LabyModProtocol.sendLabyModMessage( player, "economy", economyObject );
    }

    public void updateBalanceDisplay(Player player, EnumBalanceType type, boolean visible, int balance, boolean decimal) {
        JsonObject economyObject = new JsonObject();
        JsonObject cashObject = new JsonObject();

        // Visibility
        cashObject.addProperty( "visible", visible );

        // Amount
        cashObject.addProperty( "balance", balance );

        // Decimal number (Optional)
        if (decimal) {
            JsonObject decimalObject = new JsonObject();
            decimalObject.addProperty("format", "##.##"); // Decimal format
            decimalObject.addProperty("divisor", 100); // The value that divides the balance
            cashObject.add( "decimal", decimalObject );
        }

        // The display type can be "cash" or "bank".
        economyObject.add(type.getKey(), cashObject);

        // Send to LabyMod using the API
        LabyModProtocol.sendLabyModMessage( player, "economy", economyObject );
    }

    public void updateBalanceDisplay(Player player, EnumBalanceType type, boolean visible, int balance, String iconlink, boolean decimal) {
        JsonObject economyObject = new JsonObject();
        JsonObject cashObject = new JsonObject();

        // Visibility
        cashObject.addProperty( "visible", visible );

        // Amount
        cashObject.addProperty( "balance", balance );


        cashObject.addProperty( "icon", iconlink);

        // Decimal number (Optional)
        if (decimal) {
            JsonObject decimalObject = new JsonObject();
            decimalObject.addProperty("format", "##.##"); // Decimal format
            decimalObject.addProperty("divisor", 100); // The value that divides the balance
            cashObject.add( "decimal", decimalObject );
        }

        // The display type can be "cash" or "bank".
        economyObject.add(type.getKey(), cashObject);

        // Send to LabyMod using the API
        LabyModProtocol.sendLabyModMessage( player, "economy", economyObject );
    }

    public enum EnumBalanceType {
        CASH("cash"),
        BANK("bank");

        private final String key;

        EnumBalanceType(String key) {
            this.key = key;
        }

        public String getKey() {
            return this.key;
        }
    }

    public void setSubtitle(Player receiver, UUID subtitlePlayer, String value) {
        // List of all subtitles
        JsonArray array = new JsonArray();

        // Add subtitle
        JsonObject subtitle = new JsonObject();
        subtitle.addProperty( "uuid", subtitlePlayer.toString() );

        // Optional: Size of the subtitle
        subtitle.addProperty( "size", 0.8d); // Range is 0.8 - 1.6 (1.6 is Minecraft default)

        // no value = remove the subtitle
        if(value != null)
            subtitle.addProperty( "value", value );


        // You can set multiple subtitles in one packet
        array.add(subtitle);

        // Send to LabyMod using the API
        LabyModProtocol.sendLabyModMessage( receiver, "account_subtitle", array );
    }

    public void setSubtitle( Player receiver, UUID subtitlePlayer, String value, Double range) {
        // List of all subtitles
        JsonArray array = new JsonArray();

        // Add subtitle
        JsonObject subtitle = new JsonObject();
        subtitle.addProperty( "uuid", subtitlePlayer.toString() );

        // Optional: Size of the subtitle
        subtitle.addProperty( "size", range); // Range is 0.8 - 1.6 (1.6 is Minecraft default)

        // no value = remove the subtitle
        if(value != null)
            subtitle.addProperty( "value", value );


        // You can set multiple subtitles in one packet
        array.add(subtitle);

        // Send to LabyMod using the API
        LabyModProtocol.sendLabyModMessage( receiver, "account_subtitle", array );
    }

    public void setSubtitle( Player receiver, UUID subtitlePlayer, String value, Double range, Boolean newversionsubtitles) {
        // List of all subtitles
        JsonArray array = new JsonArray();

        // Add subtitle
        JsonObject subtitle = new JsonObject();
        subtitle.addProperty( "uuid", subtitlePlayer.toString() );

        // Optional: Size of the subtitle
        subtitle.addProperty( "size", range); // Range is 0.8 - 1.6 (1.6 is Minecraft default)

        // no value = remove the subtitle
        if(value != null)
            subtitle.addProperty( "value", value );

        // If you want to use the new text format in 1.16+
        if (newversionsubtitles) {
            subtitle.add("raw_json_text", array);
        }


        // You can set multiple subtitles in one packet
        array.add(subtitle);

        // Send to LabyMod using the API
        LabyModProtocol.sendLabyModMessage( receiver, "account_subtitle", array );
    }

    public void sendFlag(Player receiver, UUID uuid, String countryCode) {
        JsonObject flagPacket = new JsonObject();

        // Create array
        JsonArray users = new JsonArray();

        // Add user to array
        JsonObject userObject = new JsonObject();
        userObject.addProperty("uuid", uuid.toString()); // UUID of the flag player
        userObject.addProperty("code", countryCode); // The country code (e.g. "us", "de")
        users.add(userObject);

        // Add array to flag object packet
        flagPacket.add("users", users);

        LabyModProtocol.sendLabyModMessage(receiver, "language_flag", flagPacket);
    }

    public void sendWatermark( Player player, boolean visible ) {
        JsonObject object = new JsonObject();

        // Visibility
        object.addProperty( "visible", visible );

        // Send to LabyMod using the API
        LabyModProtocol.sendLabyModMessage( player, "watermark", object );
    }

    // Costum to use
	/*public void setMiddleClickActions( Player player ) {
	    // List of all action menu entries
	    JsonArray array = new JsonArray();

	    // Add entries
	    JsonObject entry = new JsonObject();
	    entry.addProperty( "displayName", "Kick player" );
	    entry.addProperty( "type", EnumActionType.RUN_COMMAND.name() );
	    entry.addProperty( "value", "kick {name}" ); // {name} will be replaced with the players name
	    array.add(entry);

	    entry = new JsonObject();
	    entry.addProperty( "displayName", "Open shop" );
	    entry.addProperty( "type", EnumActionType.OPEN_BROWSER.name() );
	    entry.addProperty( "value", "https://shop.example.com" );
	    array.add(entry);

	    entry = new JsonObject();
	    entry.addProperty( "displayName", "Copy stats profile url" );
	    entry.addProperty( "type", EnumActionType.CLIPBOARD.name() );
	    entry.addProperty( "value", "https://example.com/stats/{name}" );
	    array.add(entry);

	    entry = new JsonObject();
	    entry.addProperty( "displayName", "Create report" );
	    entry.addProperty( "type", EnumActionType.SUGGEST_COMMAND.name() );
	    entry.addProperty( "value", "report {name} >reason<" );
	    array.add(entry);

	    // Send to LabyMod using the API
	    LabyModProtocol.sendLabyModMessage( player, "user_menu_actions", array );
	}

	enum EnumActionType {
	    NONE,
	    CLIPBOARD,
	    RUN_COMMAND,
	    SUGGEST_COMMAND,
	    OPEN_BROWSER
	}*/

    public void updateGameInfo( Player player, boolean hasGame, String gamemode, long startTime, long endTime ) {

        // Create game json object
        JsonObject obj = new JsonObject();
        obj.addProperty( "hasGame", hasGame );

        if ( hasGame ) {
            obj.addProperty( "game_mode", gamemode );
            obj.addProperty( "game_startTime", startTime ); // Set to 0 for countdown
            obj.addProperty( "game_endTime", endTime ); // // Set to 0 for timer
        }

        // Send to user
        LabyModProtocol.sendLabyModMessage( player, "discord_rpc", obj );
    }

    public void forceEmote( Player receiver, UUID npcUUID, int emoteId ) {
        // List of all forced emotes
        JsonArray array = new JsonArray();

        // Emote and target NPC
        JsonObject forcedEmote = new JsonObject();
        forcedEmote.addProperty( "uuid", npcUUID.toString() );
        forcedEmote.addProperty( "emote_id", emoteId );
        array.add(forcedEmote);

        // Send to LabyMod using the API
        LabyModProtocol.sendLabyModMessage( receiver, "emote_api", array );
    }

    public void sendCurrentPlayingGamemode( Player player, boolean visible, String gamemodeName ) {
        JsonObject object = new JsonObject();
        object.addProperty( "show_gamemode", visible ); // Gamemode visible for everyone
        object.addProperty( "gamemode_name", gamemodeName ); // Name of the current playing gamemode

        // Send to LabyMod using the API
        LabyModProtocol.sendLabyModMessage( player, "server_gamemode", object );
    }

    public void forceSticker( Player receiver, UUID npcUUID, short stickerId ) {
        // List of all forced stickers
        JsonArray array = new JsonArray();

        // Sticker and target NPC
        JsonObject forcedSticker = new JsonObject();
        forcedSticker.addProperty( "uuid", npcUUID.toString() );
        forcedSticker.addProperty( "sticker_id", stickerId );
        array.add(forcedSticker);

        // Send to LabyMod using the API
        LabyModProtocol.sendLabyModMessage( receiver, "sticker_api", array );
    }

    public void sendClientToServer(Player player, String title, String address, boolean preview ) {

        JsonObject object = new JsonObject();
        object.addProperty( "title", title ); // Title of the warning
        object.addProperty( "address", address ); // Destination server address
        object.addProperty( "preview", preview ); // Display the server icon, motd and user count

        // Send to LabyMod using the API
        LabyModProtocol.sendLabyModMessage( player, "server_switch", object );
    }

    /**
     * Just send this packet to set the cinescope coverage
     *  0% - Disabled
     * 50% - Fully blind
     */
    public void sendCineScope( Player player, int coveragePercent, long duration ) {
        JsonObject object = new JsonObject();

        // Cinescope height (0% - 50%)
        object.addProperty( "coverage", coveragePercent );

        // Duration
        object.addProperty( "duration", duration );

        // Send to LabyMod using the API
        LabyModProtocol.sendLabyModMessage( player, "cinescopes", object );
    }

    public void sendServerBanner(Player player, String imageUrl) {
        JsonObject object = new JsonObject();
        object.addProperty("url", imageUrl); // Url of the image
        LabyModProtocol.sendLabyModMessage(player, "server_banner", object);
    }

}
