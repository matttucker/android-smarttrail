package com.geozen.smarttrail.io;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentResolver;

import com.geozen.smarttrail.http.SmartTrailApiV1;
import com.geozen.smarttrail.model.Area;

public class AreasHandler extends JsonHandler {

	@Override
	public void parseAndApply(String json, ContentResolver resolver) throws JSONException {

		JSONObject data = new JSONObject(json);
		JSONArray areas = data.getJSONArray(SmartTrailApiV1.AREAS);

		for (int i = 0; i < areas.length(); i++) {
			Area area = new Area(areas.getJSONObject(i));
			area.upsert(resolver);
		}
	}

}
