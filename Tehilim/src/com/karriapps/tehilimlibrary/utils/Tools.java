package com.karriapps.tehilimlibrary.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.karriapps.tehilimlibrary.R;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.ActionProvider;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import android.widget.ListView;

public class Tools {

	public static String reverse(String vers)
	{
		String temp = "";
		char[] ch = vers.toCharArray();
		for(int i = ch.length - 1; i >= 0; i--)
		{
			temp += ch[i];
		}
		
		return temp;	
	}
	
	public static void updateListViewHeight(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
		listView.requestLayout();
	}
	
	public static void setListViewHeight(ExpandableListView listView, int group) {
		ExpandableListAdapter listAdapter = listView.getExpandableListAdapter();
		int totalHeight = 0;
		int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(),
				MeasureSpec.AT_MOST);
		for (int i = 0; i < listAdapter.getGroupCount(); i++) {
			View groupItem = listAdapter.getGroupView(i, false, null, listView);
			groupItem.measure(desiredWidth, MeasureSpec.UNSPECIFIED);

			totalHeight += groupItem.getMeasuredHeight();

			if (((listView.isGroupExpanded(i)) && (i != group))
					|| ((!listView.isGroupExpanded(i)) && (i == group))) {
				for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
					View listItem = listAdapter.getChildView(i, j, false, null,
							listView);
					listItem.measure(desiredWidth, MeasureSpec.UNSPECIFIED);

					totalHeight += listItem.getMeasuredHeight();

				}
			}
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		int height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
		if (height < 10)
			height = 200;
		params.height = height;
		listView.setLayoutParams(params);
		listView.requestLayout();
	}
		
	/**
	 * Show you the free user dialog to encourage him to buy the app
	 * @param c
	 * @param onCancel
	 */
	public static void ShowFreeDialog(final Context c, OnCancelListener onCancel)
	{
		AlertDialog.Builder d = new Builder(c);
		d.setTitle(R.string.free_title);
		d.setMessage(R.string.free_desc);
		if(onCancel != null)
			d.setOnCancelListener(onCancel);
		d.setPositiveButton(R.string.ok, new OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				c.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.karriapps.tehilim")));
			}
		});
		d.show();
	}
	
	/**
	 * Checks if the String received is a numeric
	 * @param str
	 * @return
	 */
	public static boolean isDoubleNumeric(String str)
	{
		try
		{
			@SuppressWarnings("unused")
			double d = Double.parseDouble(str);
		}
		catch(Exception ex)
		{
			return false;
		}
		return true;
	}
	
	/**
	 * Checks if the String received is a numeric
	 * @param str
	 * @return
	 */
	public static boolean isIntNumeric(String str)
	{
		try
		{
			@SuppressWarnings("unused")
			double d = Integer.parseInt(str);
		}
		catch(Exception ex)
		{
			return false;
		}
		return true;
	}
	
	public static String getKufYudLetter(int kufYud){
		if(kufYud <= 10)
			return new Alef(kufYud).toString();
		else if(kufYud <= 18){
			int hefreshMiEser = (kufYud - 10) + 1;
			return new Alef(10 * hefreshMiEser).toString();
		}
		else{
			int hefreshMi18 = kufYud - 18;
			return new Alef(100 * hefreshMi18).toString();
		}
	}
	
    public static int getCharVal(char letter) {
        switch(letter) {
                case 'א': return 1;
                case 'ב': return 2;
                case 'ג': return 3;
                case 'ד': return 4;
                case 'ה': return 5;
                case 'ו': return 6;
                case 'ז': return 7;
                case 'ח': return 8;
                case 'ט': return 9;
                case 'י': return 10;
                case 'כ': 
                case 'ך': return 20;
                case 'ל': return 30;
                case 'מ': 
                case 'ם': return 40;
                case 'נ': 
                case 'ן': return 50;
                case 'ס': return 60;
                case 'ע': return 70;
                case 'פ': 
                case 'ף':return 80;
                case 'צ': 
                case 'ץ': return 90;
                case 'ק': return 100;
                case 'ר': return 200;
                case 'ש': return 300;
                case 'ת': return 400;
                default:  return 0;
        }
    }
    
    public static int getCharValOrdinal(char letter) {
        switch(letter) {
	        case 'א': return 1;
	        case 'ב': return 2;
	        case 'ג': return 3;
	        case 'ד': return 4;
	        case 'ה': return 5;
	        case 'ו': return 6;
	        case 'ז': return 7;
	        case 'ח': return 8;
	        case 'ט': return 9;
	        case 'י': return 10;
	        case 'כ': return 11;
	        case 'ל': return 12;
	        case 'מ': return 13;
	        case 'נ': return 14;
	        case 'ס': return 15;
	        case 'ע': return 16;
	        case 'פ': return 17;
	        case 'צ': return 18;
	        case 'ק': return 19;
	        case 'ר': return 20;
	        case 'ש': return 21;
	        case 'ת': return 22;
                default:  return 0;
        }
    }
    
	public static String convertStreamToString(InputStream is) throws IOException {
	    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	    StringBuilder sb = new StringBuilder();
	    String line = null;
	    while ((line = reader.readLine()) != null) {
	      sb.append(line).append("\n");
	    }
	    is.close();
	    return sb.toString();
	}
		
	   public static android.view.MenuItem getMenuItem(final MenuItem item) {
		      return new android.view.MenuItem() {
		         @Override
		         public int getItemId() {
		            return item.getItemId();
		         }

		         public boolean isEnabled() {
		            return true;
		         }

		         @Override
		         public boolean collapseActionView() {
		            // TODO Auto-generated method stub
		            return false;
		         }

		         @Override
		         public boolean expandActionView() {
		            // TODO Auto-generated method stub
		            return false;
		         }

		         @Override
		         public ActionProvider getActionProvider() {
		            // TODO Auto-generated method stub
		            return null;
		         }

		         @Override
		         public View getActionView() {
		            // TODO Auto-generated method stub
		            return null;
		         }

		         @Override
		         public char getAlphabeticShortcut() {
		            // TODO Auto-generated method stub
		            return 0;
		         }

		         @Override
		         public int getGroupId() {
		            // TODO Auto-generated method stub
		            return 0;
		         }

		         @Override
		         public Drawable getIcon() {
		            // TODO Auto-generated method stub
		            return null;
		         }

		         @Override
		         public Intent getIntent() {
		            // TODO Auto-generated method stub
		            return null;
		         }

		         @Override
		         public ContextMenuInfo getMenuInfo() {
		            // TODO Auto-generated method stub
		            return null;
		         }

		         @Override
		         public char getNumericShortcut() {
		            // TODO Auto-generated method stub
		            return 0;
		         }

		         @Override
		         public int getOrder() {
		            // TODO Auto-generated method stub
		            return 0;
		         }

		         @Override
		         public SubMenu getSubMenu() {
		            // TODO Auto-generated method stub
		            return null;
		         }

		         @Override
		         public CharSequence getTitle() {
		            // TODO Auto-generated method stub
		            return null;
		         }

		         @Override
		         public CharSequence getTitleCondensed() {
		            // TODO Auto-generated method stub
		            return null;
		         }

		         @Override
		         public boolean hasSubMenu() {
		            // TODO Auto-generated method stub
		            return false;
		         }

		         @Override
		         public boolean isActionViewExpanded() {
		            // TODO Auto-generated method stub
		            return false;
		         }

		         @Override
		         public boolean isCheckable() {
		            // TODO Auto-generated method stub
		            return false;
		         }

		         @Override
		         public boolean isChecked() {
		            // TODO Auto-generated method stub
		            return false;
		         }

		         @Override
		         public boolean isVisible() {
		            // TODO Auto-generated method stub
		            return false;
		         }

		         @Override
		         public android.view.MenuItem setActionProvider(ActionProvider actionProvider) {
		            // TODO Auto-generated method stub
		            return null;
		         }

		         @Override
		         public android.view.MenuItem setActionView(View view) {
		            // TODO Auto-generated method stub
		            return null;
		         }

		         @Override
		         public android.view.MenuItem setActionView(int resId) {
		            // TODO Auto-generated method stub
		            return null;
		         }

		         @Override
		         public android.view.MenuItem setAlphabeticShortcut(char alphaChar) {
		            // TODO Auto-generated method stub
		            return null;
		         }

		         @Override
		         public android.view.MenuItem setCheckable(boolean checkable) {
		            // TODO Auto-generated method stub
		            return null;
		         }

		         @Override
		         public android.view.MenuItem setChecked(boolean checked) {
		            // TODO Auto-generated method stub
		            return null;
		         }

		         @Override
		         public android.view.MenuItem setEnabled(boolean enabled) {
		            // TODO Auto-generated method stub
		            return null;
		         }

		         @Override
		         public android.view.MenuItem setIcon(Drawable icon) {
		            // TODO Auto-generated method stub
		            return null;
		         }

		         @Override
		         public android.view.MenuItem setIcon(int iconRes) {
		            // TODO Auto-generated method stub
		            return null;
		         }

		         @Override
		         public android.view.MenuItem setIntent(Intent intent) {
		            // TODO Auto-generated method stub
		            return null;
		         }

		         @Override
		         public android.view.MenuItem setNumericShortcut(char numericChar) {
		            // TODO Auto-generated method stub
		            return null;
		         }

		         @Override
		         public android.view.MenuItem setOnActionExpandListener(OnActionExpandListener listener) {
		            // TODO Auto-generated method stub
		            return null;
		         }

		         @Override
		         public android.view.MenuItem setOnMenuItemClickListener(OnMenuItemClickListener menuItemClickListener) {
		            // TODO Auto-generated method stub
		            return null;
		         }

		         @Override
		         public android.view.MenuItem setShortcut(char numericChar, char alphaChar) {
		            // TODO Auto-generated method stub
		            return null;
		         }

		         @Override
		         public void setShowAsAction(int actionEnum) {
		            // TODO Auto-generated method stub

		         }

		         @Override
		         public android.view.MenuItem setShowAsActionFlags(int actionEnum) {
		            // TODO Auto-generated method stub
		            return null;
		         }

		         @Override
		         public android.view.MenuItem setTitle(CharSequence title) {
		            // TODO Auto-generated method stub
		            return null;
		         }

		         @Override
		         public android.view.MenuItem setTitle(int title) {
		            // TODO Auto-generated method stub
		            return null;
		         }

		         @Override
		         public android.view.MenuItem setTitleCondensed(CharSequence title) {
		            // TODO Auto-generated method stub
		            return null;
		         }

		         @Override
		         public android.view.MenuItem setVisible(boolean visible) {
		            // TODO Auto-generated method stub
		            return null;
		         }
		      };
		   }
}
