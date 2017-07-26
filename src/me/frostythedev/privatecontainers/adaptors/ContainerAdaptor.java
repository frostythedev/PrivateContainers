package me.frostythedev.privatecontainers.adaptors;

import com.google.gson.*;
import me.frostythedev.privatecontainers.containers.ContainerType;
import me.frostythedev.privatecontainers.containers.Container;
import me.frostythedev.privatecontainers.utils.GsonAdaptor;
import me.frostythedev.privatecontainers.utils.LocationUtils;
import org.bukkit.Location;

import java.lang.reflect.Type;
import java.util.UUID;

public class ContainerAdaptor implements GsonAdaptor<Container> {

    @Override
    public Container deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        UUID owner = UUID.fromString(jsonObject.get("owner").getAsString());
        boolean locked = jsonObject.get("locked").getAsBoolean();
        ContainerType containerType = ContainerType.valueOf(jsonObject.get("containerType").getAsString());
        Location location = LocationUtils.fromString(jsonObject.get("location").getAsString());

        return new Container(owner, locked, containerType, location);
    }

    @Override
    public JsonElement serialize(Container container, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("owner", container.getOwner().toString());
        jsonObject.addProperty("locked", container.isLocked());
        jsonObject.addProperty("containerType", container.getContainerType().name());
        jsonObject.addProperty("location", LocationUtils.toString(container.getLocation()));

        return jsonObject;
    }
}
