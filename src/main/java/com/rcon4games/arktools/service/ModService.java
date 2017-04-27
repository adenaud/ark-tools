package com.rcon4games.arktools.service;

import com.rcon4games.arktools.api.NotifuseAPI;
import com.rcon4games.arktools.api.NotifuseException;
import com.rcon4games.arktools.dao.ModDao;
import com.rcon4games.arktools.model.Mod;
import com.rcon4games.arktools.api.SteamAPI;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Created by Anthony Denaud on 27/04/17.
 * Copyright Personalized-Software Ltd
 */
@Service
@Scope("singleton")
public class ModService {

	@Autowired
	private ModDao modDao;

	@Autowired
	private SteamAPI steamAPI;

	@Autowired
	private NotifuseAPI notifuseAPI;

	public List<Mod> getAll(){
		return (List<Mod>) modDao.findAll();
	}

	public void checkAll(){

		List<Mod> mods = getAll();
		int availableUpdates = 0;

		for (Mod mod : mods){
			try {

				DateTime previousLastUpdateDate = new DateTime(mod.getLastUpdate()).withZone(DateTimeZone.UTC);
				DateTime lastUpdate = steamAPI.getModLastUpdate(mod.getSteamId());
				mod.setLastChecked(new Date());


				if(lastUpdate.isAfter(previousLastUpdateDate)){
					System.out.println(String.format("An update is available for %s (%d)", mod.getName(), mod.getId()));

					JSONObject data = new JSONObject();
					data.put("modID", String.valueOf(mod.getSteamId()));
					data.put("modName", String.valueOf(mod.getName()));
					notifuseAPI.sendMessage("anthonydenaud@gmail.com", null, null, "mod-update", data);

					mod.setLastUpdate(lastUpdate.toDate());
					availableUpdates++;
				}

				modDao.save(mod);

			}catch (IOException e){
				System.out.println("Unable to check last update for mod " + String.valueOf(mod.getSteamId()));
			} catch (NotifuseException e) {
				System.out.println(e.getMessage());
			}
		}

		if(availableUpdates == 0){
			System.out.println("Not updates available.");
		}
	}
}
