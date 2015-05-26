package com.llkj.newbjia.chat;

import java.util.Comparator;

import com.llkj.db.RecentlyContacts;
import com.llkj.newbjia.utils.StringUtil;

@SuppressWarnings("rawtypes")
public class TimeComparator implements Comparator<RecentlyContacts> {

	@SuppressWarnings("unchecked")
	public int compare(RecentlyContacts o1, RecentlyContacts o2) {

		String time1 = o1.getMessageDate();
		String time2 = o2.getMessageDate();
		if (!StringUtil.isEmpty(time1) && !StringUtil.isEmpty(time2)) {
			long inttimeone = Long.parseLong(time1);
			long inttimetwo = Long.parseLong(time2);
			if (inttimeone < inttimetwo) {
				return 1;
			} else if (inttimeone > inttimetwo) {
				return -1;
			} else {
				return 0;
			}
		}
		return -1;
	}

}
