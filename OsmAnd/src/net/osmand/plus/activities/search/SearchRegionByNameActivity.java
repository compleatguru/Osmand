package net.osmand.plus.activities.search;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;

import android.view.View;
import android.widget.AdapterView;
import net.osmand.access.AccessibleToast;
import net.osmand.data.LatLon;
import net.osmand.plus.OsmandApplication;
import net.osmand.plus.OsmandSettings;
import net.osmand.plus.R;
import net.osmand.plus.helpers.FileNameTranslationHelper;
import net.osmand.plus.resources.RegionAddressRepository;
import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;


public class SearchRegionByNameActivity extends SearchByNameAbstractActivity<RegionAddressRepository> {
	private OsmandSettings osmandSettings;

	@Override
	protected Comparator<? super RegionAddressRepository> createComparator() {
		return new Comparator<RegionAddressRepository>() {
			Collator col = Collator.getInstance();
			@Override
			public int compare(RegionAddressRepository lhs,
					RegionAddressRepository rhs) {
				return col.compare(lhs.getName(), rhs.getName());
			}
		};
	}

	@Override
	protected void reset() {
		//This is really only a "clear input text field", hence do not reset settings here
		//osmandSettings.setLastSearchedRegion("", null);
		super.reset();
	}

	@Override
	protected LatLon getLocation(RegionAddressRepository item) {
		return item.getEstimatedRegionCenter();
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setLabelText(R.string.choose_available_region);
		if(((OsmandApplication)getApplication()).getResourceManager().getAddressRepositories().isEmpty()){
			AccessibleToast.makeText(this, R.string.none_region_found, Toast.LENGTH_LONG).show();
		}
		initialListToFilter = new ArrayList<RegionAddressRepository>(((OsmandApplication)getApplication()).getResourceManager().getAddressRepositories());
		NamesAdapter namesAdapter = new NamesAdapter(new ArrayList<RegionAddressRepository>(initialListToFilter),createComparator()); //$NON-NLS-1$
		setListAdapter(namesAdapter);
	}
	
	
	@Override
	public String getText(RegionAddressRepository obj) {
		return FileNameTranslationHelper.getFileName(this,
				getMyApplication().getResourceManager().getOsmandRegions(), obj.getName());
	}

	@Override
	public void itemSelected(RegionAddressRepository obj) {
		((OsmandApplication) getApplication()).getSettings().setLastSearchedRegion(obj.getName(), obj.getEstimatedRegionCenter());
		quitActivity(SearchCityByNameActivity.class);
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

	}
}
