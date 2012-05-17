/*
 * Copyright 2011 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.geozen.smarttrail.ui.phone;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

import com.geozen.smarttrail.ui.AreasFragment;
import com.geozen.smarttrail.ui.BaseSinglePaneActivity;
import com.geozen.smarttrail.util.ActionBarHelper;

public class AreasActivity extends BaseSinglePaneActivity {
	@Override
	protected Fragment onCreatePane() {
		return new AreasFragment();
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		// mkt
		// getActivityHelper().setupSubActivity();
		//final SmartTrailApplication app = (SmartTrailApplication) getApplication();
		ActionBarHelper abh = getActionBarHelper();
		// abh.setActionBarTitle(app.getRegionName());
		abh.setActionBarTitle("Trail Areas");
		abh.setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		

		return super.onOptionsItemSelected(item);
	}
}
