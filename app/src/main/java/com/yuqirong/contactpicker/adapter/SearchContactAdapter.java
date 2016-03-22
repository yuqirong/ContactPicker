package com.yuqirong.contactpicker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.yuqirong.contactpicker.R;
import com.yuqirong.contactpicker.entity.Contact;

import java.util.List;


public class SearchContactAdapter extends ArrayAdapter<Contact> {

	private int resource;

	public SearchContactAdapter(Context context, int resource,
			List<Contact> objects) {
		super(context, resource, objects);
		this.resource = resource;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Contact contact = getItem(position);
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(resource,
					null);
		}
		TextView tv_contact_name = (TextView) convertView
				.findViewById(R.id.tv_contact_name);
		tv_contact_name.setText(contact.getName());
		return convertView;
	}

}
