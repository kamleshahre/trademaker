package org.lifeform.util;

public class Sort {
	public static void dHeapSort(final double key[], final double trail[]) {
		int nkey = key.length;
		int last_parent_pos = (nkey - 2) / 2;
		int last_parent_index = last_parent_pos;
		double tkey, ttrail;
		int index_val;

		if (nkey <= 1)
			return;
		for (index_val = last_parent_index; index_val >= 0; index_val--)
			remakeHeap(key, trail, index_val, nkey - 1);

		tkey = key[0];
		key[0] = key[nkey - 1];
		key[nkey - 1] = tkey;
		ttrail = trail[0];
		trail[0] = trail[nkey - 1];
		trail[nkey - 1] = ttrail;

		for (index_val = nkey - 2; index_val > 0; index_val--) {
			remakeHeap(key, trail, 0, index_val);
			tkey = key[0];
			key[0] = key[index_val];
			key[index_val] = tkey;
			ttrail = trail[0];
			trail[0] = trail[index_val];
			trail[index_val] = ttrail;
		}
	}

	public static void remakeHeap(final double key[], final double trail[],
			final int parent_index, final int last_index) {
		int last_parent_pos = (last_index - 1) / 2;
		int last_parent_index = last_parent_pos;
		int l_child;
		int r_child;
		int max_child_index;
		int parent_temp = parent_index;
		double tkey, ttrail;

		while (true) {
			if (parent_temp > last_parent_index)
				break;
			l_child = parent_temp * 2 + 1;
			if (l_child == last_index) {
				max_child_index = l_child;
			} else {
				r_child = l_child + 1;
				if (key[l_child] > key[r_child]) {
					max_child_index = l_child;
				} else {
					max_child_index = r_child;
				}
			}
			if (key[max_child_index] > key[parent_temp]) {
				tkey = key[max_child_index];
				key[max_child_index] = key[parent_temp];
				key[parent_temp] = tkey;
				ttrail = trail[max_child_index];
				trail[max_child_index] = trail[parent_temp];
				trail[parent_temp] = ttrail;
				parent_temp = max_child_index;
			} else {
				break;
			}
		}
	}
}
