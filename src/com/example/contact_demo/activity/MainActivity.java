package com.example.contact_demo.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AlphabetIndexer;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.example.contact_demo.R;
import com.example.contact_demo.adapter.ContactAdapter;
import com.example.contact_demo.adapter.SearchContactAdapter;
import com.example.contact_demo.entity.Contact;
import com.example.contact_demo.util.PinYin;
import com.example.contact_demo.view.QuickIndexBar;
import com.example.contact_demo.view.QuickIndexBar.OnIndexChangeListener;

public class MainActivity extends ActionBarActivity {

	private ListView lv_contact;
	private QuickIndexBar qib;
	private TextView tv_center;
	private EditText et_search;
	private TextView tv_no_contact;

	private ArrayAdapter<Contact> adapter;
	private ArrayAdapter<Contact> searchAdapter;
	private List<Contact> list = new ArrayList<Contact>();
	public static final Uri URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
	private SectionIndexer mIndexer;
	private SearchTask searchTask;

	/**
	 * 定义字母表的排序规则
	 */
	private String alphabet = "#ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
	}

	private void initView() {
		list.clear();
		lv_contact = (ListView) findViewById(R.id.lv_contact);
		qib = (QuickIndexBar) findViewById(R.id.qib);
		tv_center = (TextView) findViewById(R.id.tv_center);
		et_search = (EditText) findViewById(R.id.et_search);
		tv_no_contact = (TextView) findViewById(R.id.tv_no_contact);
		getContactArray();
		adapter = new ContactAdapter(MainActivity.this, R.layout.list_item,
				list, mIndexer);
		searchAdapter = new SearchContactAdapter(MainActivity.this,
				R.layout.list_item, filterList);
		lv_contact.setAdapter(adapter);
		qib.setOnIndexChangeListener(new OnIndexChangeListener() {
			@Override
			public void onIndexChange(int section) {
				int position = mIndexer.getPositionForSection(section);
				lv_contact.setSelection(position);
				tv_center.setText(QuickIndexBar.INDEX_ARRAYS[section]);
				tv_center.setVisibility(View.VISIBLE);
			}

			@Override
			public void onActionUp() {
				tv_center.setVisibility(View.GONE);
			}
		});
		// 文本编辑框内容改变时
		et_search.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				String searchString = s.toString().trim()
						.toLowerCase(Locale.US);
				if (searchTask != null
						&& searchTask.getStatus() != AsyncTask.Status.FINISHED) {
					// 若之前有task，先取消搜索task
					searchTask.cancel(true);
				}
				// 开启搜索task
				searchTask = new SearchTask();
				searchTask.execute(searchString);
			}
		});
	}

	// 搜索出的联系人列表
	List<Contact> filterList = new ArrayList<Contact>();
	// 是否为搜索模式
	boolean searchMode;

	class SearchTask extends AsyncTask<String, Void, Void> {

		@Override
		protected Void doInBackground(String... params) {
			filterList.clear();
			searchMode = params[0].length() > 0;
			if (searchMode) {
				for (Contact contact : list) {
					boolean isChinese = contact.getName()
							.toLowerCase(Locale.US).indexOf(params[0]) > -1;
					boolean isPinyin = contact.getPinyinName().indexOf(
							params[0]) > -1;
					if (isPinyin || isChinese) {
						filterList.add(contact);
					}
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (searchMode) {
				if (filterList.size() > 0) {
					lv_contact.setAdapter(searchAdapter);
				} else {
					lv_contact.setVisibility(View.GONE);
					tv_no_contact.setVisibility(View.VISIBLE);
				}
				qib.setVisibility(View.GONE);
			} else {
				tv_no_contact.setVisibility(View.GONE);
				lv_contact.setVisibility(View.VISIBLE);
				lv_contact.setAdapter(adapter);
				qib.setVisibility(View.VISIBLE);
			}
		}

	}

	private List<Contact> getContactArray() {
		Cursor cursor = getContentResolver().query(URI,
				new String[] { "display_name", "sort_key", "phonebook_label" },
				null, null, "phonebook_label");
		Contact contact;
		if (cursor.moveToFirst()) {
			do {
				contact = new Contact();
				String contact_name = cursor.getString(0);
				String phonebook_label = cursor.getString(2);
				contact.setPhonebookLabel(getPhonebookLabel(phonebook_label));
				contact.setPinyinName(PinYin.getPinYin(contact_name));
				contact.setName(contact_name);
				list.add(contact);
			} while (cursor.moveToNext());
		}
		// 实例化indexer
		mIndexer = new AlphabetIndexer(cursor, 2, alphabet);
		return list;
	}

	private String getPhonebookLabel(String phonebook_label) {
		if (phonebook_label.matches("[A-Z]")) {
			return phonebook_label;
		}
		return "#";
	}

}
