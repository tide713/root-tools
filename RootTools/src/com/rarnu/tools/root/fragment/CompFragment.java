package com.rarnu.tools.root.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.content.Loader;
import android.content.Loader.OnLoadCompleteListener;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

import com.rarnu.devlib.base.BaseFragment;
import com.rarnu.devlib.component.DataProgressBar;
import com.rarnu.tools.root.GlobalInstance;
import com.rarnu.tools.root.MainActivity;
import com.rarnu.tools.root.R;
import com.rarnu.tools.root.adapter.CompPackageAdapter;
import com.rarnu.tools.root.common.MenuItemIds;
import com.rarnu.tools.root.fragmentactivity.CompPackageInfoActivity;
import com.rarnu.tools.root.loader.CompLoader;

public class CompFragment extends BaseFragment implements OnItemClickListener,
		OnLoadCompleteListener<List<PackageInfo>>, OnQueryTextListener {

	ListView lvComp;
	DataProgressBar progressComp;

	List<PackageInfo> listCompAll = new ArrayList<PackageInfo>();
	CompPackageAdapter compAdapter = null;
	CompLoader loader = null;
	
	MenuItem itemSearch;
	MenuItem menuRefresh;

	@Override
	protected int getBarTitle() {
		return R.string.func4_title;
	}

	@Override
	protected int getBarTitleWithPath() {
		return R.string.func4_title_with_path;
	}

	@Override
	protected void initComponents() {
		progressComp = (DataProgressBar) innerView
				.findViewById(R.id.progressComp);
		lvComp = (ListView) innerView.findViewById(R.id.lvComp);
		compAdapter = new CompPackageAdapter(getActivity(), listCompAll);
		lvComp.setAdapter(compAdapter);
		loader = new CompLoader(getActivity());
		
		
	}

	@Override
	protected void initLogic() {
		doStartLoad();
	}

	@Override
	protected int getFragmentLayoutResId() {
		return R.layout.layout_comp;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}

	@Override
	protected void initMenu(Menu menu) {
		itemSearch = menu.add(0, MenuItemIds.MENU_SEARCH, 98,
				R.string.search);
		itemSearch.setIcon(android.R.drawable.ic_menu_search);
		itemSearch.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		SearchView sv = new SearchView(getActivity());
		sv.setOnQueryTextListener(this);
		itemSearch.setActionView(sv);

		menuRefresh = menu.add(0, MenuItemIds.MENU_REFRESH, 99,
				R.string.refresh);
		menuRefresh.setIcon(android.R.drawable.ic_menu_revert);
		menuRefresh.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MenuItemIds.MENU_REFRESH:
			doStartLoad();
			break;
		}
		return true;
	}

	private void doStartLoad() {
		progressComp.setAppName(getString(R.string.loading));
		progressComp.setVisibility(View.VISIBLE);
		loader.startLoading();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		GlobalInstance.currentComp = (PackageInfo) lvComp
				.getItemAtPosition(position);

		Intent inPackage = new Intent(getActivity(),
				CompPackageInfoActivity.class);
		startActivity(inPackage);

	}

	@Override
	public void onLoadComplete(Loader<List<PackageInfo>> loader,
			List<PackageInfo> data) {
		listCompAll.clear();
		if (data != null) {
			listCompAll.addAll(data);
		}
		compAdapter.setNewList(listCompAll);
		progressComp.setVisibility(View.GONE);

	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		return false;
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		if (compAdapter != null) {
			compAdapter.getFilter().filter(newText);
		}
		return false;
	}

	@Override
	protected void initEvents() {
		lvComp.setOnItemClickListener(this);
		loader.registerListener(0, this);
	}

	@Override
	protected String getMainActivityName() {
		return MainActivity.class.getName();
	}

	@Override
	protected void onGetNewArguments(Bundle bn) {
		
	}

}
