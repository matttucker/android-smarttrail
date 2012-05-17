/*
 * Copyright 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.geozen.smarttrail.util;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.geozen.smarttrail.R;

/**
 * An extension of {@link ActionBarHelper} that provides Android 3.0-specific
 * functionality for Honeycomb tablets. It thus requires API level 11.
 */
public class ActionBarHelperHoneycomb extends ActionBarHelper {
	private Menu mOptionsMenu;
	private View mRefreshIndeterminateProgressView = null;
	private ImageButton mProviderView;

	protected ActionBarHelperHoneycomb(Activity activity) {
		super(activity);

	}

	@Override
	public void setDisplayHomeAsUpEnabled(boolean enabled) {
		ActionBar actionBar = mActivity.getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(enabled);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		mOptionsMenu = menu;
		boolean result = super.onCreateOptionsMenu(menu);

		return result;
	}

	@Override
	public void setRefreshActionItemState(boolean refreshing) {
		// On Honeycomb, we can set the state of the refresh button by giving it
		// a custom
		// action view.
		if (mOptionsMenu == null) {
			return;
		}

		final MenuItem refreshItem = mOptionsMenu.findItem(R.id.menu_refresh);
		if (refreshItem != null) {
			if (refreshing) {
				if (mRefreshIndeterminateProgressView == null) {
					LayoutInflater inflater = (LayoutInflater) getActionBarThemedContext()
							.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					mRefreshIndeterminateProgressView = inflater.inflate(
							R.layout.actionbar_indeterminate_progress, null);
				}

				refreshItem.setActionView(mRefreshIndeterminateProgressView);
			} else {
				refreshItem.setActionView(null);
			}
		}
	}

	public ImageButton getProviderView() {
		return mProviderView;
	}

	/**
	 * Returns a {@link Context} suitable for inflating layouts for the action
	 * bar. The implementation for this method in {@link ActionBarHelperICS}
	 * asks the action bar for a themed context.
	 */
	protected Context getActionBarThemedContext() {
		return mActivity;
	}

	@Override
	public void setActionBarTitle(CharSequence title) {
		ActionBar actionBar = mActivity.getActionBar();
		actionBar.setTitle(title);
	}
}
