package me.frostythedev.privatecontainers.adaptors;

import com.google.common.collect.Lists;
import com.google.gson.*;
import me.frostythedev.privatecontainers.containers.Container;
import me.frostythedev.privatecontainers.containers.ContainerType;
import me.frostythedev.privatecontainers.containers.SpecialContainer;
import me.frostythedev.privatecontainers.utils.GsonAdaptor;
import me.frostythedev.privatecontainers.utils.LocationUtils;
import org.bukkit.Location;

import java.lang.reflect.Type;
import java.util.List;
import java.util.UUID;

public class SpecialContainerAdaptor implements GsonAdaptor<SpecialContainer> {

    @Override
    public SpecialContainer deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        UUID owner = UUID.fromString(jsonObject.get("owner").getAsString());
        boolean locked = jsonObject.get("locked").getAsBoolean();
        ContainerType containerType = ContainerType.valueOf(jsonObject.get("containerType").getAsString());
        String locations = jsonObject.get("locations").getAsString();

        List<Location> locationList = Lists.newArrayList();
        if(!locations.contains("#")){
            locationList.add(LocationUtils.fromString(locations));
        }else{
            for(String locData : locations.split("#")){
                Location loc = LocationUtils.fromString(locData);
                if(!locationList.contains(loc)){
                    locationList.add(loc);
                }
            }
        }

        return new SpecialContainer(owner, locked, containerType, locationList);
    }

    @Override
    public JsonElement serialize(SpecialContainer container, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("owner", container.getOwner().toString());
        jsonObject.addProperty("locked", container.isLocked());
        jsonObject.addProperty("containerType", container.getContainerType().name());

        String locs = "";
        if(!container.getLocations().isEmpty()){
            for(Location l : container.getLocations()){
                if(locs.equals("")){
                    locs+=LocationUtils.toString(l);
                }else{
                    locs+="#" + LocationUtils.toString(l);
                }
            }
        }
        jsonObject.addProperty("locations", locs);

        return jsonObject;
    }
}
