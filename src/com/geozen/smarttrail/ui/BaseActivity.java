/**
 * GeoZen LLC
 * Copyright 2011. All rights reserved.
 */

package com.geozen.smarttrail.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.MenuItem;

import com.geozen.smarttrail.R;
import com.geozen.smarttrail.service.SyncService;
import com.geozen.smarttrail.util.ActionBarActivity;
import com.geozen.smarttrail.util.DetachableResultReceiver;

/**
 * A base activity that defers common functionality across app activities to an
 * {@link ActivityHelper}. This class shouldn't be used directly; instead, activities should
 * inherit from {@link BaseSinglePaneActivity} or {@link BaseMultiPaneActivity}.
 */
public abstract class BaseActivity extends ActionBarActivity implements OnRefreshStatusListener {


    private SyncStatusUpdaterFragment mSyncStatusUpdaterFragment;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	

		final FragmentManager fm = getSupportFragmentManager();
	

		mSyncStatusUpdaterFragment = (SyncStatusUpdaterFragment) fm
				.findFragmentByTag(SyncStatusUpdaterFragment.TAG);
		if (mSyncStatusUpdaterFragment == null) {
			mSyncStatusUpdaterFragment = new SyncStatusUpdaterFragment();
			fm.beginTransaction()
					.add(mSyncStatusUpdaterFragment,
							SyncStatusUpdaterFragment.TAG).commit();

		}
		
	}
    
   
    
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	if (item.getItemId() == R.id.menu_refresh) {
			triggerRefresh();
			return true;
		}
        return super.onOptionsItemSelected(item);
    }

  

    /**
     * Takes a given intent and either starts a new activity to handle it (the default behavior),
     * or creates/updates a fragment (in the case of a multi-pane activity) that can handle the
     * intent.
     *
     * Must be called from the main (UI) thread.
     */
    public void openActivityOrFragment(Intent intent) {
        // Default implementation simply calls startActivity
        startActivity(intent);
    }

    /**
     * Converts an intent into a {@link Bundle} suitable for use as fragment arguments.
     */
    public static Bundle intentToFragmentArguments(Intent intent) {
        Bundle arguments = new Bundle();
        if (intent == null) {
            return arguments;
        }

        final Uri data = intent.getData();
        if (data != null) {
            arguments.putParcelable("_uri", data);
        }

        final Bundle extras = intent.getExtras();
        if (extras != null) {
            arguments.putAll(intent.getExtras());
        }

        return arguments;
    }

    /**
     * Converts a fragment arguments bundle into an intent.
     */
    public static Intent fragmentArgumentsToIntent(Bundle arguments) {
        Intent intent = new Intent();
        if (arguments == null) {
            return intent;
        }

        final Uri data = arguments.getParcelable("_uri");
        if (data != null) {
            intent.setData(data);
        }

        intent.putExtras(arguments);
        intent.removeExtra("_uri");
        return intent;
    }
    
    
	protected void triggerRefresh() {
		final Intent intent = new Intent(Intent.ACTION_SYNC, null, this,
				SyncService.class);
		intent.putExtra(SyncService.EXTRA_STATUS_RECEIVER,
				mSyncStatusUpdaterFragment.mReceiver);
		startService(intent);

	}
    
	/**
	 * A non-UI fragment, retained across configuration changes, that updates
	 * its activity's UI when sync status changes.
	 */
	public static class SyncStatusUpdaterFragment extends Fragment implements
			DetachableResultReceiver.Receiver {
		public static final String TAG = SyncStatusUpdaterFragment.class
				.getName();

		private boolean mSyncing = false;
		private DetachableResultReceiver mReceiver;

		public SyncStatusUpdaterFragment() {
			super();
			mReceiver = new DetachableResultReceiver(new Handler());
			mReceiver.setReceiver(this);
		}
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setRetainInstance(true);
		
		}

		/** {@inheritDoc} */
		public void onReceiveResult(int resultCode, Bundle resultData) {
			Activity activity = getActivity();
			if (activity == null) {
				return;
			}

			switch (resultCode) {
			case SyncService.STATUS_RUNNING: {
				mSyncing = true;
				break;
			}
			case SyncService.STATUS_FINISHED: {
				mSyncing = false;
				break;
			}
			case SyncService.STATUS_ERROR: {
				// Error happened down in SyncService, show as toast.
				mSyncing = false;
				final String errorText = getString(R.string.toast_sync_error,
						resultData.getString(Intent.EXTRA_TEXT));
				//Toast.makeText(activity, errorText, Toast.LENGTH_LONG).show();
				break;
			}
			}

			((OnRefreshStatusListener) activity)
			.onRefreshStatusChange(mSyncing);
			
		}

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);
			((OnRefreshStatusListener) getActivity())
					.onRefreshStatusChange(mSyncing);
			
		}
	}
	
	
	public void onRefreshStatusChange(boolean status) {
		getActionBarHelper().setRefreshActionItemState(status);
		//getActivityHelper().setRefreshActionButtonCompatState(status);
	}
}
