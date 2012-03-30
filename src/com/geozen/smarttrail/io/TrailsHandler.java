package com.geozen.smarttrail.io;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentResolver;

import com.geozen.smarttrail.model.Trail;
import com.geozen.smarttrail.http.SmartTrailApiV1;



public class TrailsHandler extends JsonHandler {

	@Override
	public void parseAndApply(String json, ContentResolver resolver) throws JSONException {

		JSONObject data = new JSONObject(json);
		JSONArray trails = data.getJSONArray(SmartTrailApiV1.TRAILS);

		for (int i = 0; i < trails.length(); i++) {
			Trail trail = new Trail(trails.getJSONObject(i));
			trail.upsert(resolver);
		}
	}

}
